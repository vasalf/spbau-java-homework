package ru.spbau.alferov.javahw.fp;

/**
 * This class provides some special functionality for Function1&lt;X, Boolean&gt;
 */
public abstract class Predicate<X> extends Function1<X, Boolean> {
    /**
     * Returns the function f(x) || rt(x).
     */
    public Predicate<X> or(Predicate<X> rt) {
        return new Predicate2Composition<>(this, rt, new Function2<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean apply(Boolean arg1, Boolean arg2) {
                return arg1 || arg2;
            }
        });
    }

    /**
     * Returns the function f(x) && rt(x).
     */
    public Predicate<X> and(Predicate<X> rt) {
        return new Predicate2Composition<>(this, rt, new Function2<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean apply(Boolean arg1, Boolean arg2) {
                return arg1 && arg2;
            }
        });
    }

    /**
     * Returns the function !f(x).
     */
    public Predicate<X> not() {
        return new Predicate1Composition<>(this, new Predicate<Boolean>() {
            @Override
            public Boolean apply(Boolean arg) {
                return !arg;
            }
        });
    }

    /**
     * This returns an always true Predicate&lt;T, X&gt;
     */
    public static <T> Predicate<T> ALWAYS_TRUE() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T arg) {
                return true;
            }
        };
    }


    /**
     * This returns an always false Predicate&lt;T, X&gt;
     */
    public static <T> Predicate<T> ALWAYS_FALSE() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T arg) {
                return false;
            }
        };
    }
}
