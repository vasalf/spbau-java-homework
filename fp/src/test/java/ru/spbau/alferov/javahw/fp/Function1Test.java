package ru.spbau.alferov.javahw.fp;

import org.junit.Test;

import static org.junit.Assert.*;

public class Function1Test {
    /**
     * Basic apply() test. Tests that identity function works.
     */
    @Test
    public void testIdentityFunction() {
        Function1<Integer, Integer> id = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer arg) {
                return arg;
            }
        };

        assertEquals(0, (int)id.apply(0));
        assertEquals(1, (int)id.apply(1));
        assertEquals(2, (int)id.apply(2));
        assertEquals(179, (int)id.apply(179));
    }

    /**
     * Tests that composition works and is applied in the <s>wrong</s> correct way (as in specification).
     */
    @Test
    public void compose() {
        Function1<Integer, Integer> f = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer arg) {
                return arg - 1;
            }
        };
        Function1<Integer, Integer> g = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer arg) {
                return 2 * arg;
            }
        };
        Function1<Integer, Integer> h = f.compose(g);

        assertEquals(0, (int)h.apply(1));
    }

}