package ru.spbau.alferov.javahw.calculator.tokens;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.calculator.MyStack;

import java.util.EmptyStackException;

/**
 * A class representing the opening bracket token.
 */
public class OpeningBracket extends CalcToken {
    /**
     * The evaluation of opening brackets is actually not allowed as
     * long as we don't have any brackets in the RPM. So this method
     * actually should never be called.
     */
    @Override
    public void evaluate(@NotNull MyStack<Integer> ints) throws EmptyStackException { }

    @Override
    public boolean isOpeningBracket() {
        return true;
    }

    public OpeningBracket() { }
}
