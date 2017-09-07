package ru.spbau.alferov.javahw.hashtable;

/**
 * The List class provides an implementation of singly linked list.
 * The Hashtable class uses it to store the elements in one bucket.
 *
 * @author Vasily Alferov
 */
public class List {
    /**
     * The Node class stores one Object and a pointer to the next
     * Node in the List.
     * As long as all of the fields can be modified/accessed by List
     * methods, all of them are public.
     */
    private class Node {
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
    public class Iterator {
        /**
         * The current Node.
         */
        public Node current;

        /**
         * The previous Node (used by List.erase).
         */
        public Node previous = null;

        /**
         * Basic constructor. Takes a Node supposing it is the head
         * of the List and creates an Iterator pointing at it.
         *
         * @param head The head of the List
         */
        Iterator(Node head) {
            current = head;
        }

        /**
         * Get the object stored by current Node.
         */
        public Object get() {
            return current.stored;
        }

        /**
         * Advance one position further.
         */
        public void advance() {
            previous = current;
            current = current.next;
        }

        /**
         * Check whether we are pointing at the past-end element.
         */
        public boolean isEnd() {
            return current == null;
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
     * Get the Iterator pointing at the List head.
     */
    public Iterator listHead() {
        return new Iterator(head);
    }

    /**
     * Erase an element by an Iterator.
     *
     * @param which An iterator pointing at the element that will be
     *              erased.
     */
    public void erase(Iterator which) {
        if (which.previous == null) {
            /* We are erasing head of the List */
            head = head.next;
        } else {
            /* We are erasing some element from middle of the List */
            which.previous.next = which.current.next;
        }
        size--;
    }

    /**
     * Get size of the List.
     */
    public int getSize() {
        return size;
    }
}
