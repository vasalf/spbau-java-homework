package ru.spbau.alferov.javahw.trie;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class implements a set of Strings. Strings can be added,
 * found and removed. Additionally, given a prefix, it can determine
 * the number of Strings in the Trie which have exactly this prefix.
 *
 * Each String is stored only once.
 */
public class Trie implements Serializable {
    /**
     * A class for nodes of the trie, required by Java code conventions
     * to make Java code even slower.
     */
    private static class Node implements Serializable {
        /**
         * A hashmap of ancestor of node.
         * Namely, v = next.get(c) if and only if there is an edge u->v
         * by the letter c.
         */
        @NotNull
        private Map<Character, Node> next = new HashMap<>();

        /**
         * Is node terminal?
         */
        private boolean terminal = false;

        /**
         * Number of terminal ancestors of the node.
         */
        private int numAncestors = 0;

        /**
         * Increase number of ancestors by some value.
         * This function is called when a new string is
         * added or an old string is removed.
         */
        public void increaseNumAncestors(int a) {
            numAncestors += a;
        }


        /**
         * Get the number of terminal ancestors of the node.
         */
        public int getNumAncestors() {
            return numAncestors;
        }

        /**
         * Set the terminal flag.
         */
        public void setTerminal(boolean isTerminal) {
            terminal = isTerminal;
        }

        /**
         * Get the next node by character on edge or return
         * Optional.empty if there is no edge.
         */
        @NotNull
        public Optional<Node> getNextNode(char c) {
            if (next.containsKey(c)) {
                return Optional.of(next.get(c));
            } else {
                return Optional.empty();
            }
        }

        /**
         * Get the next node by character on edge or create a
         * new one, if there is no edge.
         * The new node will be created as new Node()
         */
        @NotNull
        public Node getOrCreateNextNode(char c) {
            if (!next.containsKey(c)) {
                next.put(c, new Node());
            }
            return next.get(c);
        }

        /**
         * Getter for terminal.
         */
        public boolean isTerminal() {
            return terminal;
        }

        /**
         * Serialize an object into outputstream.
         */
        public void serialize(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.writeObject(terminal);
            objectOutputStream.writeObject(numAncestors);
            objectOutputStream.writeObject(next.size());
            for (Map.Entry<Character, Node> entry : next.entrySet()) {
                objectOutputStream.writeObject(entry.getKey());
                entry.getValue().serialize(objectOutputStream);
            }
        }

        /**
         * Deserialize object from inputstream.
         */
        public void deserialize(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            this.terminal = (Boolean)objectInputStream.readObject();
            this.numAncestors = (Integer)objectInputStream.readObject();
            int size = (Integer)objectInputStream.readObject();
            next = new HashMap<>();
            for (int i = 0; i < size; i++) {
                char c = (Character)objectInputStream.readObject();
                Node node = new Node();
                node.deserialize(objectInputStream);
                next.put(c, node);
            }
        }
    }

    /**
     * The trie root.
     */
    @NotNull
    private Node root = new Node();

    /**
     * The number of terminal vertices in the Trie.
     */
    private int trieSize = 0;

    /**
     * Adds some value to numAncestors through some path from the
     * root in Trie. The path must exist in the Trie.
     * @param path a String representing the path from root.
     * @param value value to be added to numAncestors.
     */
    private void addOnPath(@NotNull String path, int value) {
        @NotNull Node v = root;
        for (int i = 0; i < path.length(); i++) {
            v.increaseNumAncestors(value);
            v = v.getOrCreateNextNode(path.charAt(i));
        }
        v.increaseNumAncestors(value);
    }

    /**
     * Goes through given path from root and returns the bottom
     * vertex (or null if path does not exist).
     * @param path a String representing the path from root.
     * @return index of the bottom vertex or null (see the
     * description).
     */
    @Nullable
    private Node walkThroughPath(@NotNull String path) {
        @NotNull Node currentVertex = root;
        for (int i = 0; i < path.length(); i++) {
            Optional<Node> next = currentVertex.getNextNode(path.charAt(i));
            if (next.isPresent())
                currentVertex = next.get();
            else
                return null;
        }
        return currentVertex;
    }

    /**
     * Adds an element to the Trie. Each element is stored only once.
     * @param element An element to be added.
     * @return true if element already exists, false otherwise.
     */
    public boolean add(@NotNull String element) {
        Node currentVertex = root;
        for (int i = 0; i < element.length(); i++) {
            currentVertex = currentVertex.getOrCreateNextNode(element.charAt(i));
        }
        boolean ret = currentVertex.isTerminal();
        if (!ret) {
            trieSize++;
            addOnPath(element, 1);
        }
        currentVertex.setTerminal(true);
        return ret;
    }

    /**
     * Checks if Trie contains the element.
     */
    public boolean contains(@NotNull String element) {
        Node currentVertex = walkThroughPath(element);
        if (currentVertex == null) {
            return false;
        } else {
            return currentVertex.isTerminal();
        }
    }

    /**
     * Removes an element from the Trie. Even if the element was
     * added multiple times, it will be removed.
     * @return true if the element existed in the Trie before
     * removal, false otherwise.
     */
    public boolean remove(@NotNull String element) {
        Node currentVertex = walkThroughPath(element);
        if (currentVertex == null)
            return false;
        boolean ret = currentVertex.isTerminal();
        if (ret) {
            addOnPath(element, -1);
            trieSize--;
        }
        currentVertex.setTerminal(false);
        return ret;
    }

    /**
     * Returns number of unique Strings in the Trie.
     */
    public int size() {
        return trieSize;
    }

    /**
     * Returns number of Strings in the Trie which start with given
     * prefix.
     */
    int howManyStartsWithPrefix(@NotNull String prefix) {
        Node currentVertex = walkThroughPath(prefix);
        if (currentVertex == null) {
            return 0;
        } else {
            return currentVertex.getNumAncestors();
        }
    }

    /**
     * Serializes the Trie to OutputStream.
     */
    public void serialize(OutputStream out) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
        objectOutputStream.writeObject(this.trieSize);
        root.serialize(objectOutputStream);
    }

    /**
     * Deserializes the Trie from an InputStream.
     */
    public void deserialize(InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(in);
        this.trieSize = (Integer)objectInputStream.readObject();
        root.deserialize(objectInputStream);
    }
}
