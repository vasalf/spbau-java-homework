package ru.spbau.alferov.javahw.calculator.tokens;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.calculator.MyStack;

import java.util.EmptyStackException;

/**
 * A class representing the integer constant token.
 */
public class IntegerConstant extends CalcToken {
    /**
     * The stored constant.
     */
    private int token;

    /**
     *  The evaluation of the division operator.
     *  Just puts the stored constant onto the stack.
     */
    @Override
    public void evaluate(@NotNull MyStack<Integer> ints) throws EmptyStackException {
        ints.push(token);
    }

    public IntegerConstant(int constant) {
        token = constant;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    /**
     * The toString overrides are actually used for debugging.
     */
    @Override
    public String toString() {
        return Integer.toString(token);
    }
}
