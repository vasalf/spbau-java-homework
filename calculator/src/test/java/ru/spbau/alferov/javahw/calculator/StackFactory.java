package ru.spbau.alferov.javahw.calculator;

import java.util.ArrayList;
import java.util.EmptyStackException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StackFactory {
    /**
     * Creates an ArrayList-based MyStack objects.
     * I believe it works correctly.
     */
    static <T> MyStack<T> myStackFromArrayList() {
        ArrayList<T> lst = new ArrayList<>();
        MyStack<T> ret = mock(MyStack.class);

        when(ret.push(any())).thenAnswer(invocation -> {
            lst.add((T)invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(ret.pop()).thenAnswer(invocation -> {
            if (lst.isEmpty()) throw new EmptyStackException();
            T r = lst.get(lst.size() - 1);
            lst.remove(lst.size() - 1);
            return r;
        });

        when(ret.empty()).thenAnswer(invocation -> lst.isEmpty());

        when(ret.peek()).thenAnswer(invocation -> {
            if (lst.isEmpty()) throw new EmptyStackException();
            return lst.get(lst.size() - 1);
        });

        return ret;
    }
}
