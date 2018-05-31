package ru.spbau.alferov.javahw.myjunit.launcher.event;

import org.jetbrains.annotations.NotNull;

/**
 * This represents an ignored test
 */
public class IgnoredTestEvent {
    /**
     * Name of the test
     */
    @NotNull
    private String testName;

    /**
     * Reason of failure
     */
    @NotNull
    private String reason;

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
    public String getReason() {
        return reason;
    }

    /**
     * Base constructor
     */
    public IgnoredTestEvent(@NotNull String testName, @NotNull String reason) {
        this.testName = testName;
        this.reason = reason;
    }
}
