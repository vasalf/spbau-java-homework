package ru.spbau.alferov.javahw.myjunit.launcher;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 * This is thrown in case of a failed test.
 * This should never get out of {@link Launcher#launchTests(Class)} method.
 */
public class InvocationFailure extends Exception {
    /**
     * Time elapsed till the failure
     */
    @NotNull
    private Duration duration;

    /**
     * @return Time elapsed till the failure
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * Base constructor without cause
     */
    public InvocationFailure(@NotNull Duration duration, String message) {
        super(message);
        this.duration = duration;
    }

    /**
     * Base constructor with a cause
     */
    public InvocationFailure(@NotNull Duration duration, String message, Throwable reason) {
        super(message, reason);
        this.duration = duration;
    }
}
