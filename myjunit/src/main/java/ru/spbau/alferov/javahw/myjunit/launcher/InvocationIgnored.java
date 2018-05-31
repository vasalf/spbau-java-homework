package ru.spbau.alferov.javahw.myjunit.launcher;

/**
 * This is thrown in case of an ignored test.
 * This should never get out of {@link Launcher#launchTests(Class)}.
 */
public class InvocationIgnored extends Exception {
    /**
     * Base constructor
     *
     * @param reason Reason for the test being ignored
     */
    public InvocationIgnored(String reason) {
        super(reason);
    }
}
