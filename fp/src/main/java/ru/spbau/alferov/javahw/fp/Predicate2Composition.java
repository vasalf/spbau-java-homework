package ru.spbau.alferov.javahw.fp;

/**
 * A helper class for compositions of predicates and Function2
 */
public class Predicate2Composition<X> extends Predicate<X> {
    /**
     * Predicate binded to the first argument.
     */
    private Predicate<X> firstNested;

    /**
     * Predicate binded to the second argument.
     */
    private Predicate<X> secondNested;

    /**
     * The outer predicate of the composition.
     */
    private Function2<Boolean, Boolean, Boolean> outer;

    /**
     * Basic constructor.
     */
    public Predicate2Composition(Predicate<X> first, Predicate<X> second, Function2<Boolean, Boolean, Boolean> effect) {
        firstNested = first;
        secondNested = second;
        outer = effect;
    }

    /**
     * Returns the composition.
     */
    @Override
    public Boolean apply(X arg) {
        return outer.apply(firstNested.apply(arg), secondNested.apply(arg));
    }
}
