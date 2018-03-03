package ru.spbau.alferov.javahw.calculator.tokens;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.calculator.MyStack;

import java.util.EmptyStackException;

/**
 * A class representing the binary "+" operator.
 */
public class PlusOperator extends CalcToken {
    /**
     * The evaluation of the plus operator.
     * Takes two top elements from the stack at puts their difference
     * back.
     */
    @Override
    public void evaluate(@NotNull MyStack<Integer> ints) throws EmptyStackException {
        int b = ints.pop();
        int a = ints.pop();
        ints.push(a + b);
    }

    public PlusOperator() { }

    /**
     * {@link CalcToken#operatorPriority()}
     */
    @Override
    public int operatorPriority() {
        return 2;
    }

    /**
     * The toString overrides are actually used for debugging.
     */
    @Override
    public String toString() {
        return "+";
    }
}
