package ru.spbau.alferov.javahw.myjunit.test;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AfterClass {
}
