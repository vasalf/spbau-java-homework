package ru.spbau.alferov.javahw.myjunit.test;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
    final class NoExceptionHolder extends Throwable {}

    @NotNull Class<? extends Throwable> expected() default NoExceptionHolder.class;
    @NotNull String ignored() default "";
}
