package ru.spbau.alferov.javahw.calculator;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.calculator.tokens.CalcToken;

import java.text.ParseException;
import java.util.EmptyStackException;
import java.util.List;

/**
 * This class calculates given RPN-expressions.
 *
 * The expression is a List of CalcTokens. Uses stack in the
 * calculations.
 */
public class Calculator {
    /**
     * The stack used in the calculations.
     *
     * The class can be theoretically used for multiple consistent
     * calculations. The stack remains empty between calculations.
     */
    @NotNull
    private MyStack<Integer> currentState;

    /**
     * Basic constructor. Takes a stack-like object that is supposed
     * to be used in the calculations.
     */
    public Calculator(@NotNull MyStack<Integer> toStore) {
        currentState = toStore;
    }

    /**
     * Calculates the given expressions with given stack. Returns the
     * value.
     */
    int calculate(@NotNull List<CalcToken> tokens) throws EmptyStackException, ParseException{
        for (CalcToken token : tokens) {
            token.evaluate(currentState);
        }

        int ans = currentState.pop();
        if (!currentState.empty()) {
            while (!currentState.empty()) {
                currentState.pop();
            }
            throw new ParseException("Parse error", tokens.size());
        }
        return ans;
    }
}
