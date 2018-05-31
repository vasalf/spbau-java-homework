package ru.spbau.alferov.javahw.myjunit.launcher.event;

import org.jetbrains.annotations.NotNull;

public class IgnoredTestEvent {
    @NotNull
    private String testName;

    @NotNull
    private String reason;

    @NotNull
    public String getTestName() {
        return testName;
    }

    @NotNull
    public String getReason() {
        return reason;
    }

    public IgnoredTestEvent(@NotNull String testName, @NotNull String reason) {
        this.testName = testName;
        this.reason = reason;
    }
}
