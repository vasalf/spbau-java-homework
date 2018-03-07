package ru.spbau.alferov.javahw.threadpool;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is the class for {@link ThreadPool} unit tests.
 */
class ThreadPoolTest {
    /**
     * In this test the ThreadPool is created and immediately shut down.
     */
    @Test
    public void createAndShutdown() throws Exception {
        ThreadPool threadPool = new ThreadPool(179);
        threadPool.shutdown();
    }

    /**
     *  In this task a single task is performed in the ThreadPool.
     */
    @Test
    public void oneTask() throws Exception {
        ThreadPool threadPool = new ThreadPool(2);
        LightFuture<Integer> future = threadPool.acceptTask(() -> 179);
        Integer value = future.get();
        threadPool.shutdown();
        assertEquals(new Integer(179), value);
    }

    /**
     *  In this test we check that get function throws an exception in case
     *  the task execution was interrupted (by {@link ThreadPool#shutdown()},
     *  for example).
     */
    @Test
    public void shutdownThrowsException() throws Exception {
        ThreadPool threadPool = new ThreadPool(2);
        LightFuture<Integer> future = threadPool.acceptTask(() -> {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted", e);
            }
            return 179;
        });
        threadPool.shutdown();
        assertThrows(LightExecutionException.class, future::get);
    }

    /**
     * <p>This is the manager of multi-thread parties.</p>
     *
     * <p>partySize threads are invited to a party. They are coming
     * to the place one by one. When all of them are here, they start
     * having fun.</p>
     */
    private static class Party {
        /**
         * Number of threads which have already joined the party.
         */
        private volatile int joined = 0;

        /**
         * Number of invited threads.
         */
        private final int waitingFor;

        /**
         * Constructs a party object.
         *
         * @param partySize Number of invitations.
         */
        Party(int partySize) {
            waitingFor = partySize;
        }

        /**
         * This function is called by a thread when it joins the party.
         * It blocks the thread until all of the invitees come.
         */
        public synchronized void join() throws InterruptedException {
            joined++;
            if (joined == waitingFor) {
                notifyAll();
            } else {
                while (joined < waitingFor) {
                    wait();
                }
            }
        }
    }

    /**
     * This is a helper function for two tests.
     * It checks that if there are numTasks threads and numTasks tasks
     * and all of them can be executed simultaneously and they are all
     * waiting for each other to start working, then exactly all of the
     * threads are used.
     */
    private void testNTasksInNThreads(int numTasks) throws Exception {
        ThreadPool threadPool = new ThreadPool(numTasks);
        Party threadParty = new Party(numTasks);
        Set<Long> inThreads = new HashSet<>();
        Supplier<String> sup = () -> {
            synchronized (inThreads) {
                inThreads.add(Thread.currentThread().getId());
            }

            try {
                threadParty.join();
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted", e);
            }

            return "Nice job!";
        };

        LightFuture futures[] = new LightFuture[numTasks];
        for (int i = 0; i < numTasks; i++)
            futures[i] = threadPool.acceptTask(sup);

        for (int i = 0; i < numTasks; i++)
            futures[i].get();
        threadPool.shutdown();

        assertEquals(numTasks, inThreads.size());
    }

    /**
     * {@link ThreadPoolTest#testNTasksInNThreads(int)} on two tasks
     * and threads.
     */
    @RepeatedTest(100)
    public void twoTasksTwoThreads() throws Exception {
        testNTasksInNThreads(2);
    }

    /**
     * {@link ThreadPoolTest#testNTasksInNThreads(int)} on one hundred
     * tasks and threads.
     */
    @RepeatedTest(100)
    public void hudredTasksHundredThreads() throws Exception {
        testNTasksInNThreads(100);
    }

    /**
     * <p>A stress test on race condition. Runs one hundred tasks in
     * ten threads. The tasks work for random time (medium is 50 ms).</p>
     *
     * <p>It might be also useful to look at the execution time of
     * the test. It should be near 500ms per repetition. It cannot be
     * guaranteed, though, so it is not checked in the test.</p>
     */
    @RepeatedTest(100)
    public void manyRandomSleeps() throws Exception {
        Random rnd = new Random(179);
        ThreadPool threadPool = new ThreadPool(10);
        LightFuture[] futures = new LightFuture[100];
        for (int i = 0; i < 100; i++) {
            futures[i] = threadPool.acceptTask(() -> {
               long millis_to_sleep =  rnd.nextInt(100);
               try {
                   Thread.sleep(millis_to_sleep);
               } catch (InterruptedException e) {
                   throw new RuntimeException("Interrupted", e);
               }
               return 179;
            });
        }
        for (int i = 0; i < 100; i++) {
            futures[i].get();
        }
        threadPool.shutdown();
    }

    /**
     * This just checks how {@link LightFuture#thenApply(Function)}
     * implementation works in ThreadPool. Just puts one dependent
     * task.
     */
    @Test
    public void simpleDependency() throws Exception {
        ThreadPool threadPool = new ThreadPool(10);
        int answer = threadPool
                .acceptTask(() -> 59)
                .thenApply(x -> 3 * x + 2)
                .get();
        assertEquals(179, answer);
        threadPool.shutdown();
    }

    /**
     * <p>This checks that the dependencies can work consecutively.</p>
     *
     * <p>The test execution time must be nearly 10s. Although there
     * are twp threads created, they cannot be used both in same time
     * because dependants should wait to their parents being executed.</p>
     */
    @Test
    public void tenConsecutiveDependencies() throws Exception {
        ThreadPool threadPool = new ThreadPool(2);
        Function<Integer, Integer> toApply = x -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted", e);
            }
            return 2 * x;
        };
        LightFuture<Integer> start = threadPool.acceptTask(() -> 1);
        LightFuture<Integer> end = start;
        for (int i = 0; i < 10; i++) {
            end = end.thenApply(toApply);
        }
        assertEquals(new Integer(1024), end.get());
        threadPool.shutdown();
    }

    /**
     * <p>This checks that dependencies work even for complex graphs.
     * In this particular case a binary tree of dependants is built.</p>
     *
     * <p>It can be shown that the proper execution time for this test
     * should be near 12 seconds.</p>
     */
    @Test
    public void dependencyBTree() throws Exception {
        ThreadPool threadPool = new ThreadPool(32);
        List<LightFuture<Integer>> currentFutures = new ArrayList<>();
        currentFutures.add(threadPool.acceptTask(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted", e);
            }
            return 1;
        }));
        for (int i = 0; i < 5; i++) {
            List<LightFuture<Integer>> nextFutures = new ArrayList<>();
            for (LightFuture<Integer> future : currentFutures) {
                nextFutures.add(future.thenApply(x -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException("Interrupted", e);
                    }
                    return 2 * x - 1;
                }));
                nextFutures.add(future.thenApply(x -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException("Interrupted", e);
                    }
                    return 2 * x;
                }));
            }
            currentFutures = nextFutures;
        }
        for (int i = 0; i < 32; i++) {
            assertEquals(new Integer(i + 1), currentFutures.get(i).get());
        }
        threadPool.shutdown();
    }

    /**
     *  This is a little demonstration of ThreadPool abilities: one
     *  ThreadPool can accept the Suppliers of any type.
     */
    @Test
    public void differentTypesFuture() throws Exception {
        String secret = "I want to know length of this string.";
        ThreadPool threadPool = new ThreadPool(1);
        LightFuture<String> getString = threadPool.acceptTask(() -> secret);
        LightFuture<Integer> answer = getString.thenApply(String::length);
        assertEquals(new Integer(secret.length()), answer.get());
        threadPool.shutdown();
    }

    /**
     *  This checks that a {@link LightExecutionException} is thrown
     *  in case some unchecked exception happens in the supplier.
     */
    @Test
    public void getExceptionThrown() throws Exception {
        ThreadPool threadPool = new ThreadPool(2);
        LightFuture<Integer> future = threadPool.acceptTask(() -> { throw new RuntimeException(); });
        assertThrows(LightExecutionException.class, future::get);
        threadPool.shutdown();
    }

    /**
     *  This checks that a {@link LightExecutionException} is thrown
     *  in case some unchecked exception happens in the parent supplier
     *  for some dependant task.
     */
    @Test
    public void assertDependentExceptionThrownOnException() throws Exception {
        ThreadPool threadPool = new ThreadPool(2);
        LightFuture<Integer> parent = threadPool.acceptTask(() -> { throw new RuntimeException(); });
        LightFuture<String> dependent = parent.thenApply(Object::toString);
        assertThrows(LightExecutionException.class, dependent::get);
        threadPool.shutdown();
    }

    /**
     *  This checks that a {@link LightExecutionException} is thrown
     *  in case the parent task is interrupted for some dependant task.
     */
    @Test
    public void assertDependentExceptionThrownOnInterrupt() throws Exception {
        Object neverNotifiedConditionalVariable = new Object();
        ThreadPool threadPool = new ThreadPool(2);
        LightFuture<Integer> parent = threadPool.acceptTask(() -> {
            try {
                neverNotifiedConditionalVariable.wait();
            } catch (InterruptedException ie) {
                throw new RuntimeException("Interrupted", ie);
            }
            return 179;
        });
        LightFuture<String> dependent = parent.thenApply(Object::toString);
        threadPool.shutdown();
        assertThrows(LightExecutionException.class, dependent::get);
    }
}