package ru.spbau.alferov.javahw.fp;

/**
 * This class provides some special functionality for Function1&lt;X, Boolean&gt;
 */
public interface Predicate<X> extends Function1<X, Boolean> {
    /**
     * Returns the function f(x) || rt(x).
     */
    default Predicate<X> or(Predicate<? super X> rt) {
        return it -> apply(it) || rt.apply(it);
    }

    /**
     * Returns the function f(x) && rt(x).
     */
    default Predicate<X> and(Predicate<X> rt) {
        return it -> apply(it) && rt.apply(it);
    }

    /**
     * Returns the function !f(x).
     */
    default Predicate<X> not() {
        return it -> !apply(it);
    }
}
