package ru.spbau.alferov.javahw.myjunit.launcher.event;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.myjunit.launcher.InvocationFailure;

import java.time.Duration;

public class FailedTestEvent {
    @NotNull
    private String testName;

    @NotNull
    private InvocationFailure reason;

    @NotNull
    private Duration executionTime;

    @NotNull
    public String getTestName() {
        return testName;
    }

    @NotNull
    public InvocationFailure getReason() {
        return reason;
    }

    @NotNull
    public Duration getExecutionTime() {
        return executionTime;
    }

    public FailedTestEvent(@NotNull String testName, @NotNull InvocationFailure reason, @NotNull Duration executionTime) {
        this.testName = testName;
        this.reason = reason;
        this.executionTime = executionTime;
    }
}
