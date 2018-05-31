package ru.spbau.alferov.javahw.myjunit.launcher.event;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.myjunit.launcher.InvocationFailure;

import java.time.Duration;

/**
 * This represents a failed test
 */
public class FailedTestEvent {
    /**
     * Name of the test
     */
    @NotNull
    private String testName;

    /**
     * Reason of failure
     */
    @NotNull
    private InvocationFailure reason;

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
     * @return Reason of failure
     */
    @NotNull
    public InvocationFailure getReason() {
        return reason;
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
    public FailedTestEvent(@NotNull String testName, @NotNull InvocationFailure reason, @NotNull Duration executionTime) {
        this.testName = testName;
        this.reason = reason;
        this.executionTime = executionTime;
    }
}
