package ru.spbau.alferov.javahw.hashtable;

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
    private class Cell {
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
        Cell(String key_, String value_) {
            key = key_;
            keyHash = key.hashCode();
            value = value_;
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
        for (int i = 0; i < buckets; i++)
            storage[i] = new List();
    }

    /**
     * Checks out the load factor and rebuilds the Hashtable if
     * necessary. Literally, it doubles the number of buckets in
     * case (size / buckets) >= 1/2.
     */
    private void rebuild() {
        if (2 * currentSize >= buckets) {
            int new_buckets = buckets;
            List[] new_storage = new List[new_buckets];
            for (int i = 0; i < new_buckets; i++)
                new_storage[i] = new List();
            for (int i = 0; i < buckets; i++) {
                for (List.Iterator it = storage[i].listHead(); !(it.isEnd()); it.advance()) {
                    Cell c = (Cell)it.get();
                    new_storage[c.getKeyHash() % new_buckets].insert(c);
                }
            }
            buckets = new_buckets;
            storage = new_storage;
        }
    }

    /**
     * Get number of keys in the table.
     */
    public int size() {
        return currentSize;
    }

    /**
     * Get an iterator on a Cell with given Key in the bucket or null
     * if there is no such Key.
     */
    private List.Iterator findKey(int bucket, String key) {
        for (List.Iterator it = storage[bucket].listHead(); !(it.isEnd()); it.advance()) {
            Cell c = (Cell)it.get();
            if (c.getKey().compareTo(key) == 0)
                return it;
        }
        return null;
    }

    /**
     * Checks if there exists a Cell with a given key.
     */
    public boolean contains(String key) {
        int bucket = key.hashCode() % buckets;
        return findKey(bucket, key) != null;
    }

    /**
     * Get a value from a Cell with given Key or null if there is no
     * such Key
     */
    public String get(String key) {
        int bucket = key.hashCode() % buckets;
        List.Iterator it = findKey(bucket, key);
        if (it == null)
            return null;
        else {
            Cell c = (Cell)it.get();
            return c.value;
        }
    }

    /**
     * Sets value for a given Key. Creates a new Cell, if necessary.
     * @return Old value for a given Key or null if there was no Cell
     *         with given Key.
     */
    public String put(String key, String value) {
        int bucket = key.hashCode() % buckets;
        List.Iterator it = findKey(bucket, key);
        String ret;
        if (it == null) {
            Cell c = new Cell(key, value);
            storage[bucket].insert(c);
            currentSize++;
            ret = null;
        } else {
            Cell c = (Cell)it.get();
            ret = c.value;
            c.value = value;
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
        int bucket = key.hashCode() % buckets;
        List.Iterator it = findKey(bucket, key);
        if (it == null)
            return null;
        Cell c = (Cell)it.get();
        String ret = c.value;
        storage[bucket].erase(it);
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
