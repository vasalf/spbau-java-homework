package ru.spbau.alferov.javahw.fp;

/**
 * A helper class for composition of predicates.
 */
public class Predicate1Composition<X> implements Predicate<X> {
    /**
     * The inner predicate of the composition.
     */
    private Predicate<X> inner;

    /**
     * The outer predicate of the composition
     */
    private Predicate<Boolean> outer;

    /**
     * Basic constructor.
     */
    public Predicate1Composition(Predicate<X> innerPred, Predicate<Boolean> outerPred) {
        inner = innerPred;
        outer = outerPred;
    }

    /**
     * Returns the composition of the predicates.
     */
    @Override
    public Boolean apply(X arg) {
        return outer.apply(inner.apply(arg));
    }
}
