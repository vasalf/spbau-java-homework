package ru.spbau.alferov.javahw.fp;

/**
 * A helper class for binding first argument of Function2
 * @param <X> Type of the first argument.
 * @param <Y> Type of the second argument.
 * @param <R> Type of the result.
 */
public class FunctionBinding1<X, Y, R> implements Function1<Y, R> {
    /**
     * Binded argument.
     */
    private X arg1;

    /**
     * The applied Function2.
     */
    private Function2<X, Y, R> binded;

    /**
     * Basic constructor.
     */
    public FunctionBinding1(Function2<X, Y, R> toBind, X arg) {
        binded = toBind;
        arg1 = arg;
    }

    /**
     * Returns the binding.
     */
    @Override
    public R apply(Y arg2) {
        return binded.apply(arg1, arg2);
    }
}
