package ru.spbau.alferov.javahw.calculator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EmptyStackException;

/**
 * Stack of objects of type T. Implemented with a linked list.
 * Stored objects must not be null.
 */
public class MyStack<T> {
    /**
     * Node of the stored list.
     */
    private static class StackNode<T> {
        /**
         * Value stored in the list.
         */
        @NotNull
        private T value;

        /**
         * Next node in the list. Null if node is the last.
         */
        @Nullable
        private StackNode<T> next = null;

        /**
         * Constructs a node with the given stored value.
         * Next is automatically initialized with null.
         */
        StackNode(@NotNull T store) {
            value = store;
        }
    }

    /**
     * The head of the list or null if the stack is empty.
     */
    @Nullable
    private StackNode<T> stackHead = null;

    /**
     * Constructs an empty stack.
     */
    public MyStack() { }

    /**
     * Puts an item to the top of the stack.
     * @param item Item to be pushed.
     * @return The pushed item.
     */
    @NotNull
    public T push(@NotNull T item) {
        StackNode<T> newStackHead = new StackNode<>(item);
        newStackHead.next = stackHead;
        stackHead = newStackHead;
        return item;
    }

    /**
     * Removes an item from the top of the stack.
     * @return The removed item.
     * @throws EmptyStackException In case the stack is empty.
     */
    @NotNull
    public T pop() throws EmptyStackException {
        if (stackHead == null) {
            throw new EmptyStackException();
        }
        T item = stackHead.value;
        stackHead = stackHead.next;
        return item;
    }

    /**
     * Returns the top element of the stack.
     * @throws EmptyStackException In case the stack is empty.
     */
    @NotNull
    public T peek() throws EmptyStackException {
        if (stackHead == null) {
            throw new EmptyStackException();
        }
        return stackHead.value;
    }

    /**
     * Returns true if stack is empty and false otherwise.
     */
    public boolean empty() {
        return stackHead == null;
    }
}
