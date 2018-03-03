package ru.spbau.alferov.javahw.maybe;

/**
 * This exception is thrown in case of attempt of getting a value
 * from Maybe.nothing().
 */
public class MaybeException extends Exception {
    public MaybeException() {
        super("Trying to get value from Maybe.nothing()");
    }
}