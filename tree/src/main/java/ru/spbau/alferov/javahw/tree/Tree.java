package ru.spbau.alferov.javahw.tree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Tree<T extends Comparable<T>> {
    private class Node {
        @NotNull
        private T value;

        @Nullable
        private Node left = null;

        @Nullable
        private Node right = null;

        Node(@NotNull T store) {
            value = store;
        }
    }

    @Nullable
    private Node root = null;

    private int treeSize = 0;

    public Tree() {}

    public boolean contains(T value) {
        @Nullable Node cur = root;
        while (cur != null) {
            if (cur.value.compareTo(value) == 0) {
                return true;
            } else if (cur.value.compareTo(value) < 0) {
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return false;
    }

    public void add(@NotNull T value) {
        if (contains(value))
            return;
        treeSize++;
        if (root == null) {
            root = new Node(value);
            return;
        }
        @NotNull Node cur = root;
        while (true) {
            if (cur.value.compareTo(value) < 0) {
                if (cur.right == null) {
                    cur.right = new Node(value);
                    return;
                }
                cur = cur.right;
            } else {
                if (cur.left == null) {
                    cur.left = new Node(value);
                    return;
                }
                cur = cur.left;
            }
        }
    }

    public int size() {
        return treeSize;
    }
}
