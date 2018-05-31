package ru.spbau.alferov.javahw.myjunit.launcher.event;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InvocationResult {
    private int ignoredTests = 0;

    private int passedTests = 0;

    private int failedTests = 0;

    @NotNull
    private List<IgnoredTestEvent> ignoredTestEvents = new ArrayList<>();

    @NotNull
    private List<FailedTestEvent> failedTestEvents = new ArrayList<>();

    @NotNull
    private List<PassedTestEvent> passedTestEvents = new ArrayList<>();

    public int getIgnoredTests() {
        return ignoredTests;
    }

    public int getPassedTests() {
        return passedTests;
    }

    public int getFailedTests() {
        return failedTests;
    }

    public List<IgnoredTestEvent> getIgnoredTestEvents() {
        return ignoredTestEvents;
    }

    public List<FailedTestEvent> getFailedTestEvents() {
        return failedTestEvents;
    }

    public List<PassedTestEvent> getPassedTestEvents() {
        return passedTestEvents;
    }

    public void addIgnoredTest(IgnoredTestEvent event) {
        ignoredTests++;
        ignoredTestEvents.add(event);
    }

    public void addPassedTest(PassedTestEvent event) {
        passedTests++;
        passedTestEvents.add(event);
    }

    public void addFailedTest(FailedTestEvent event) {
        failedTests++;
        failedTestEvents.add(event);
    }
}
