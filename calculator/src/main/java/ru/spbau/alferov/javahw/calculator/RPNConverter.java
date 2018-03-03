package ru.spbau.alferov.javahw.calculator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.alferov.javahw.calculator.tokens.*;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

/**
 * Converts a given string to RPN-expression. Uses the given stack in
 * the process of calculation.
 */
public class RPNConverter {
    /**
     * The expression to be converted.
     */
    @NotNull
    private String expression;

    /**
     * The current position in the expressions (position after last
     * parsed token).
     */
    private int curPosition = 0;

    /**
     * Constructs the converter for given String.
     */
    public RPNConverter(@NotNull String expr) {
        expression = expr;
    }

    /**
     * Parses the next token and returns null if no tokens are to be
     * parsed.
     * @throws ParseException In case of illegal tokens in the
     * expression.
     */
    @Nullable
    private CalcToken parseNextToken() throws ParseException {
        // Skip symbols until we are in the beginning of next token.
        while (curPosition < expression.length()
                && Character.isSpaceChar(expression.charAt(curPosition))) {
            curPosition++;
        }

        if (curPosition == expression.length()) {
            return null;
        }

        if (Character.isDigit(expression.charAt(curPosition))) {
            // The token is a number
            StringBuilder strToken = new StringBuilder();
            while (curPosition < expression.length()
                    && Character.isDigit(expression.charAt(curPosition))) {
                strToken.append(expression.charAt(curPosition));
                curPosition++;
            }
            return new IntegerConstant(Integer.parseInt(strToken.toString()));
        } else {
            // The token is an operation
            char c = expression.charAt(curPosition);
            curPosition++;
            switch (c) {
                case '+':
                    return new PlusOperator();
                case '-':
                    return new MinusOperator();
                case '*':
                    return new MultiplicationOperator();
                case '/':
                    return new DivisionOperator();
                case '(':
                    return new OpeningBracket();
                case ')':
                    return new ClosingBracket();
            }
            throw new ParseException("Unexpected token", curPosition - 1);
        }
    }

    /**
     * Converts the expression into RPN.
     * @param stack
     * @return
     * @throws ParseException
     */
    @NotNull
    public List<CalcToken> convert(@NotNull MyStack<CalcToken> stack) throws ParseException {
        @NotNull List<CalcToken> ret = new LinkedList<>();

        CalcToken nextToken = parseNextToken();

        while (nextToken != null) {
            if (nextToken.isNumber()) {
                ret.add(nextToken);
            } else if (nextToken.isOperator()) {
                while (!stack.empty() && stack.peek().isOperator()
                        && stack.peek().operatorPriority() <= nextToken.operatorPriority()) {
                    ret.add(stack.pop());
                }
                stack.push(nextToken);
            } else if (nextToken.isOpeningBracket()) {
                stack.push(nextToken);
            } else {
                // Next token is a closing bracket
                while (!stack.empty() && !stack.peek().isOpeningBracket()) {
                    ret.add(stack.pop());
                }
                if (stack.empty())
                    throw new ParseException("Mismatched bracket", curPosition - 1);
                stack.pop();
            }

            nextToken = parseNextToken();
        }

        while (!stack.empty()) {
            if (stack.peek().isOpeningBracket())
                throw new ParseException("Mismatched bracket", curPosition);
            ret.add(stack.pop());
        }

        return ret;
    }
}
