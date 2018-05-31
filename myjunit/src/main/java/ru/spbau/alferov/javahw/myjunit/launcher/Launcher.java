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


public class Launcher<T> {
    @NotNull
    private Class<T> inClass;

    @NotNull
    private Constructor<T> defaultConstructor;

    @NotNull
    private Map<CallWhen, List<TargetMethod<T>>> targetMethods;

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

    private Launcher(@NotNull Class<T> inClass) throws LauncherException {
        this.inClass = inClass;
        initConstructor();
        initMethodList();
    }

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
