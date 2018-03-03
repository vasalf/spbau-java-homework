package ru.spbau.alferov.javahw.hashtable;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class HashtableTest {
    /**
     * Tests that construction does not fail.
     */
    @Test
    public void construction() {
        Hashtable h = new Hashtable();
    }

    /**
     * Tests that putting values into Hashtable does not fail.
     */
    @Test
    public void put() {
        Hashtable h = new Hashtable();
        for (int i = 0; i < 179; i++) {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(i);
            String key = stringBuilder.toString();
            stringBuilder.setLength(0);

            stringBuilder.append(179 - i);
            String value = stringBuilder.toString();

            h.put(key, value);
        }
    }

    /**
     * Tests whether size is calculated correctly. Literally, it
     * checks that:
     * <ol>
     * <li> Size of an empty Hashtable is zero.</li>
     * <li> When a new element is inserted, size is increased by 1.</li>
     * <li> When an existing value is changed, the size is not.</li>
     * </ol>
     */
    @Test
    public void size() {
        Hashtable h = new Hashtable();
        assertEquals(0, h.size());
        h.put("a", "b");
        assertEquals(1, h.size());
        h.put("a", "c");
        assertEquals(1, h.size());
    }

    /**
     * Tests contains() function.
     * Checks whether contained keys are contained and non-contained
     * elements are not contained.
     */
    @Test
    public void contains() {
        Hashtable h = new Hashtable();
        h.put("a", "b");
        assertTrue(h.contains("a"));
        assertFalse(h.contains("b"));
    }

    /**
     * Tests that stored values are stored correctly.
     */
    @Test
    public void get() {
        Hashtable h = new Hashtable();
        for (int i = 0; i < 179; i++) {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(i);
            String key = stringBuilder.toString();
            stringBuilder.setLength(0);

            stringBuilder.append(179 - i);
            String value = stringBuilder.toString();

            h.put(key, value);
        }
        for (int i = 0; i < 179; i++) {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(i);
            String key = stringBuilder.toString();
            stringBuilder.setLength(0);

            stringBuilder.append(179 - i);
            String value = stringBuilder.toString();

            assertEquals(value, h.get(key));
        }
    }

    /**
     * Tests that values are overwritten correctly.
     */
    @Test
    public void getChanged() {
        Hashtable h = new Hashtable();
        h.put("a", "b");
        h.put("a", "c");
        assertEquals("c", h.get("a"));
    }

    /**
     * Tests that get(key) returns null on non-contained element.
     */
    @Test
    public void getNotContained() {
        Hashtable h = new Hashtable();
        h.put("a", "b");
        assertFalse(h.contains("b"));
        assertEquals(null, h.get("b"));
    }

    /**
     * Tests that removed values are no longer contained.
     */
    @Test
    public void remove() {
        Hashtable h = new Hashtable();
        h.put("a", "b");
        assertEquals("b", h.remove("a"));
        assertEquals(0, h.size());
        assertFalse(h.contains("a"));
    }

    /**
     * Tests that remove(key) returns null when removing a non-contained
     * element.
     */
    @Test
    public void removeNotContained() {
        Hashtable h = new Hashtable();
        h.put("a", "b");
        assertFalse(h.contains("b"));
        assertEquals(null, h.remove("b"));

    }

    /**
     * Tests that size of a clear hashtable is zero.
     */
    @Test
    public void clear() {
        Hashtable h = new Hashtable();
        for (int i = 0; i < 179; i++) {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(i);
            String key = stringBuilder.toString();
            stringBuilder.setLength(0);

            stringBuilder.append(179 - i);
            String value = stringBuilder.toString();

            h.put(key, value);
        }
        h.clear();
        assertEquals(0, h.size());
    }

    /**
     * Tests the Hashtable performance when there are collisions.
     */
    @Test
    public void checkCollisions() {
        String a = "rbgwbpkyjx";
        String b = "porcixytiy";
        assertEquals(a.hashCode(), b.hashCode());
        Hashtable h = new Hashtable();
        h.put(a, b);
        h.put(b, a);
        assertEquals(b, h.get(a));
        assertEquals(a, h.get(b));
        assertEquals(b, h.remove(a));
        assertEquals(a, h.get(b));
    }
}