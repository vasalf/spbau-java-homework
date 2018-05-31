package ru.spbau.alferov.javahw.myjunit.cli;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.myjunit.launcher.Launcher;
import ru.spbau.alferov.javahw.myjunit.launcher.LauncherException;
import ru.spbau.alferov.javahw.myjunit.launcher.event.FailedTestEvent;
import ru.spbau.alferov.javahw.myjunit.launcher.event.IgnoredTestEvent;
import ru.spbau.alferov.javahw.myjunit.launcher.event.InvocationResult;
import ru.spbau.alferov.javahw.myjunit.launcher.event.PassedTestEvent;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * This is the main class of the CLI application.
 */
public class MyJUnitCLI {
    /**
     * <p>This is the entry point of the CLI application.</p>
     *
     * <p>Two command line arguments should be passed to the application: the project root and the classpath.</p>
     */
    @SuppressWarnings("unchecked")
    public static void main(String args[]) {
        if (args.length != 2) {
            System.out.println("You should provide the project root and the class path as arguments");
            return;
        }

        @NotNull String projectRoot = args[0];
        @NotNull String classPath = args[1];

        Class testedClass;

        try {
            URL[] urls = new URL[]{new File(projectRoot).toURI().toURL()};
            ClassLoader classLoader = new URLClassLoader(urls);

            testedClass = classLoader.loadClass(classPath);
        } catch (MalformedURLException e) {
            System.out.println("Could not parse URL: " + e.getMessage());
            return;
        } catch (ClassNotFoundException e) {
            System.out.println("Could not find class: " + e.getMessage());
            return;
        }

        InvocationResult result;

        try {
            result = Launcher.launchTests(testedClass);
        } catch (LauncherException e) {
            System.out.println("Failed to run tests:");
            e.printStackTrace();
            return;
        }

        if (result.getIgnoredTests() > 0) {
            System.out.println("\nIgnored tests: ");
            for (IgnoredTestEvent event : result.getIgnoredTestEvents()) {
                System.out.println(event.getTestName() + ": " + event.getReason());
            }
        }
        if (result.getPassedTests() > 0) {
            System.out.println("\nPassed tests: ");
            for (PassedTestEvent event : result.getPassedTestEvents()) {
                System.out.println(event.getTestName() + " in " + event.getExecutionTime().toMillis() + " ms");
            }
        }
        if (result.getFailedTests() > 0) {
            System.out.println("\nFailed tests: ");
            for (FailedTestEvent event : result.getFailedTestEvents()) {
                System.out.println(event.getTestName() + " in " + event.getExecutionTime().toMillis() + " ms");
                event.getReason().printStackTrace();
            }
        }
        System.out.printf("\nTotal:\nignored: %d, passed: %d, failed: %d\n", result.getIgnoredTests(),
                result.getPassedTests(), result.getFailedTests());
    }
}
