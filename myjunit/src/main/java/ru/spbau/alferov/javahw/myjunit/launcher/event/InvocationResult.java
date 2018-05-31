package ru.spbau.alferov.javahw.myjunit.launcher.event;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result of test invocation
 */
public class InvocationResult {
    /**
     * Number of ignored tests
     */
    private int ignoredTests = 0;
    /**
     * Number of passed tests
     */
    private int passedTests = 0;
    /**
     * Number of failed tests
     */
    private int failedTests = 0;

    /**
     * List of ignored tests
     */
    @NotNull
    private List<IgnoredTestEvent> ignoredTestEvents = new ArrayList<>();

    /**
     * List of failed tests
     */
    @NotNull
    private List<FailedTestEvent> failedTestEvents = new ArrayList<>();

    /**
     * List of passed tests
     */
    @NotNull
    private List<PassedTestEvent> passedTestEvents = new ArrayList<>();

    /**
     * @return Number of ignored tests
     */
    public int getIgnoredTests() {
        return ignoredTests;
    }

    /**
     * @return Number of passed tests
     */
    public int getPassedTests() {
        return passedTests;
    }

    /**
     * @return Number of failed tests
     */
    public int getFailedTests() {
        return failedTests;
    }

    /**
     * @return List of ignored tests
     */
    public List<IgnoredTestEvent> getIgnoredTestEvents() {
        return ignoredTestEvents;
    }

    /**
     * @return List of failed tests
     */
    public List<FailedTestEvent> getFailedTestEvents() {
        return failedTestEvents;
    }

    /**
     * @return List of passed tests
     */
    public List<PassedTestEvent> getPassedTestEvents() {
        return passedTestEvents;
    }

    /**
     * Adds new ignored test
     */
    public void addIgnoredTest(IgnoredTestEvent event) {
        ignoredTests++;
        ignoredTestEvents.add(event);
    }

    /**
     * Adds new passed test
     */
    public void addPassedTest(PassedTestEvent event) {
        passedTests++;
        passedTestEvents.add(event);
    }

    /**
     * Adds new failed test
     */
    public void addFailedTest(FailedTestEvent event) {
        failedTests++;
        failedTestEvents.add(event);
    }
}
