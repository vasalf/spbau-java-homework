package ru.spbau.alferov.javahw.trie;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class TrieTest {
    @Test
    public void construct() {
        Trie tr = new Trie();
    }

    @Test
    public void add() {
        Trie tr = new Trie();
        assertEquals(false, tr.add("abcd"));
        assertEquals(false, tr.add("efgh"));
        assertEquals(false, tr.add(""));
        assertEquals(false, tr.add("abgh"));
        assertEquals(true, tr.add("abcd"));
    }

    @Test
    public void contains() {
        Trie tr = new Trie();
        tr.add("abcd");
        tr.add("abcd");
        tr.add("efghi");
        assertEquals(true, tr.contains("abcd"));
        assertEquals(true, tr.contains("efghi"));
        assertEquals(false, tr.contains(""));
        assertEquals(false, tr.contains("abcde"));
    }

    @Test
    public void remove() {
        Trie tr = new Trie();
        tr.add("abcd");
        tr.add("abcd");
        tr.add("efgh");
        assertEquals(true, tr.contains("abcd"));
        assertEquals(true, tr.contains("efgh"));
        assertEquals(true, tr.remove("abcd"));
        assertEquals(false, tr.contains("abcd"));
        assertEquals(true, tr.contains("efgh"));
        assertEquals(false, tr.add("abcd"));
        assertEquals(true, tr.contains("abcd"));
    }

    @Test
    public void removeNonExistent() {
        Trie tr = new Trie();
        assertEquals(false, tr.remove("abcd"));
        tr.add("abcd");
        assertEquals(true, tr.remove("abcd"));
        assertEquals(false, tr.remove("abcd"));
    }

    @Test
    public void size() {
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

    @Test
    public void serialize() throws IOException, ClassNotFoundException {
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