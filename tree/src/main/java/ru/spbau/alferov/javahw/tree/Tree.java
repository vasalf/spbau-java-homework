package ru.spbau.alferov.javahw.tree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class stores a set of comparable objects.
 * Elements are stored in a non-balanced binary tree. Complexity of
 * operations is linear in the worst case.
 */
public class Tree<T extends Comparable<T>> {

    /**
     * The node of the tree.
     */
    private class Node {
        /**
         *  Value stored in the tree. Must not be null.
         */
        @NotNull
        private T value;

        /**
         * Points to the left child of this node.
         * Null iff node has no left child.
         */
        @Nullable
        private Node left = null;

        /**
         * Points to the right child of this node.
         * Null iff node has no right child.
         */
        @Nullable
        private Node right = null;

        /**
         * Basic constructor.
         * Sets left and right children to null.
         */
        public Node(@NotNull T store) {
            value = store;
        }
    }

    /**
     * Root of the tree. Null iff tree is empty.
     */
    @Nullable
    private Node root = null;

    /**
     * Current size of stored set.
     */
    private int treeSize = 0;

    /**
     * Basic constructor. Constructs an empty set.
     */
    public Tree() {}

    /**
     * Checks whether set contains the value.
     */
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

    /**
     * Adds an element to the set.
     * Does nothing if set already contains the element.
     */
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

    /**
     * Returns size of the tree.
     */
    public int size() {
        return treeSize;
    }
}
