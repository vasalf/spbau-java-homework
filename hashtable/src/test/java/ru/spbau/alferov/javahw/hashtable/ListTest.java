package ru.spbau.alferov.javahw.hashtable;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.*;

public class ListTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * Tests that consequent inserts into List do not fail.
     */
    @Test
    public void insert() {
        List l = new List();
        for (int i = 0; i < 179; i++) {
            l.insert(i);
        }
    }

    /**
     * Checks that size of List is calculated correctly.
     */
    @Test
    public void getSize() {
        List l = new List();
        for (int i = 0; i < 179; i++) {
            l.insert(i);
        }
        assertEquals(179, l.getSize());
    }

    /**
     * Tests that a List from insert test is created correctly.
     * Order of elements in List is not documented, so this function
     * checks that the set of elements stored in the List is correct.
     */
    @Test
    public void iterator() {
        List l = new List();
        int[] expected = new int[179];
        for (int i = 0; i < 179; i++) {
            l.insert(i);
            expected[i] = i;
        }
        int setInList[] = new int[179];
        int cur = 0;
        for (Object obj : l) {
            int u = (int)obj;
            setInList[cur++] = u;
        }
        Arrays.sort(setInList);
        assertArrayEquals(expected, setInList);
    }

    private static Iterator<Object> findEvenElement(List l) {
        Iterator<Object> it = l.iterator();
        while (it.hasNext()) {
            int x = (int)it.next();
            if (x % 2 == 0)
                return it;
        }
        return null;
    }

    /**
     * Tests the remove function by an iterator.
     * As in iterator test, only the stored set is checked up.
     */
    @Test
    public void remove() {
        List l = new List();
        int[] expected = new int[90];
        for (int i = 1; i <= 179; i++) {
            l.insert(i);
            if (i % 2 == 1)
                expected[i / 2] = i;
        }

        Iterator<Object> rem = findEvenElement(l);
        while (rem != null) {
            rem.remove();
            rem = findEvenElement(l);
        }

        assertEquals(90,l.getSize());

        int setInList[] = new int[90];
        int cur = 0;
        for (Object obj : l) {
            int u = (int)obj;
            setInList[cur++] = u;
        }
        Arrays.sort(setInList);
        assertArrayEquals(expected, setInList);
    }

    /**
     * Tests the list head removal
     */
    @Test
    public void removeHead() {
        List l = new List();
        l.insert(2);
        l.insert(3);
        Iterator<Object> it = l.iterator();
        it.next();
        it.remove();
        it = l.iterator();
        int x = (int)it.next();
        assertEquals(1, l.getSize());
        assertTrue(x == 2 || x == 3);
    }

    /**
     * Tests the IllegalStateException on removing by the pre-begin
     * iterator.
     */
    @Test
    public void removePreHead() {
        exception.expect(IllegalStateException.class);
        List l = new List();
        l.insert(0);
        Iterator<Object> it = l.iterator();
        it.remove();
    }
}