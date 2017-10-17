package ru.spbau.alferov.javahw.maybe;

public class MaybeException extends Exception {
    public MaybeException() {
        super("Trying to get value from Maybe.nothing");
    }
}