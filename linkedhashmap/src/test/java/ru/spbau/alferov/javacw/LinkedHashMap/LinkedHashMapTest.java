package ru.spbau.alferov.javacw.LinkedHashMap;

import org.junit.Test;

import static org.junit.Assert.*;

public class LinkedHashMapTest {
    @Test
    public void construct() {
        LinkedHashMap<String, String> lhm = new LinkedHashMap<>();
    }

    @Test
    public void put() {
        LinkedHashMap<String, Integer> lhm = new LinkedHashMap<>();
        assertNull(lhm.put("abc", 1));
        assertEquals(1, (int)lhm.put("abc", 2));
        assertNull(lhm.put("def", 3));
    }

    @Test
    public void get() {
        LinkedHashMap<Integer, String> lhm = new LinkedHashMap<>();
        assertNull(lhm.get(1));
        lhm.put(1, "abc");
        assertEquals("abc", lhm.get(1));
    }

    @Test
    public void remove() {
        LinkedHashMap<Integer, String> lhm = new LinkedHashMap<>();
        assertNull(lhm.get(1));
        lhm.put(1, "abc");
        assertEquals("abc", lhm.get(1));
        lhm.remove(1);
        assertNull(lhm.get(1));
    }

    @Test
    public void size() {
        LinkedHashMap<Integer, String> lhm = new LinkedHashMap<>();
        assertEquals(0,lhm.size());
        lhm.put(1, "abc");
        assertEquals(1, lhm.size());
        lhm.remove(1);
        assertEquals(0, lhm.size());
    }
}