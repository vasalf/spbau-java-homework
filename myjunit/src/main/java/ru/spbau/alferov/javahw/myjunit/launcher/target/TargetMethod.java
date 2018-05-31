package ru.spbau.alferov.javahw.myjunit.launcher.target;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.alferov.javahw.myjunit.launcher.InvocationFailure;
import ru.spbau.alferov.javahw.myjunit.launcher.InvocationIgnored;
import ru.spbau.alferov.javahw.myjunit.launcher.LauncherException;
import ru.spbau.alferov.javahw.myjunit.test.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.util.Optional;

public abstract class TargetMethod<T> {
    @NotNull
    protected Method ofMethod;

    @NotNull
    protected CallWhen targetType;

    @NotNull
    public abstract Duration invoke(T onObject) throws InvocationFailure, InvocationIgnored;

    @NotNull
    public CallWhen getTargetType() {
        return targetType;
    }

    @NotNull
    public String getName() {
        return ofMethod.getName();
    }

    protected TargetMethod(@NotNull Method ofMethod, @NotNull CallWhen targetType) {
        this.ofMethod = ofMethod;
        this.targetType = targetType;
    }

    @NotNull
    public static <T> Optional<TargetMethod<T>> createTargetMethod(@NotNull Method method) throws LauncherException {
        @Nullable CallWhen callWhen = null;
        if (method.isAnnotationPresent(Test.class)) {
            callWhen = CallWhen.TEST;
        }
        if (method.isAnnotationPresent(Before.class)) {
            if (callWhen != null) {
                throw new LauncherException("No more than one annotation is allowed on method " + method.getName());
            }
            callWhen = CallWhen.BEFORE;
        }
        if (method.isAnnotationPresent(After.class)) {
            if (callWhen != null) {
                throw new LauncherException("No more than one annotation is allowed on method " + method.getName());
            }
            callWhen = CallWhen.AFTER;
        }
        if (method.isAnnotationPresent(BeforeClass.class)) {
            if (callWhen != null) {
                throw new LauncherException("No more than one annotation is allowed on method " + method.getName());
            }
            callWhen = CallWhen.BEFORE_CLASS;
        }
        if (method.isAnnotationPresent(AfterClass.class)) {
            if (callWhen != null) {
                throw new LauncherException("No more than one annotation is allowed on method " + method.getName());
            }
            callWhen = CallWhen.AFTER_CLASS;
        }
        if (callWhen == null) {
            return Optional.empty();
        } else {
            if ((method.getModifiers() & Modifier.PUBLIC) == 0) {
                throw new LauncherException("Annotated method " + method.getName() + " must be public");
            }
            if ((method.getModifiers() & Modifier.STATIC) != 0) {
                throw new LauncherException("Annotated method " + method.getName() + " must not be static");
            }
            if (method.getReturnType() != void.class) {
                throw new LauncherException("Annotated method " + method.getName() + " must return void");
            }
            if (method.getParameterCount() > 0) {
                throw new LauncherException("Annotated method " + method.getName() + " must not have parameters");
            }
            if (callWhen == CallWhen.TEST) {
                return Optional.of(new TestMethod<>(method, callWhen));
            } else {
                return Optional.of(new HelperMethod<>(method, callWhen));
            }
        }
    }
}
