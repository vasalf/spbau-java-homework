package ru.spbau.alferov.javahw.trie;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class TrieTest {
    /**
     * Checks that construction does not fail.
     */
    @Test
    public void construct() {
        new Trie();
    }

    /**
     * Checks behaviour of the returned value of add.
     */
    @Test
    public void addReturnValue() {
        Trie tr = new Trie();
        assertFalse(tr.add("abcd"));
        assertFalse(tr.add("efgh"));
        assertFalse(tr.add(""));
        assertFalse(tr.add("abgh"));
        assertTrue(tr.add("abcd"));
    }

    /**
     * Checks the contains() function.
     */
    @Test
    public void containsBehaviour() {
        Trie tr = new Trie();
        tr.add("abcd");
        tr.add("abcd");
        tr.add("efghi");
        assertTrue(tr.contains("abcd"));
        assertTrue(tr.contains("efghi"));
        assertFalse(tr.contains(""));
        assertFalse(tr.contains("abcde"));
    }

    /**
     * Checks the remove() function behaviour and returned value.
     */
    @Test
    public void removeReturnValue() {
        Trie tr = new Trie();
        tr.add("abcd");
        tr.add("abcd");
        tr.add("efgh");
        assertTrue(tr.contains("abcd"));
        assertTrue(tr.contains("efgh"));
        assertTrue(tr.remove("abcd"));
        assertFalse(tr.contains("abcd"));
        assertTrue(tr.contains("efgh"));
        assertFalse(tr.add("abcd"));
        assertTrue(tr.contains("abcd"));
    }

    /**
     * Checks that the remove() function works correctly being asked
     * to remove a non-existent element.
     */
    @Test
    public void removeNonExistent() {
        Trie tr = new Trie();
        assertFalse(tr.remove("abcd"));
        tr.add("abcd");
        assertTrue(tr.remove("abcd"));
        assertFalse(tr.remove("abcd"));
    }

    /**
     * Checks the size() function behaviour.
     */
    @Test
    public void sizeChanges() {
        Trie tr = new Trie();
        assertEquals(0, tr.size());
        tr.add("abcd");
        tr.add("efgh");
        assertEquals(2, tr.size());
        tr.add("abcd");
        assertEquals(2, tr.size());
        tr.remove("abcd");
        assertEquals(1, tr.size());
    }

    /**
     * Checks the howManyStartsWithPrefix() function.
     */
    @Test
    public void howManyStartsWithPrefix() {
        Trie tr = new Trie();
        tr.add("abcde");
        tr.add("abcd");
        tr.add("abc");
        tr.add("ab");
        tr.add("a");
        tr.add("");
        tr.add("abdef");
        tr.add("abcf");
        assertEquals(0, tr.howManyStartsWithPrefix("b"));
        assertEquals(1, tr.howManyStartsWithPrefix("abcde"));
        assertEquals(2, tr.howManyStartsWithPrefix("abcd"));
        assertEquals(4, tr.howManyStartsWithPrefix("abc"));
        assertEquals(6, tr.howManyStartsWithPrefix("ab"));
        assertEquals(7, tr.howManyStartsWithPrefix("a"));
        assertEquals(8, tr.howManyStartsWithPrefix(""));
        tr.remove("abcf");
        assertEquals(0, tr.howManyStartsWithPrefix("b"));
        assertEquals(1, tr.howManyStartsWithPrefix("abcde"));
        assertEquals(2, tr.howManyStartsWithPrefix("abcd"));
        assertEquals(3, tr.howManyStartsWithPrefix("abc"));
        assertEquals(5, tr.howManyStartsWithPrefix("ab"));
        assertEquals(6, tr.howManyStartsWithPrefix("a"));
        assertEquals(7, tr.howManyStartsWithPrefix(""));
    }

    /**
     * Checks the behaviour of serialize() function: constructs a
     * Trie, seralizes and deserializes it and checks the results
     * on equality.
     */
    @Test
    public void serializeAndDeserialize() throws IOException, ClassNotFoundException {
        String put[] = {"abcd", "efgh", "ijkl", "abdf", "a", ""};
        Trie tr = new Trie();
        for (String s : put) {
            tr.add(s);
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        tr.serialize(outStream);
        byte[] data = outStream.toByteArray();

        ByteArrayInputStream inStream = new ByteArrayInputStream(data);
        Trie dsr = new Trie();
        dsr.deserialize(inStream);

        assertEquals(put.length, dsr.size());
        for (String s : put) {
            assertTrue(dsr.contains(s));
        }
    }
}