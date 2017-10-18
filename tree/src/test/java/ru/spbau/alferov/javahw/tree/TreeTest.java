package ru.spbau.alferov.javahw.tree;

import org.junit.Test;

import static org.junit.Assert.*;

public class TreeTest {
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


    @Test
    public void size() throws Exception {
        Tree<Integer> t = new Tree<>();
        assertEquals(0, t.size());
        t.add(1);
        assertEquals(1, t.size());
    }

}