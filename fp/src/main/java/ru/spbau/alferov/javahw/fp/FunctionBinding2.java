package ru.spbau.alferov.javahw.fp;

/**
 * A helper class for binding second argument of Function2
 * @param <X> Type of the first argument.
 * @param <Y> Type of the second argument.
 * @param <R> Type of the result.
 */
public class FunctionBinding2<X, Y, R> implements Function1<X, R> {
    /**
     * Binded argument.
     */
    private Y arg2;

    /**
     * The applied Function2.
     */
    private Function2<X, Y, R> binded;

    /**
     * Basic constructor.
     */
    public FunctionBinding2(Function2<X, Y, R> to_bind, Y arg) {
        binded = to_bind;
        arg2 = arg;
    }

    /**
     * Returns the binding.
     */
    @Override
    public R apply(X arg) {
        return binded.apply(arg, arg2);
    }
}
