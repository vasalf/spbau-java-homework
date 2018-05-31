package ru.spbau.alferov.javahw.myjunit.launcher.event;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 * This represents a passed test
 */
public class PassedTestEvent {
    /**
     * Name of the test
     */
    @NotNull
    private String testName;

    /**
     * Time elapsed
     */
    @NotNull
    private Duration executionTime;

    /**
     * @return Name of the test
     */
    @NotNull
    public String getTestName() {
        return testName;
    }

    /**
     * @return Time elapsed
     */
    @NotNull
    public Duration getExecutionTime() {
        return executionTime;
    }

    /**
     * Base constructor
     */
    @NotNull
    public PassedTestEvent(@NotNull String testName, @NotNull Duration executionTime) {
        this.testName = testName;
        this.executionTime = executionTime;
    }
}
