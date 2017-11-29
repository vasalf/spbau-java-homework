package ru.spbau.alferov.javahw.calculator;

import org.junit.Test;

import static org.junit.Assert.*;

public class MyStackTest {
    /**
     * Tests that constructor does not fail.
     */
    @Test
    public void construct() {
        MyStack<Integer> stack = new MyStack<>();
    }

    /**
     * Tests that pushing does not fail.
     */
    @Test
    public void push() {
        MyStack<String> stack = new MyStack<>();
        for (int i = 0; i < 179; i++) {
            stack.push(Integer.toString(i));
        }
    }

    /**
     * Tests that pop() works and takes elements in correct order.
     */
    @Test
    public void pop() {
        MyStack<Integer> stack = new MyStack<>();
        for (int i = 0; i < 179; i++) {
            stack.push(i);
        }
        for (int i = 178; i >= 0; i--) {
            assertEquals((long)i, (long)stack.pop());
        }
    }

    /**
     * Tests that peek() is correct before each pop().
     */
    @Test
    public void peek() {
        MyStack<Integer> stack = new MyStack<>();
        for (int i = 0; i < 179; i++) {
            stack.push(i);
        }
        for (int i = 178; i >= 0; i--) {
            assertEquals((long)i, (long)stack.peek());
            assertEquals((long)i, (long)stack.pop());
        }
    }

    /**
     * Tests empty() at every point of execution.
     */
    @Test
    public void empty() {
        MyStack<Integer> stack = new MyStack<>();
        assertTrue(stack.empty());
        for (int i = 0; i < 179; i++) {
            stack.push(i);
            assertFalse(stack.empty());
        }
        for (int i = 178; i >= 0; i--) {
            assertFalse(stack.empty());
            assertEquals((long)i, (long)stack.peek());
            assertEquals((long)i, (long)stack.pop());
        }
        assertTrue(stack.empty());
    }

}