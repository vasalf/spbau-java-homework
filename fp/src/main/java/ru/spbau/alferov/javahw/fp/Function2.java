package ru.spbau.alferov.javahw.fp;

/**
 * This class provides an interface for functions of two arguments.
 * @param <X> Type of the first argument.
 * @param <Y> Type of the second argument.
 * @param <R> Type of the return value.
 */
public interface Function2<X, Y, R> {

    /**
     * Apply the function to the arguments and return the return value.
     */
    R apply(X arg1, Y arg2);

    /**
     * Composition of functions of one argument and two arguments.
     * Denote the basic (this) function by f. Than this returns the function g(f(x, y)).
     */
    default <S> Function2<X, Y, S> compose(Function1<R, S> g) {
        return new Function2Composition<>(this, g);
    }

    /**
     * Binds the first argument.
     * @return Function f(arg, x);
     */
    default Function1<Y, R> bind1(X arg) {
        return new FunctionBinding1<>(this, arg);
    }

    /**
     * Binds the second argument.
     * @return Function f(x, arg);
     */
    default Function1<X, R> bind2(Y arg) {
        return new FunctionBinding2<>(this, arg);
    }

    /**
     * <s>For some strange reason in the specification this is the same as bind2.</s>
     */
    default Function1<X, R> curry(Y arg) {
        return new FunctionBinding2<>(this, arg);
    }
}
