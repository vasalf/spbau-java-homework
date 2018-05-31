package ru.spbau.alferov.javahw.myjunit.launcher.event;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class PassedTestEvent {
    @NotNull
    private String testName;

    @NotNull
    private Duration executionTime;

    @NotNull
    public String getTestName() {
        return testName;
    }

    @NotNull
    public Duration getExecutionTime() {
        return executionTime;
    }

    @NotNull
    public PassedTestEvent(@NotNull String testName, @NotNull Duration executionTime) {
        this.testName = testName;
        this.executionTime = executionTime;
    }
}
