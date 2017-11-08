package ru.spbau.alferov.javahw.fp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Special tests for special predicate functions
 */
public class PredicateTest {
    /**
     * Test for or().
     */
    @Test
    public void or() {
        Predicate<Integer> eq1 = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg == 1;
            }
        };
        Predicate<Integer> eq2 = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg == 2;
            }
        };
        Predicate<Integer> eq3 = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg == 3;
            }
        };
        Predicate<Integer> disj = eq1.or(eq2).or(eq3);

        assertTrue(disj.apply(1));
        assertTrue(disj.apply(2));
        assertTrue(disj.apply(3));
        assertFalse(disj.apply(4));
    }

    /**
     * Test for and().
     */
    @Test
    public void and() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg % 2 == 0;
            }
        };
        Predicate<Integer> isLessThan10 = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg < 10;
            }
        };
        Predicate<Integer> conj = isEven.and(isLessThan10);

        assertTrue(conj.apply(8));
        assertFalse(conj.apply(7));
        assertFalse(conj.apply(12));
    }

    /**
     * Test for not().
     */
    @Test
    public void not() {
        Predicate<Integer> nonNegative = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return 0 <= arg;
            }
        };
        Predicate<Integer> lessThan10 = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg < 10;
            }
        };
        Predicate<Integer> inInterval = nonNegative.and(lessThan10);

        assertTrue(inInterval.not().apply(179));
        assertFalse(inInterval.not().apply(5));
    }

    /**
     * Tests always true predicate and its conjuctions and disjuctions.
     */
    @Test
    public void ALWAYS_TRUE() {
        Predicate<String> a = Predicate.ALWAYS_TRUE();
        Predicate<Integer> b = Predicate.ALWAYS_TRUE();

        assertTrue(a.apply("a"));
        assertTrue(a.apply(""));
        assertTrue(b.apply(0));
        assertTrue(b.apply(1));

        Predicate<String> non_empty = new Predicate<String>() {
            @Override
            public Boolean apply(String arg) {
                return arg.length() != 0;
            }
        };
        Predicate<Integer> not_zero = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg != 0;
            }
        };

        assertTrue(a.or(non_empty).apply("abc"));
        assertFalse(b.and(not_zero).apply(0));
    }

    /**
     * Tests always false predicate and its conjuctions and disjuctions.
     */
    @Test
    public void ALWAYS_FALSE() {
        Predicate<String> a = Predicate.ALWAYS_FALSE();
        Predicate<Integer> b = Predicate.ALWAYS_FALSE();

        assertFalse(a.apply("a"));
        assertFalse(a.apply(""));
        assertFalse(b.apply(0));
        assertFalse(b.apply(1));

        Predicate<String> non_empty = new Predicate<String>() {
            @Override
            public Boolean apply(String arg) {
                return arg.length() != 0;
            }
        };
        Predicate<Integer> not_zero = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg != 0;
            }
        };

        assertTrue(a.or(non_empty).apply("abc"));
        assertFalse(b.and(not_zero).apply(123));
    }

}