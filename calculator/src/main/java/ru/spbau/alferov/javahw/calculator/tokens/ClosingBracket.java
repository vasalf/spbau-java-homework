package ru.spbau.alferov.javahw.calculator.tokens;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.calculator.MyStack;

import java.util.EmptyStackException;

/**
 * A class representing the closing bracket token.
 */
public class ClosingBracket extends CalcToken {
    /**
     * The evaluation of closing brackets is actually not allowed as
     * long as we don't have any brackets in the RPM. So this method
     * actually should never be called.
     */
    @Override
    public void evaluate(@NotNull MyStack<Integer> ints) throws EmptyStackException { }

    @Override
    public boolean isClosingBracket() {
        return true;
    }

    public ClosingBracket() { }
}
