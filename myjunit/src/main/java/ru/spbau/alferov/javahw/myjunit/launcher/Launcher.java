package ru.spbau.alferov.javahw.myjunit.launcher;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.myjunit.launcher.event.FailedTestEvent;
import ru.spbau.alferov.javahw.myjunit.launcher.event.IgnoredTestEvent;
import ru.spbau.alferov.javahw.myjunit.launcher.event.InvocationResult;
import ru.spbau.alferov.javahw.myjunit.launcher.event.PassedTestEvent;
import ru.spbau.alferov.javahw.myjunit.launcher.target.CallWhen;
import ru.spbau.alferov.javahw.myjunit.launcher.target.TargetMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.util.*;

/**
 * This is the main class for launching the tests.
 *
 * @param <T> Class with the tests
 */
public class Launcher<T> {
    /**
     * The class
     */
    @NotNull
    private Class<T> inClass;

    /**
     * The default constructor of the class
     */
    @NotNull
    private Constructor<T> defaultConstructor;

    /**
     * Target methods mapped to time when they are to be executed
     */
    @NotNull
    private Map<CallWhen, List<TargetMethod<T>>> targetMethods;

    /**
     * Initializes the {@link #defaultConstructor}
     *
     * @throws LauncherException In case the class doesn't have an accessible default constructor
     */
    private void initConstructor() throws LauncherException {
        try {
            defaultConstructor = inClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new LauncherException("No default constructor found in class " + inClass.getName());
        }
        if ((defaultConstructor.getModifiers() & Modifier.PUBLIC) == 0) {
            throw new LauncherException("Default constructor in class " + inClass.getName() + " must be public");
        }
    }

    /**
     * Initializes the target methods map
     *
     * @throws LauncherException In case of misused annotations
     */
    private void initMethodList() throws LauncherException {
        targetMethods = ImmutableMap.<CallWhen, List<TargetMethod<T>>>builder()
                .put(CallWhen.TEST, new ArrayList<>())
                .put(CallWhen.BEFORE, new ArrayList<>())
                .put(CallWhen.BEFORE_CLASS, new ArrayList<>())
                .put(CallWhen.AFTER, new ArrayList<>())
                .put(CallWhen.AFTER_CLASS, new ArrayList<>())
                .build();
        for (Method method : inClass.getMethods()) {
            Optional<TargetMethod<T>> target = TargetMethod.createTargetMethod(method);
            target.ifPresent(targetMethod -> targetMethods.get(targetMethod.getTargetType()).add(targetMethod));
        }
    }

    /**
     * Constructs an instance of the tested class
     *
     * @throws LauncherException In case the class is abstract or there were exceptions in the constructor
     */
    public T construct() throws LauncherException {
        try {
            return defaultConstructor.newInstance();
        } catch (InstantiationException e) {
            throw new LauncherException("Class " + inClass.getName() + " must not be declared abstract.");
        } catch (IllegalAccessException | IllegalArgumentException e) {
            // Checking the invocation on being allowed is performed in {@link Launcher#initConstructor}, thus this
            // situation is clearly a bug.
            throw new RuntimeException("BUG: Illegal access or argument exception thrown on a checked constructor", e);
        } catch (InvocationTargetException e) {
            throw new LauncherException("Test object creation failed with exception", e);
        }
    }

    /**
     * Base constructor. Should not be called from outside of the package. Use {@link #launchTests(Class)}.
     */
    private Launcher(@NotNull Class<T> inClass) throws LauncherException {
        this.inClass = inClass;
        initConstructor();
        initMethodList();
    }

    /**
     * Helper method for invoking non-test methods
     *
     * @throws LauncherException In case of failure in the non-test method
     */
    private static <T> void invokeHelperMethod(@NotNull TargetMethod<T> method, @NotNull T onObject) throws LauncherException {
        try {
            method.invoke(onObject);
        } catch (InvocationFailure e) {
            throw new LauncherException("Helper action failed with exception", e.getCause());
        } catch (InvocationIgnored e) {
            // Helper methods are never ignored, thus this situation is clearly a bug.
            throw new RuntimeException("BUG: Could not ignore helper method", e);
        }
    }

    /**
     * <p>Launches all tests in the class.</p>
     *
     * <p>The requirements are:</p>
     * <ul>
     *     <li>There should be an accessible default constructor in the class.</li>
     *     <li>The annotated methods should be "public void" and receive no arguments.</li>
     *     <li>No method is annotated with more than one annotation.</li>
     * </ul>
     *
     * @param inClass Class from which the tests are taken
     * @return The invocation result
     * @throws LauncherException In case of wrong test structure or failure while invoking the tests.
     */
    @NotNull
    public static <T> InvocationResult launchTests(@NotNull Class<T> inClass) throws LauncherException {
        Launcher<T> launcher = new Launcher<>(inClass);

        InvocationResult result = new InvocationResult();
        T testObject = launcher.construct();

        for (@NotNull TargetMethod<T> toCallBefore : launcher.targetMethods.get(CallWhen.BEFORE_CLASS)) {
            invokeHelperMethod(toCallBefore, testObject);
        }
        for (@NotNull TargetMethod<T> testMethod : launcher.targetMethods.get(CallWhen.TEST)) {
            for (@NotNull TargetMethod<T> toCallBefore : launcher.targetMethods.get(CallWhen.BEFORE)) {
                invokeHelperMethod(toCallBefore, testObject);
            }
            try {
                Duration duration = testMethod.invoke(testObject);
                result.addPassedTest(new PassedTestEvent(testMethod.getName(), duration));
            } catch (InvocationIgnored e) {
                result.addIgnoredTest(new IgnoredTestEvent(testMethod.getName(), e.getMessage()));
            } catch (InvocationFailure e) {
                result.addFailedTest(new FailedTestEvent(testMethod.getName(), e, e.getDuration()));
            }
            for (@NotNull TargetMethod<T> toCallAfter : launcher.targetMethods.get(CallWhen.AFTER)) {
                invokeHelperMethod(toCallAfter, testObject);
            }
        }
        for (@NotNull TargetMethod<T> toCallAfter : launcher.targetMethods.get(CallWhen.AFTER_CLASS)) {
            invokeHelperMethod(toCallAfter, testObject);
        }

        return result;
    }
}
