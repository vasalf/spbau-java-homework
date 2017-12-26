package ru.spbau.alferov.javahw.fp;

/**
 * A helper class for composing Function1 and Function2.
 * @param <X> Type of first argument of composition.
 * @param <Y> Type of second argument of composition.
 * @param <R> Return type of inner function.
 * @param <S> Return type of composition.
 */
public class Function2Composition<X, Y, R, S> implements Function2<X, Y, S> {
    /**
     * Inner function of the composition.
     */
    private Function2<X, Y, R> inner;

    /**
     * Outer function of the composition.
     */
    private Function1<R, S> outer;

    /**
     * Basic constructor.
     */
    public Function2Composition(Function2<X, Y, R> innerFunc, Function1<R, S> outerFunc) {
        inner = innerFunc;
        outer = outerFunc;
    }

    /**
     * Returns the composition.
     */
    @Override
    public S apply(X arg1, Y arg2) {
        return outer.apply(inner.apply(arg1, arg2));
    }
}
