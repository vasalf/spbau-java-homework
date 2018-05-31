package ru.spbau.alferov.javahw.myjunit.launcher;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class InvocationFailure extends Exception {
    @NotNull
    private Duration duration;

    public Duration getDuration() {
        return duration;
    }

    public InvocationFailure(@NotNull Duration duration, String message) {
        super(message);
        this.duration = duration;
    }

    public InvocationFailure(@NotNull Duration duration, String message, Throwable reason) {
        super(message, reason);
        this.duration = duration;
    }
}
