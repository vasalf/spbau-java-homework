package ru.spbau.alferov.javahw.hashtable;

import java.security.Key;
import java.util.Iterator;

/**
 * The Hashtable class provides an implementation of a hash-table
 * with both keys and values of type String. Separate chaining is
 * used to resolve collisions. It means that a List of values is
 * stored for each individual hash.
 *
 * @author Vasily Alferov
 */
public class Hashtable {
    /**
     * The Cell class stores a single pair of key and value.
     */
    private static class Cell {
        /**
         * The key stored in the Cell. As long as it should not be
         * modified, the access is private.
         */
        private String key;

        /**
         * The key's hash. It is computed in the constructor.
         */
        private int keyHash;

        /**
         * The value stored in the Cell. As long as it is mutable,
         * the access is public.
         */
        public String value;

        /**
         * Basic constructor from key and value.
         */
        public Cell(String elemKey, String elemValue) {
            key = elemKey;
            keyHash = key.hashCode();
            value = elemValue;
        }

        /**
         * Get the key stored in the Cell.
         */
        public String getKey() {
            return key;
        }

        /**
         * Get the key's hash.
         */
        public int getKeyHash() {
            return keyHash;
        }
    }

    /**
     * Current number of stored Cells.
     */
    private int currentSize = 0;
    /**
     * Current number of buckets.
     */
    private int buckets = 2;
    /**
     * The storage.
     */
    private List[] storage;

    /**
     * Constructs an empty Hashtable.
     */
    public Hashtable() {
        storage = new List[buckets];
        for (int i = 0; i < buckets; i++) {
            storage[i] = new List();
        }
    }

    /**
     * Get the index of buckets in which an element with given hash
     * should be stored.
     *
     * @param buckets Total number of buckets in the Hashtable.
     */
    private static int bucketIndex(int hash, int buckets) {
        /* Hashcode may be negative, so we must return
         * the mathematical modulo. */
        return ((hash % buckets) + buckets) % buckets;
    }

    /**
     * Checks out the load factor and rebuilds the Hashtable if
     * necessary. Literally, it doubles the number of buckets in
     * case (size / buckets) >= 1/2.
     */
    private void rebuild() {
        if (2 * currentSize >= buckets) {
            int newBuckets = buckets;
            List[] newStorage = new List[newBuckets];
            for (int i = 0; i < newBuckets; i++) {
                newStorage[i] = new List();
            }
            for (int i = 0; i < buckets; i++) {
                for (Object obj : storage[i]) {
                    Cell c = (Cell)obj;
                    newStorage[Hashtable.bucketIndex(c.getKeyHash(), newBuckets)].insert(c);
                }
            }
            buckets = newBuckets;
            storage = newStorage;
        }
    }

    /**
     * Get number of keys in the table.
     */
    public int size() {
        return currentSize;
    }

    /**
     * Get an iterator _after_ the Cell with given Key in the bucket
     * or null if there is no such Key.
     */
    private Iterator<Object> findKeyIter(int bucket, String key) {
        Iterator<Object> it = storage[bucket].iterator();
        while (it.hasNext()) {
            Cell c = (Cell)it.next();
            if (c.getKey().compareTo(key) == 0) {
                return it;
            }
        }
        return null;
    }

    /** Get a Cell with given Key in the bucket or null of there is
     * no such Key.
     */
    private Cell findKey(int bucket, String key) {
        for (Object obj : storage[bucket]) {
            Cell c = (Cell)obj;
            if (c.getKey().compareTo(key) == 0)
                return c;
        }
        return null;
    }

    /**
     * Checks if there exists a Cell with a given key.
     */
    public boolean contains(String key) {
        int bucket = Hashtable.bucketIndex(key.hashCode(), buckets);
        return findKey(bucket, key) != null;
    }

    /**
     * Get a value from a Cell with given Key or null if there is no
     * such Key
     */
    public String get(String key) {
        int bucket = Hashtable.bucketIndex(key.hashCode(), buckets);
        Cell c = findKey(bucket, key);
        if (c == null) {
            return null;
        } else {
            return c.value;
        }
    }

    /**
     * Sets value for a given Key. Creates a new Cell, if necessary.
     * @return Old value for a given Key or null if there was no Cell
     *         with given Key.
     */
    public String put(String key, String value) {
        int bucket = Hashtable.bucketIndex(key.hashCode(), buckets);
        Cell cur = findKey(bucket, key);
        String ret;
        if (cur == null) {
            Cell c = new Cell(key, value);
            storage[bucket].insert(c);
            currentSize++;
            ret = null;
        } else {
            ret = cur.value;
            cur.value = value;
        }
        rebuild();
        return ret;
    }

    /**
     * Removes a Cell with given Key.
     * @return Value stored in the deleted Cell or null if there was
     *         no Cell with given Key.
     */
    public String remove(String key) {
        int bucket = Hashtable.bucketIndex(key.hashCode(), buckets);
        Cell cur = findKey(bucket, key);
        Iterator<Object> it = findKeyIter(bucket, key);
        if (it == null)
            return null;
        String ret = cur.value;
        it.remove();
        currentSize--;
        return ret;
    }

    /**
     * Clears the table.
     */
    public void clear() {
        currentSize = 0;
        buckets = 2;
        storage = new List[buckets];
        for (int i = 0; i < buckets; i++)
            storage[i] = new List();
    }
}
