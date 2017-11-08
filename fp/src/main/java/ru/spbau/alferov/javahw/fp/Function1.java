package ru.spbau.alferov.javahw.fp;

/**
 * This class provides an interface for functions of one argument.
 * @param <X> Type of the argument.
 * @param <R> Type of the return value.
 */
public abstract class Function1<X, R> {

    /**
     * Apply the function to the argument and return the return value.
     */
    public abstract R apply(X arg);

    /**
     * Composition of functions of one argument.
     * Denote the basic (this) function by f. Than this returns the function g(f(x)).
     */
    public <S> Function1<X, S> compose(Function1<R, S> g) {
        return new Function1Composition<X, R, S>(this, g);
    }
}
