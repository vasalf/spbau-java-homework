package ru.spbau.alferov.javahw.trie;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Trie implements Serializable {
    private ArrayList<HashMap<Character, Integer>> next;
    private ArrayList<Boolean> isTerminal;
    private ArrayList<Integer> numAncestors;
    private int trieSize = 0;

    private int addNewVertex() {
        next.add(new HashMap<>());
        isTerminal.add(false);
        numAncestors.add(0);
        return next.size() - 1;
    }

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

    public Trie() {
        next = new ArrayList<>();
        isTerminal = new ArrayList<>();
        numAncestors = new ArrayList<>();
        addNewVertex();
    }

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

    public boolean contains(@NotNull String element) {
        Integer currentVertex = walkThroughPath(element);
        if (currentVertex == null) {
            return false;
        } else {
            return isTerminal.get(currentVertex);
        }
    }

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

    int size() {
        return trieSize;
    }

    int howManyStartsWithPrefix(@NotNull String prefix) {
        Integer currentVertex = walkThroughPath(prefix);
        if (currentVertex == null) {
            return 0;
        } else {
            return numAncestors.get(currentVertex);
        }
    }

    public void serialize(OutputStream out) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
        objectOutputStream.writeObject(this);
    }

    public void deserialize(InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(in);
        Trie tr = (Trie)objectInputStream.readObject();
        this.trieSize = tr.trieSize;
        this.next = tr.next;
        this.isTerminal = tr.isTerminal;
        this.numAncestors = tr.numAncestors;
    }
}
