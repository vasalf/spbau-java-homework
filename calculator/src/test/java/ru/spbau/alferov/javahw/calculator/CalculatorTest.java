package ru.spbau.alferov.javahw.calculator;

import org.junit.Test;
import ru.spbau.alferov.javahw.calculator.tokens.*;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class CalculatorTest {
    /**
     * Tests that calculation of a constant does not fail.
     */
    @Test
    public void calcIntegerConstant() throws ParseException {
        List<CalcToken> tok = new LinkedList<>();
        tok.add(new IntegerConstant(-42));
        assertEquals(-42, new Calculator(StackFactory.myStackFromArrayList()).calculate(tok));
    }

    /**
     * Tests that calculation of a sum works correctly.
     */
    @Test
    public void calcSum() throws ParseException {
        List<CalcToken> tok = new LinkedList<>();
        tok.add(new IntegerConstant(23));
        tok.add(new IntegerConstant(156));
        tok.add(new PlusOperator());
        assertEquals(179, new Calculator(StackFactory.myStackFromArrayList()).calculate(tok));
    }

    /**
     * Tests that calculation of a difference works correctly.
     */
    @Test
    public void calcDiff() throws ParseException {
        List<CalcToken> tok = new LinkedList<>();
        tok.add(new IntegerConstant(23));
        tok.add(new IntegerConstant(-156));
        tok.add(new MinusOperator());
        assertEquals(179, new Calculator(StackFactory.myStackFromArrayList()).calculate(tok));
    }

    /**
     * Tests that calculation of a multiplication works correctly.
     */
    @Test
    public void calcMul() throws ParseException {
        List<CalcToken> tok = new LinkedList<>();
        tok.add(new IntegerConstant(1));
        tok.add(new IntegerConstant(179));
        tok.add(new MultiplicationOperator());
        assertEquals(179, new Calculator(StackFactory.myStackFromArrayList()).calculate(tok));
    }

    /**
     * Tests that calculation of a division works correctly.
     */
    @Test
    public void calcDiv() throws ParseException {
        List<CalcToken> tok = new LinkedList<>();
        tok.add(new IntegerConstant(179));
        tok.add(new IntegerConstant(1));
        tok.add(new DivisionOperator());
        assertEquals(179, new Calculator(StackFactory.myStackFromArrayList()).calculate(tok));
    }

    /**
     * Tests that complex expression is calculated correctly.
     */
    @Test
    public void calcComplex() throws ParseException {
        List<CalcToken> tok = new LinkedList<>();
        tok.add(new IntegerConstant(1));
        tok.add(new IntegerConstant(2));
        tok.add(new IntegerConstant(3));
        tok.add(new IntegerConstant(4));
        tok.add(new MultiplicationOperator());
        tok.add(new PlusOperator());
        tok.add(new MultiplicationOperator());
        assertEquals(14, new Calculator(StackFactory.myStackFromArrayList()).calculate(tok));
    }

    /**
     * Tests that too less operands cause the ParseException.
     */
    @Test(expected = ParseException.class)
    public void testTooLessOperands() throws ParseException {
        List<CalcToken> tok = new LinkedList<>();
        tok.add(new PlusOperator());
        new Calculator(StackFactory.myStackFromArrayList()).calculate(tok);
    }

    /**
     * Tests that too many operands cause the ParseException
     */
    public void testTooManyOperands() throws ParseException {
        List<CalcToken> tok = new LinkedList<>();
        tok.add(new IntegerConstant(1));
        tok.add(new IntegerConstant(2));
        tok.add(new IntegerConstant(3));
        tok.add(new PlusOperator());
        new Calculator(StackFactory.myStackFromArrayList()).calculate(tok);
    }
}