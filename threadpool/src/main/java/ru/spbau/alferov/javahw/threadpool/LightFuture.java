package ru.spbau.alferov.javahw.threadpool;

import java.util.function.Function;

/**
 * This is the interface for accepted tasks.
 *
 * @param <R> The return value type of the task.
 */
public interface LightFuture<R> {
    /**
     * <p>Returns true if the task is completed.</p>
     * <p>Doesn't block the executor.</p>
     */
    boolean isReady();

    /**
     * <p>Blocks the executor until the task is finished.</p>
     *
     * @throws LightExecutionException In case the Supplier finished with exception.
     * @throws InterruptedException In case the thread execution has been interrupted.
     * @return The result of the task (possibly null).
     */
    R get() throws LightExecutionException, InterruptedException;

    /**
     * <p>Puts a new task which applies given function to the result in the pool.</p>
     *
     * <p>The new task waits until this task is finished and then applies the function.</p>
     *
     * <p>This function does not wait this task to be finished.</p>
     *
     * @param toApply Function to be applied on the result.
     * @param <S> The return type of the function.
     * @return The accepted task.
     */
    <S> LightFuture<S> thenApply(Function<R, S> toApply);
}
