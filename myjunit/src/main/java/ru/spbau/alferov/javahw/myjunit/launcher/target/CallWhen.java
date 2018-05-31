package ru.spbau.alferov.javahw.myjunit.launcher.target;

/**
 * Describes cause in which an annotated function should be called
 */
public enum CallWhen {
    /**
     * For functions annotated with {@link ru.spbau.alferov.javahw.myjunit.test.Test}
     */
    TEST,

    /**
     * For functions annotated with {@link ru.spbau.alferov.javahw.myjunit.test.Before}
     */
    BEFORE,

    /**
     * For functions annotated with {@link ru.spbau.alferov.javahw.myjunit.test.After}
     */
    AFTER,

    /**
     * For functions annotated with {@link ru.spbau.alferov.javahw.myjunit.test.BeforeClass}
     */
    BEFORE_CLASS,

    /**
     * For functions annotated with {@link ru.spbau.alferov.javahw.myjunit.test.AfterClass}
     */
    AFTER_CLASS
}
