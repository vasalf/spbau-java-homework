package ru.spbau.alferov.javahw.trie;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class implements a set of Strings. Strings can be added,
 * found and removed. Additionally, given a prefix, it can determine
 * the number of Strings in the Trie which have exactly this prefix.
 *
 * Each String is stored only once.
 *
 * All vertices are stored in an ArrayLists because we don't know the
 * capacity before creating the Trie.
 *
 * There is no Vertex class due to performance reasons (classes are
 * slow).
 */
public class Trie implements Serializable {
    /**
     * ArrayList of sets of children of vertices.
     * Namely, v = next.get(u).get(c) if and only if there is an edge
     * u->v by the letter c.
     */
    private ArrayList<HashMap<Character, Integer>> next;

    /**
     * The v-th element of this ArrayList is True if and only if v is
     * a terminal vertex.
     */
    private ArrayList<Boolean> isTerminal;

    /**
     * The v-th element of this ArrayList represents number of
     * terminal vertices in the Trie which are the ancestors of
     * vertex v.
     */
    private ArrayList<Integer> numAncestors;

    /**
     * The number of terminal vertices in the Trie.
     */
    private int trieSize = 0;

    /**
     * Adds a new vertex to the Trie.
     * @return index of a new vertex.
     */
    private int addNewVertex() {
        next.add(new HashMap<>());
        isTerminal.add(false);
        numAncestors.add(0);
        return next.size() - 1;
    }

    /**
     * Adds some value to numAncestors through some path from the
     * root in Trie. The path must exist in the Trie.
     * @param path a String representing the path from root.
     * @param value value to be added to numAncestors.
     */
    private void addOnPath(@NotNull String path, int value) {
        int currentVertex = 0;
        numAncestors.set(0, numAncestors.get(0) + value);
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            // We guarantee that given path exists in the Trie.
            currentVertex = next.get(currentVertex).get(c);
            numAncestors.set(currentVertex, numAncestors.get(currentVertex) + value);
        }
    }

    /**
     * Goes through given path from root and returns the bottom
     * vertex (or null if path does not exist).
     * @param path a String representing the path from root.
     * @return index of the bottom vertex or null (see the
     * description).
     */
    @Nullable
    private Integer walkThroughPath(@NotNull String path) {
        int currentVertex = 0;
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if (!next.get(currentVertex).containsKey(c))
                return null;
            currentVertex = next.get(currentVertex).get(c);
        }
        return currentVertex;
    }

    /**
     * Constructs an empty Trie.
     */
    public Trie() {
        next = new ArrayList<>();
        isTerminal = new ArrayList<>();
        numAncestors = new ArrayList<>();
        addNewVertex();
    }

    /**
     * Adds an element to the Trie. Each element is stored only once.
     * @param element An element to be added.
     * @return true if element already exists, false otherwise.
     */
    public boolean add(@NotNull String element) {
        int currentVertex = 0;
        for (int i = 0; i < element.length(); i++) {
            char c = element.charAt(i);
            if (!next.get(currentVertex).containsKey(c)) {
                int v = addNewVertex();
                next.get(currentVertex).put(c, v);
            }
            currentVertex = next.get(currentVertex).get(c);
        }
        boolean ret = isTerminal.get(currentVertex);
        if (!ret) {
            trieSize++;
            addOnPath(element, 1);
        }
        isTerminal.set(currentVertex, true);
        return ret;
    }

    /**
     * Checks if Trie contains the element.
     */
    public boolean contains(@NotNull String element) {
        Integer currentVertex = walkThroughPath(element);
        if (currentVertex == null) {
            return false;
        } else {
            return isTerminal.get(currentVertex);
        }
    }

    /**
     * Removes an element from the Trie. Even if the element was
     * added multiple times, it will be removed.
     * @return true if the element existed in the Trie before
     * removal, false otherwise.
     */
    public boolean remove(@NotNull String element) {
        Integer currentVertex = walkThroughPath(element);
        if (currentVertex == null)
            return false;
        boolean ret = isTerminal.get(currentVertex);
        if (ret) {
            addOnPath(element, -1);
            trieSize--;
        }
        isTerminal.set(currentVertex, false);
        return ret;
    }

    /**
     * Returns number of unique Strings in the Trie.
     */
    int size() {
        return trieSize;
    }

    /**
     * Returns number of Strings in the Trie which start with given
     * prefix.
     */
    int howManyStartsWithPrefix(@NotNull String prefix) {
        Integer currentVertex = walkThroughPath(prefix);
        if (currentVertex == null) {
            return 0;
        } else {
            return numAncestors.get(currentVertex);
        }
    }

    /**
     * Serializes the Trie to OutputStream.
     */
    public void serialize(OutputStream out) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
        objectOutputStream.writeObject(this);
    }

    /**
     * Deserializes the Trie from an InputStream.
     */
    public void deserialize(InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(in);
        Trie tr = (Trie)objectInputStream.readObject();
        this.trieSize = tr.trieSize;
        this.next = tr.next;
        this.isTerminal = tr.isTerminal;
        this.numAncestors = tr.numAncestors;
    }
}
