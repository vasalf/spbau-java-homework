package ru.spbau.alferov.javahw.calculator.tokens;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.calculator.MyStack;

import java.util.EmptyStackException;

/**
 * <p>An abstract class for all types of tokens that can be parsed from
 * the input string. The implemented methods are used to identify
 * the token's type and behaviour.</p>
 *
 * <p>For each token, exactly one of isNumber(), isOperator(),
 * isOpeningBracket(), isClosingBracket() calls must return true.
 * Information represented by these methods (and by operatorPriority()
 * as well) is used in RPNConverter.</p>
 */
public abstract class CalcToken {
    /**
     * This method defines the behaviour of token in calculation of
     * RPN-expression: how does the token affect the stack used in
     * the algorithm.
     */
    public abstract void evaluate(@NotNull MyStack<Integer> ints) throws EmptyStackException;

    /**
     * This method must return true for the number constants and only
     * for them. Most of the token types are not numbers so a default
     * implementation is represented there.
     */
    public boolean isNumber() {
        return false;
    }

    /**
     * This method must return true for operators and only for them.
     * As long as all of the operators have their priorities, the
     * default implementations checks the priority for being
     * overriden so there is no need to implement this method if
     * operatorPriority() is overriden.
     */
    public boolean isOperator() {
        return operatorPriority() != -1;
    }

    /**
     * This method must return trie for the opening brackets and only
     * for them. Most of the token types are not brackets so a
     * default implementation is represented there.
     */
    public boolean isOpeningBracket() {
        return false;
    }

    /**
     * This method must return trie for the closing brackets and only
     * for them. Most of the token types are not brackets so a
     * default implementation is represented there.
     */
    public boolean isClosingBracket() {
        return false;
    }

    /**
     * This method must return -1 if token is not an operator and the
     * operator priority otherwise. Currently the priorities are
     * <ul>
     *     <li>2 for binary "+" and "-"</li>
     *     <li>1 for "*" and "/"</li>
     * </ul>
     */
    public int operatorPriority() { return -1; }
}
