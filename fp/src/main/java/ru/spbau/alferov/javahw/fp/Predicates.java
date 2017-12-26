package ru.spbau.alferov.javahw.fp;

public class Predicates {
    /**
     * This returns an always true Predicate&lt;T, X&gt;
     */
    public static <T> Predicate<T> alwaysTrue() {
        return arg -> true;
    }

    /**
     * This returns an always false Predicate&lt;T, X&gt;
     */
    public static  <T> Predicate<T> alwaysFalse() {
        return arg -> false;
    }
}
