package ru.spbau.alferov.javahw.fp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests specific Collection functions.
 */
public class CollectionsTest {
    /**
     * Tests mapping for non-empty containers.
     */
    @Test
    public void mapOfNonEmptyList() {
        List<Integer> l = Arrays.asList(1, 2, 179);
        List<Integer> m = Collections.map(arg -> (arg - 1) / 2, l);
        int exp[] = {0, 0, 89};
        assertEquals(exp.length, l.size());
        int i = 0;
        for (int t : m) {
            assertEquals(exp[i++], t);
        }
    }

    /**
     * Tests mapping for empty containers.
     */
    @Test
    public void mapEmpty() {
        List<Integer> m = Collections.map((Function1<Integer, Integer>) arg -> null, new ArrayList<>());
        assertEquals(0, m.size());
    }

    /**
     * Tests filtering for non-empty containers.
     */
    @Test
    public void filterOfNonEmptyList() {
        List<Integer> l = Arrays.asList(1, 10, 5, 110);
        List<Integer> m = Collections.filter(arg -> arg < 7, l);
        int exp[] = {1, 5};
        assertEquals(exp.length, m.size());
        int i = 0;
        for (int t : m) {
            assertEquals(exp[i++], t);
        }
    }

    /**
     * Tests filtering for empty containers.
     */
    @Test
    public void filterEmpty() {
        List<Integer> m = Collections.filter(arg -> null, new ArrayList<>());
        assertEquals(0, m.size());
    }

    /**
     * Tests takeWhile for empty and non-empty result.
     */
    @Test
    public void takeWhileFromNonEmptyList() {
        List<Integer> l = Arrays.asList(1, 10, 5, 110);
        List<Integer> m = Collections.takeWhile(arg -> arg < 7, l);
        int exp[] = {1};
        assertEquals(exp.length, m.size());
        int i = 0;
        for (int t : m) {
            assertEquals(exp[i++], t);
        }

        List<Integer> o = Collections.takeWhile(arg -> false, l);
        assertEquals(0, o.size());
    }

    /**
     * Tests takeWhile for empty containers.
     */
    @Test
    public void takeWhileEmpty() {
        List<Integer> m = Collections.takeWhile(arg -> null, new ArrayList<>());
        assertEquals(0, m.size());
    }

    /**
     * Tests takeUnless for empty and non-empty result.
     */
    @Test
    public void takeUnlessFromEmptyList() {
        List<Integer> l = Arrays.asList(1, 10, 5, 110);
        List<Integer> m = Collections.takeUnless(arg -> arg >= 7, l);
        int exp[] = {1};
        assertEquals(exp.length, m.size());
        int i = 0;
        for (int t : m) {
            assertEquals(exp[i++], t);
        }

        List<Integer> o = Collections.takeUnless(arg -> true, l);
        assertEquals(0, o.size());
    }

    /**
     * Tests takeUnless for empty containers.
     */
    @Test
    public void takeUnlessEmpty() {
        List<Integer> m = Collections.takeUnless(arg -> null, new ArrayList<>());
        assertEquals(0, m.size());
    }

    /**
     * Tests foldl for non-empty containers.
     */
    @Test
    public void foldlNonEmptyList() {
        List<Integer> l = Arrays.asList(1, 7, 9);
        assertEquals(179, (int)Collections.foldl((arg1, arg2) -> 10 * arg1 + arg2, 0, l));
    }

    /**
     * Tests foldl for empty containers.
     */
    @Test
    public void foldlEmpty() {
        List<Integer> l = new ArrayList<>();
        assertEquals(0, (int)Collections.foldl((arg1, arg2) -> 10 * arg1 + arg2, 0, l));
    }

    /**
     * Tests foldr for non-empty containers.
     */
    @Test
    public void foldrNonEmptyList() {
        List<Integer> l = Arrays.asList(10, 20, 179);
        assertEquals(".10.20.179", Collections.foldr((arg1, arg2) -> arg2 + "." + arg1.toString(), "", l));
    }

    /**
     * Tests foldr for empty containers.
     */
    @Test
    public void foldrEmpty() {
        List<Integer> l = new ArrayList<>();
        assertEquals("", Collections.foldr((arg1, arg2) -> arg1.toString() + "." + arg2, "", l));
    }
}