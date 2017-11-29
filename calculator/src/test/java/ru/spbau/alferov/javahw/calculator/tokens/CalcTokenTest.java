package ru.spbau.alferov.javahw.calculator.tokens;

import org.junit.Test;
import ru.spbau.alferov.javahw.calculator.MyStack;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CalcTokenTest {
    /** Tests that exactly one of type-identifying functions returns true
     */
    private void testType(CalcToken token) {
        int x = 0;
        if (token.isOperator()) {
            x++;
        }
        if (token.isClosingBracket()) {
            x++;
        }
        if (token.isNumber()) {
            x++;
        }
        if (token.isOpeningBracket()) {
            x++;
        }
        assertTrue(x == 1);
    }

    /**
     * Tests that token is an operator.
     */
    private void testOperator(CalcToken token) {
        assertTrue(token.isOperator());
        assertNotEquals(-1, token.operatorPriority());
    }

    /**
     * Tests creation and functions of ClosingBracket.
     */
    @Test
    public void testClosingBracket() {
        CalcToken token = new ClosingBracket();
        assertTrue(token.isClosingBracket());
        testType(token);
    }

    private int someAnswer;

    /**
     * Tests creation and function of DivisionOperator
     */
    @Test
    public void testDivisionOperator() {
        CalcToken token = new DivisionOperator();
        testOperator(token);
        testType(token);

        MyStack<Integer> mockedStack = mock(MyStack.class);
        when(mockedStack.pop()).thenReturn(1,3);
        someAnswer = 0;
        when(mockedStack.push(anyInt())).thenAnswer(invocation -> {
            someAnswer = (Integer)invocation.getArguments()[0];
            return someAnswer;
        });
        token.evaluate(mockedStack);
        assertEquals(3, someAnswer);
    }
}