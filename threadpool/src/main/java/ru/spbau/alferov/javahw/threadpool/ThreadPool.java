package ru.spbau.alferov.javahw.threadpool;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class provides a thread pool with some task dependency support.
 */
public class ThreadPool {
    /**
     * <p>This is the {@link LightFuture<R>} implementation for the ThreadPool.</p>
     *
     * <p>Instances of this class are stored as Runnable in the ThreadPool for being
     * executed in threads with no binding to Suppliers' return type. But this class
     * also implements LightFuture&lt;R&gt; so all necessary type checks are
     * performed.</p>
     */
    private class LightFutureImpl<R> implements Runnable, LightFuture<R> {
        /**
         * Here the result of the execution is being stored.
         */
        @Nullable
        private R result = null;

        /**
         * Here the supplier to be called is being stored.
         */
        @NotNull
        private Supplier<R> supplier;

        /**
         * Here the exception thrown by the supplier (if any) is being stored.
         */
        @Nullable
        private Throwable exception;

        /**
         * This is set to true iff the execution has ended.
         */
        private volatile boolean finished = false;

        /**
         * This is the conditional variable used by threads for sleeping until
         * the execution has finished.
         */
        private final Object conditionalVariable = new Object();

        /**
         * Constructs the task. Does not start the execution.
         */
        public LightFutureImpl(@NotNull Supplier<R> worker) {
            supplier = worker;
        }

        /**
         * Executes the task. Does not create a new thread.
         */
        @Override
        public void run() {
            try {
                result = supplier.get();
            } catch (Throwable t) {
                exception = t;
            }
            finished = true;
            synchronized (conditionalVariable) {
                conditionalVariable.notifyAll();
            }
        }

        /**
         * {@link LightFuture#isReady()} implementation.
         */
        @Override
        public boolean isReady() {
            return finished;
        }

        /**
         * {@link LightFuture#get()} implementation.
         */
        @Override
        @Nullable
        public R get() throws LightExecutionException, InterruptedException {
            while (!finished) {
                synchronized (conditionalVariable) {
                    if (!finished) {
                        conditionalVariable.wait();
                    }
                }
            }
            if (exception != null)
                throw new LightExecutionException(exception);

            return result;
        }

        /**
         * <p>{@link LightFuture#thenApply(Function)} implementation.</p>
         *
         * <p>The execution of _this_ task might have finished with exception and
         * might have been interrupted. In this case {@link RuntimeException}
         * is thrown in the supplier and {@link LightExecutionException} will occur
         * in the corresponding {@link LightFuture#get()} caused by that
         * {@link RuntimeException}</p>.
         */
        @Override
        @NotNull
        public <S> LightFuture<S> thenApply(Function<R, S> toApply) {
            return acceptTask(() -> {
                try {
                    return toApply.apply(LightFutureImpl.this.get());
                } catch (LightExecutionException lex) {
                    throw new RuntimeException("Attempt to get value from a task finished with exception");
                } catch (InterruptedException iex) {
                    throw new RuntimeException("Attempt to get value from an interrupted task");
                }
            });
        }
    }

    /**
     * Here all of the running threads are being stored.
     */
    @NotNull
    private Thread[] runningThreads;

    /**
     * This is a queue for tasks waiting for a free thread.
     * The queue is also used as a conditional variable for threads waiting for incoming tasks.
     */
    private final Queue<Runnable> waitingRunnables = new LinkedList<>();

    /**
     * <p>Creates a thread pool.</p>
     *
     * <p>All of the threads are started on the class creation.</p>
     *
     * @param threads Number of threads to be started.
     */
    public ThreadPool(int threads) {
        Runnable runnable = () -> {
            while (true) {
                Runnable task;
                synchronized (waitingRunnables) {
                    while (waitingRunnables.isEmpty()) {
                        try {
                            waitingRunnables.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    task = waitingRunnables.poll();
                }
                task.run();
            }
        };
        runningThreads = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            runningThreads[i] = new Thread(runnable);
            runningThreads[i].start();
        }
    }

    /**
     * <p>Accepts the task for execution.</p>
     *
     * <p>If some of the threads is waiting, the execution will start immediately.
     * Otherwise the task is put in the queue and will wait its turn.</p>
     *
     * @param taskForExecution The task for being executed.
     * @param <R> The result object type of the task.
     * @return A LightFuture instance provided for the task.
     */
    @NotNull
    public <R> LightFuture<R> acceptTask(Supplier<R> taskForExecution) {
        LightFutureImpl<R> ret = new LightFutureImpl<>(taskForExecution);
        synchronized (waitingRunnables) {
            waitingRunnables.add(ret);
            waitingRunnables.notify();
        }
        return ret;
    }

    /**
     * <p>Interrupts execution of all of the threads.</p>
     *
     * <p>No accepted task is being executed after this function is called.</p>
     */
    void shutdown() {
        for (Thread thread : runningThreads) {
            thread.interrupt();
        }
    }
}
