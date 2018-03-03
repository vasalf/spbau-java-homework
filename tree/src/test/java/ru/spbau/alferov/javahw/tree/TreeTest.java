package ru.spbau.alferov.javahw.tree;

import org.junit.Test;

import static org.junit.Assert.*;

public class TreeTest {

    /**
     * Tests that add() and contains() functions work as
     * expected for Tree&lt;Integer&gt; and Tree&lt;String&gt;.
     */
    @Test
    public void contains() throws Exception {
        Tree<Integer> t = new Tree<>();
        t.add(1);
        assertTrue(t.contains(1));
        assertFalse(t.contains(2));

        Tree<String> s = new Tree<>();
        s.add("a179b");
        assertTrue(s.contains("a179b"));
        assertFalse(s.contains("a179"));
        assertFalse(s.contains(""));
    }

    /**
     * Tests that size of empty tree is zero, size increases after
     * adding a new element and size doesn't change after adding an
     * existing element.
     */
    @Test
    public void size() throws Exception {
        Tree<Integer> t = new Tree<>();
        assertEquals(0, t.size());
        t.add(1);
        assertEquals(1, t.size());
        t.add(1);
        assertEquals(1, t.size());
    }

    /**
     * Constructs a large (size = 2 * 179) tree and checks its
     * behaviour for correctness.
     */
    @Test
    public void largeTree() throws Exception {
        Tree<Integer> t = new Tree<>();
        for (int i = -179; i < 179; i++) {
            t.add(i);
        }
        assertEquals(179 * 2, t.size());
        for (int i = -200; i < 200; i++) {
            assertEquals(i >= -179 && i < 179, t.contains(i));
        }
    }
}