package ru.spbau.alferov.javahw.simpleftp.gui.filetree;

/**
 * This should represent any exception thrown in {@link Visitor} implementations.
 */
public class VisitorException extends Exception {
    public VisitorException(String message, Throwable reason) {
        super(message, reason);
    }
}
