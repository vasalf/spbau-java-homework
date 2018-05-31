package ru.spbau.alferov.javahw.myjunit.test;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a test.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
    /**
     * Supposed to be an exception which is never thrown.
     */
    final class NoExceptionHolder extends Throwable {
        /**
         * One does not simply create instance.
         */
        private NoExceptionHolder() {}
    }

    /**
     * The exception expected to be thrown.
     */
    @NotNull Class<? extends Throwable> expected() default NoExceptionHolder.class;

    /**
     * The reason why the test is ignored (or empty string if the test is not ignored, which is default).
     */
    @NotNull String ignored() default "";
}
