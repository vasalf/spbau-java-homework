package ru.spbau.alferov.javahw.fp;

import org.junit.Test;

import static org.junit.Assert.*;

public class Function2Test {
    /**
     * Just tests that apply() for sum works.
     */
    @Test
    public void sum() {
        Function2<Integer, Integer, Integer> f = (arg1, arg2) -> arg1 + arg2;

        assertEquals(42, (int)f.apply(0, 42));
        assertEquals(179, (int)f.apply(100, 79));
    }

    /**
     * Tests that composition works in the <s>wrong</s>correct (specified) way.
     */
    @Test
    public void compose() {
        Function2<String, Integer, Integer> f = (arg1, arg2) -> arg1.length() * arg2;
        Function1<Integer, String> toStr = arg -> arg.toString();
        Function2<String, Integer, String> h = f.compose(toStr);

        assertEquals("20", h.apply("abcd", 5));
    }

    /**
     * Tests that bindings (and curry) work in the specified way.
     */
    @Test
    public void bind() {
        Function2<String, Integer, Integer> f = (arg1, arg2) -> arg1.length() * arg2;
        Function1<String, Integer> doubledLength = f.bind2(2);
        Function1<String, Integer> doubledLength2 = f.curry(2);
        Function1<Integer, Integer> mul4 = f.bind1("qwer");

        assertEquals(10, (int)doubledLength.apply("abcde"));
        assertEquals(10, (int)doubledLength2.apply("qwert"));
        assertEquals(92, (int)mul4.apply(23));
    }
}