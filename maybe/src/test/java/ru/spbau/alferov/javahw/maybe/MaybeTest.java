package ru.spbau.alferov.javahw.maybe;

import org.junit.Test;

import static org.junit.Assert.*;

public class MaybeTest {

    /**
     * Tests that just() static constructor works correctly.
     * In particular, it creates a stored value and get() method
     * returns the value expected from him.
     */
    @Test
    public void just() throws Exception {
        Maybe<String> abc = Maybe.just("abc");
        assertTrue(abc.isPresent());
        assertEquals("abc", abc.get());
        Maybe<String> empty = Maybe.just("");
        assertTrue(empty.isPresent());
        assertEquals("", empty.get());
    }

    /**
     * Tests that nothing() static constructor works correctly.
     * In particular, it creates a non-stored value and get() throws
     * MaybeException.
     */
    @Test
    public void nothing() throws Exception {
        Maybe<Double> md = Maybe.nothing();
        assertFalse(md.isPresent());
        try {
            md.get();
        } catch (MaybeException e) {
            //ok
            return;
        }
        assertTrue("Did not catch the MaybeException", false);
    }

    /**
     * Tests that map() function works correctly.
     * In particular, it works for Maybe.nothing() and it works for
     * either same or different types (T and U).
     */
    @Test
    public void map() throws Exception {
        Maybe<String> abc = Maybe.just("abc");
        Maybe<String> res1 = abc.map(s -> s + "179");
        assertTrue(res1.isPresent());
        assertEquals("abc179", res1.get());

        Maybe<Integer> a = Maybe.just((int)1e9);
        Maybe<Long> res2 = a.map(x -> (long)x * (long)x);
        assertTrue(res2.isPresent());
        assertEquals((long)1e18, (long)res2.get());

        Maybe<Double> n = Maybe.nothing();
        Maybe<Double> res3 = n.map(x -> x * x);
        assertFalse(res3.isPresent());
    }

}