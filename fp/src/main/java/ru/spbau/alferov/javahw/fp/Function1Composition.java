package ru.spbau.alferov.javahw.fp;

public class Function1Composition<X, R, S> extends Function1<X, S> {
    private Function1<X, R> inner;
    private Function1<R, S> outer;

    public Function1Composition(Function1<X, R> innerFunction, Function1<R, S> outerFunction) {
        inner = innerFunction;
        outer = outerFunction;
    }

    @Override
    public S apply(X arg) {
        return outer.apply(inner.apply(arg));
    }
}
