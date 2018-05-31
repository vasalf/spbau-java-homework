package ru.spbau.alferov.javahw.myjunit.launcher;

/**
 * This represents an error after which the full invocation should be stopped.
 */
public class LauncherException extends Exception {
    /**
     * Base constructor without cause.
     */
    public LauncherException(String message) {
        super(message);
    }

    /**
     * Base constructor with cause.
     */
    public LauncherException(String message, Throwable cause) {
        super(message, cause);
    }
}
