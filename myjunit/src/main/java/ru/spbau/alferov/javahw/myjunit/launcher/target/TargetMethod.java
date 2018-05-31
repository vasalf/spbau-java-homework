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

/**
 * This is a base class for target methods of MyJUnit.
 *
 * @param <T> The class from which tests are taken
 */
public abstract class TargetMethod<T> {
    /**
     * The hold method
     */
    @NotNull
    protected Method ofMethod;

    /**
     * When the method is called.
     */
    @NotNull
    protected CallWhen targetType;

    /**
     * Invokes the method
     *
     * @param onObject On which object to invoke
     * @return Time elapsed
     * @throws InvocationFailure In case of unexpected exceptions in method
     * @throws InvocationIgnored In case of ignored @Test method
     */
    @NotNull
    public abstract Duration invoke(T onObject) throws InvocationFailure, InvocationIgnored;

    /**
     * @return When the method is called.
     */
    @NotNull
    public CallWhen getTargetType() {
        return targetType;
    }

    /**
     * @return The method name.
     */
    @NotNull
    public String getName() {
        return ofMethod.getName();
    }

    /**
     * The base constructor. This constructor should not be called from outside of the package: use
     * {@link #createTargetMethod(Method)} instead.
     */
    protected TargetMethod(@NotNull Method ofMethod, @NotNull CallWhen targetType) {
        this.ofMethod = ofMethod;
        this.targetType = targetType;
    }

    /**
     * Parses annotations of given method and returns the TargetMethod instance, if needed
     *
     * @param method The method from which to parse the annotations
     * @param <T> The class from which the tests are taken
     * @return None if the method is annotated, the TargetMethod instance otherwise
     * @throws LauncherException In case of misused annotations or wrong method signature
     */
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
