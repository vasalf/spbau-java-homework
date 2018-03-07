package ru.spbau.alferov.javahw.threadpool;

/**
 * This is an exception to be thrown in case some task from the pool finished with an exception.
 */
public class LightExecutionException extends Exception {
    /**
     * Constructor for the exception.
     * @param cause The Throwable object thrown by the task.
     */
    public LightExecutionException(Throwable cause) {
        super("Task execution finished with an exception.", cause);
    }
}
