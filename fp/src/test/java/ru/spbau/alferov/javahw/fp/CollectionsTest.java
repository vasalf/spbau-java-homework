package ru.spbau.alferov.javahw.fp;

import org.junit.Test;

import java.util.ArrayList;
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
    public void map() {
        List<Integer> l = new ArrayList<>();
        l.add(1); l.add(2); l.add(179);
        List<Integer> m = Collections.map(new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer arg) {
                return (arg - 1) / 2;
            }
        }, l);
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
        List<Integer> m = Collections.map(new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer arg) {
                return null;
            }
        }, new ArrayList<>());
        assertEquals(0, m.size());
    }

    /**
     * Tests filtering for non-empty containers.
     */
    @Test
    public void filter() {
        List<Integer> l = new ArrayList<>();
        l.add(1); l.add(10); l.add(5); l.add(110);
        List<Integer> m = Collections.filter(new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg < 7;
            }
        }, l);
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
        List<Integer> m = Collections.filter(new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return null;
            }
        }, new ArrayList<>());
        assertEquals(0, m.size());
    }

    /**
     * Tests takeWhile for empty and non-empty result.
     */
    @Test
    public void takeWhile() {
        List<Integer> l = new ArrayList<>();
        l.add(1); l.add(10); l.add(5); l.add(110);
        List<Integer> m = Collections.takeWhile(new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg < 7;
            }
        }, l);
        int exp[] = {1};
        assertEquals(exp.length, m.size());
        int i = 0;
        for (int t : m) {
            assertEquals(exp[i++], t);
        }

        List<Integer> o = Collections.takeWhile(new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return false;
            }
        }, l);
        assertEquals(0, o.size());
    }

    /**
     * Tests takeWhile for empty containers.
     */
    @Test
    public void takeWhileEmpty() {
        List<Integer> m = Collections.takeWhile(new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return null;
            }
        }, new ArrayList<>());
        assertEquals(0, m.size());
    }

    /**
     * Tests takeUnless for empty and non-empty result.
     */
    @Test
    public void takeUnless() {
        List<Integer> l = new ArrayList<>();
        l.add(1); l.add(10); l.add(5); l.add(110);
        List<Integer> m = Collections.takeUnless(new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg >= 7;
            }
        }, l);
        int exp[] = {1};
        assertEquals(exp.length, m.size());
        int i = 0;
        for (int t : m) {
            assertEquals(exp[i++], t);
        }

        List<Integer> o = Collections.takeUnless(new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return true;
            }
        }, l);
        assertEquals(0, o.size());
    }

    /**
     * Tests takeUnless for empty containers.
     */
    @Test
    public void takeUnlessEmpty() {
        List<Integer> m = Collections.takeUnless(new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return null;
            }
        }, new ArrayList<>());
        assertEquals(0, m.size());
    }

    /**
     * Tests foldl for non-empty containers.
     */
    @Test
    public void foldl() {
        List<Integer> l = new ArrayList<>();
        l.add(1); l.add(7); l.add(9);
        assertEquals(179, (int)Collections.foldl(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return 10 * arg1 + arg2;
            }
        }, 0, l));
    }

    /**
     * Tests foldl for empty containers.
     */
    @Test
    public void foldlEmpty() {
        List<Integer> l = new ArrayList<>();
        assertEquals(0, (int)Collections.foldl(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return 10 * arg1 + arg2;
            }
        }, 0, l));
    }

    /**
     * Tests foldr for non-empty containers.
     */
    @Test
    public void foldr() {
        List<Integer> l = new ArrayList<>();
        l.add(10); l.add(20); l.add(179);
        assertEquals(".10.20.179", Collections.foldr(new Function2<Integer, String, String>() {
            @Override
            public String apply(Integer arg1, String arg2) {
                return arg2 + "." + arg1.toString();
            }
        }, "", l));
    }

    /**
     * Tests foldr for empty containers.
     */
    @Test
    public void foldrEmpty() {
        List<Integer> l = new ArrayList<>();
        assertEquals("", Collections.foldr(new Function2<Integer, String, String>() {
            @Override
            public String apply(Integer arg1, String arg2) {
                return arg1.toString() + "." + arg2;
            }
        }, "", l));
    }
}