package ru.spbau.alferov.javacw.LinkedHashMap;

import java.util.*;

public class LinkedHashMap<K extends Comparable<K>,V> extends AbstractMap<K,V> {

    private class LinkedHashMapEntry implements Entry<K,V> {
        private K key;
        private V value;
        private Iterator<Entry<K,V>> listPosition;
        private boolean yetInMap = true;

        LinkedHashMapEntry(K entryKey, V entryValue) {
            key = entryKey;
            value = entryValue;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V v) {
            V old = value;
            value = v;
            return old;
        }

        public Iterator<Entry<K, V>> getListPosition() {
            return listPosition;
        }

        public void setListPosition(Iterator<Entry<K, V>> listPosition) {
            this.listPosition = listPosition;
        }

        public boolean isYetInMap() {
            return yetInMap;
        }

        public void removeFromMap() {
            yetInMap = false;
        }
    }

    private List<Entry<K, V>>[] storage;

    private LinkedList<Entry<K,V>> inOrder;

    private int size = 0;

    private int capacity;

    private void createStorage() {
        storage = new List[capacity];
        for (int i = 0; i < capacity; i++) {
            storage[i] = new LinkedList<>();
        }
        inOrder = new LinkedList<>();
    }

    public LinkedHashMap() {
        capacity = 16;
        createStorage();
    }

    private int bucketIndex(int hashCode) {
        return ((hashCode % capacity) + capacity) % capacity;
    }

    private void rebuild() {
        if (2 * size >= capacity) {
            List<Entry<K,V>>[] old = storage;
            int oldCapacity = capacity;
            capacity = 2 * capacity;
            createStorage();

            for (int i = 0; i < oldCapacity; i++) {
                for (Entry<K,V> entry : old[i]) {
                    int hashCode = entry.getKey().hashCode();
                    storage[bucketIndex(hashCode)].add(entry);
                }
            }
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        createStorage();
        inOrder.clear();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new AbstractSet<Entry<K, V>>() {
            @Override
            public Iterator<Entry<K, V>> iterator() {
                return new Iterator<Entry<K, V>>() {
                    Iterator<Entry<K, V>> wrap = inOrder.descendingIterator();
                    LinkedHashMapEntry entry = null;

                    private void skipUntilInMap() {
                        if (!wrap.hasNext()) {
                            entry = null;
                            return;
                        }
                        entry = (LinkedHashMapEntry)wrap.next();
                        while (wrap.hasNext() && !entry.isYetInMap()) {
                            entry = (LinkedHashMapEntry)wrap.next();
                        }
                        if (!wrap.hasNext() && (entry == null || !entry.isYetInMap()))
                            entry = null;
                    }

                    @Override
                    public boolean hasNext() {
                        if (entry == null)
                            skipUntilInMap();
                        return entry != null;
                    }

                    @Override
                    public Entry<K, V> next() {
                        if (entry == null)
                            skipUntilInMap();
                        Entry<K,V> ret = entry;
                        skipUntilInMap();
                        return ret;
                    }
                };
            }

            @Override
            public int size() {
                return inOrder.size();
            }
        };
    }

    private Iterator<Entry<K, V>> findKeyIter(int bucket, K key) {
        Iterator<Entry<K,V>> it = storage[bucket].iterator();
        while (it.hasNext()) {
            Entry<K,V> entry = it.next();
            if (entry.getKey().compareTo(key) == 0) {
                return it;
            }
        }
        return null;
    }

    private Entry<K,V> findEntry(int bucket, K key) {
        for (Entry<K,V> entry : storage[bucket]) {
            if (entry.getKey().compareTo(key) == 0) {
                return entry;
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        Entry<K,V> entry = findEntry(bucketIndex(key.hashCode()), key);
        if (entry != null) {
            entry.setValue(value);
            rebuild();
            return entry.getValue();
        } else {
            LinkedHashMapEntry newEntry = new LinkedHashMapEntry(key, value);
            inOrder.addFirst(newEntry);
            newEntry.setListPosition(inOrder.iterator());
            storage[bucketIndex(key.hashCode())].add(newEntry);
            rebuild();
            return null;
        }
    }

    @Override
    public V get(Object obj) {
        K key = (K)obj;
        Entry<K,V> entry = findEntry(bucketIndex(key.hashCode()), key);
        if (entry != null)
            return entry.getValue();
        else
            return null;
    }

    @Override
    public V remove(Object obj) {
        K key = (K)obj;
        LinkedHashMapEntry entry = (LinkedHashMapEntry)findEntry(bucketIndex(key.hashCode()), key);
        if (entry == null)
            return null;
        entry.removeFromMap();
        Iterator<Entry<K,V>> iterator = findKeyIter(bucketIndex(key.hashCode()), key);
        iterator.remove();
        return entry.getValue();
    }
}
