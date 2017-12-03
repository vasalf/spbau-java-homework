package ru.spbau.alferov.javahw.calculator;

import org.junit.Test;
import ru.spbau.alferov.javahw.calculator.tokens.*;

import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class RPNConverterTest {
    /**
     * Tests that converter from empty string works and returns nothing.
     */
    @Test
    public void emptyStringConverter() throws ParseException {
        MyStack<CalcToken> lst = StackFactory.myStackFromArrayList();
        List<CalcToken> parsed = new RPNConverter("").convert(lst);
        assertTrue(parsed.isEmpty());
    }

    /**
     * Checks that an IntegerConstant is an IntegerConstant and stores the correct value.
     */
    private void checkIntegerConstant(CalcToken token, int which) {
        assertTrue(token.isNumber());
        MyStack<Integer> stack = StackFactory.myStackFromArrayList();
        token.evaluate(stack);
        assertEquals(which, (int)stack.peek());
    }

    /**
     * Tests that parsing an integer constant works.
     */
    @Test
    public void parseIntegerConstant() throws ParseException {
        MyStack<CalcToken> lst = StackFactory.myStackFromArrayList();
        List<CalcToken> parsed = new RPNConverter("179").convert(lst);
        assertEquals(1, parsed.size());
        checkIntegerConstant(parsed.get(0), 179);
    }

    /**
     * Tests that parsing the sum works.
     */
    @Test
    public void parseSum() throws ParseException {
        MyStack<CalcToken> lst = StackFactory.myStackFromArrayList();
        List<CalcToken> parsed = new RPNConverter("1+   178").convert(lst);
        assertEquals(3, parsed.size());
        checkIntegerConstant(parsed.get(0), 1);
        checkIntegerConstant(parsed.get(1), 178);
        assertTrue(parsed.get(2) instanceof PlusOperator);
    }

    /**
     * Tests that parsing the difference works.
     */
    @Test
    public void parseDifference() throws ParseException {
        MyStack<CalcToken> lst = StackFactory.myStackFromArrayList();
        List<CalcToken> parsed = new RPNConverter("1    -178").convert(lst);
        assertEquals(3, parsed.size());
        checkIntegerConstant(parsed.get(0), 1);
        checkIntegerConstant(parsed.get(1), 178);
        assertTrue(parsed.get(2) instanceof MinusOperator);
    }

    /**
     * Tests that parsing the multiplication works.
     */
    @Test
    public void parseMultiplication() throws ParseException {
        MyStack<CalcToken> lst = StackFactory.myStackFromArrayList();
        List<CalcToken> parsed = new RPNConverter("1*178").convert(lst);
        assertEquals(3, parsed.size());
        checkIntegerConstant(parsed.get(0), 1);
        checkIntegerConstant(parsed.get(1), 178);
        assertTrue(parsed.get(2) instanceof MultiplicationOperator);
    }

    /**
     * Tests that parsing the division works.
     */
    @Test
    public void parseDivision() throws ParseException {
        MyStack<CalcToken> lst = StackFactory.myStackFromArrayList();
        List<CalcToken> parsed = new RPNConverter("1 / 178 ").convert(lst);
        assertEquals(3, parsed.size());
        checkIntegerConstant(parsed.get(0), 1);
        checkIntegerConstant(parsed.get(1), 178);
        assertTrue(parsed.get(2) instanceof DivisionOperator);
    }

    /**
     * Tests that expression with brackets parses in correct way.
     */
    @Test
    public void parseWithBrackets() throws ParseException {
        MyStack<CalcToken> lst = StackFactory.myStackFromArrayList();
        List<CalcToken> parsed = new RPNConverter("1 * (2 + 3 * 4)").convert(lst);
        assertEquals(7, parsed.size());
        checkIntegerConstant(parsed.get(0), 1);
        checkIntegerConstant(parsed.get(1), 2);
        checkIntegerConstant(parsed.get(2), 3);
        checkIntegerConstant(parsed.get(3), 4);
        assertTrue(parsed.get(4) instanceof MultiplicationOperator);
        assertTrue(parsed.get(5) instanceof PlusOperator);
        assertTrue(parsed.get(6) instanceof MultiplicationOperator);
    }

    /**
     * Expects ParseException on mismatched brackets.
     */
    @Test(expected = ParseException.class)
    public void mismatchedBrackets() throws ParseException {
        MyStack<CalcToken> lst = StackFactory.myStackFromArrayList();
        new RPNConverter("1 * (2 + 3 * 4").convert(lst);
    }
}