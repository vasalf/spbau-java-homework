package ru.spbau.alferov.javahw.hashtable;

import java.util.Iterator;

/**
 * The List class provides an implementation of singly linked list.
 * The Hashtable class uses it to store the elements in one bucket.
 *
 * @author Vasily Alferov
 */
public class List implements Iterable<Object> {
    /**
     * The Node class stores one Object and a pointer to the next
     * Node in the List.
     * As long as all of the fields can be modified/accessed by List
     * methods, all of them are public.
     */
    private static class Node {
        /**
         * An Object stored in the Node.
         */
        public Object stored;

        /**
         * The next Node in the list (null if current is the last).
         */
        public Node next = null;

        /**
         * Basic constructor.
         *
         * @param toStore Object to be stored in this Node.
         */
        public Node(Object toStore) {
            stored = toStore;
        }
    }

    /**
     * Class used to iterate through the List.
     * <p>
     * All of the iterators are invalidated after any insert/erase
     * operation on the List.
     * <p>
     * As long as all of the fields should be accessible from List
     * class, all of them are public.
     */
    private class ListIterator implements Iterator<Object> {
        /**
         * The current Node.
         */
        private Node current;

        /**
         * The previous Node (which we should remove when the remove
         * method is called).
         */
        private Node previous = null;

        /**
         * As long as List is singly connected, we should store two
         * last elements to remove from List correctly.
         */
        private Node previousOfPrevious = null;

        /**
         * Basic constructor. Creates an iterator pointing to the
         * head of parent List.
         */
        public ListIterator() {
            current = List.this.head;
        }

        /**
         * Advance one position further.
         *
         * @return Element in position before the advance.
         */
        public Object next() {
            Object ret = current.stored;
            previousOfPrevious = previous;
            previous = current;
            current = current.next;
            return ret;
        }

        /**
         * Check whether there is a place to advance.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Remove the previous element.
         */
        public void remove() {
            if (previous == null)
                throw new IllegalStateException();
            if (previousOfPrevious == null) {
            /* We are erasing head of the List */
                List.this.head = previous;
            } else {
            /* We are erasing some element from middle of the List */
                previousOfPrevious.next = current;
            }
            List.this.size--;
        }
    }

    /**
     * The head of the List (null if List is empty).
     */
    private Node head = null;
    /**
     * Current size of the List.
     */
    private int size = 0;

    /**
     * Constructs an empty List.
     */
    public List() {
    }

    /**
     * Insert an element in head of the List.
     *
     * @param elem An element to be inserted.
     */
    public void insert(Object elem) {
        Node newHead = new Node(elem);
        newHead.next = head;
        head = newHead;
        size++;
    }

    /**
     * Get the Iterator (List implements Iterable interface)
     */
    public Iterator<Object> iterator() {
        return new ListIterator();
    }

    /**
     * Get size of the List.
     */
    public int getSize() {
        return size;
    }
}
