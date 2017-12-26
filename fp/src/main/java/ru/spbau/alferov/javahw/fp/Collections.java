package ru.spbau.alferov.javahw.fp;

import java.util.LinkedList;
import java.util.List;

/**
 * This class provides special Iterable-based functions for Function1, Function2 and Predicates
 */
public class Collections {
    /**
     * Applies the Function1 to all of the elements of some Iterable object a.
     */
    static <T, S> List<S> map(Function1<? super T, S> f, Iterable<T> a) {
        List<S> ret = new LinkedList<>();
        for (T t : a) {
            ret.add(f.apply(t));
        }
        return ret;
    }

    /**
     * Returns all of the elements of some Iterable object a that matches the predicate p.
     */
    static <T> List<T> filter(Predicate<? super T> p, Iterable<T> a) {
        List<T> ret = new LinkedList<>();
        for (T t : a) {
            if (p.apply(t)) {
                ret.add(t);
            }
        }
        return ret;
    }

    /**
     * Takes elements of some Iterable object a while they match the predicate p.
     */
    static <T> List<T> takeWhile(Predicate<? super T> p, Iterable<T> a) {
        return filter(new Predicate1Composition<>(p, new Predicate<Boolean>() {
            boolean wasFalse = false;
            @Override
            public Boolean apply(Boolean arg) {
                if (wasFalse) {
                    return false;
                }
                if (!arg) {
                    wasFalse = true;
                }
                return arg;
            }
        }), a);
    }

    /**
     * Takes elements of some Iterable object a while they don't match the predicate p.
     */
    static <T> List<T> takeUnless(Predicate<? super T> p, Iterable<T> a) {
        return takeWhile(p.not(), a);
    }

    /**
     * Left-associative folding of Iterable objects.
     */
    static <A, B> B foldl(Function2<B, ? super A, B> function, B init, Iterable<A> a) {
        for (A elem : a) {
            init = function.apply(init, elem);
        }
        return init;
    }

    /**
     * Right-associative folding of Iterable objects.
     */
    static <A, B> B foldr(Function2<? super A, B, B> function, B init, Iterable<A> a) {
        List<A> viewed = new LinkedList<>();
        for (A elem : a) {
            viewed.add(elem);
        }
        for (A elem : viewed) {
            init = function.apply(elem, init);
        }
        return init;
    }
}
