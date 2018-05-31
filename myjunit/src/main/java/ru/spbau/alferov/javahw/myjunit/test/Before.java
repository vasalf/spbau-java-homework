package ru.spbau.alferov.javahw.myjunit.test;

import java.lang.annotation.*;

/**
 * Annotates an action which should be invoked before each test invocation.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Before {
}
