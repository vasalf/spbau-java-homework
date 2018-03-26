package ru.spbau.alferov.javahw.reversi.player.ai;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.Turn;
import ru.spbau.alferov.javahw.reversi.player.Player;
import ru.spbau.alferov.javahw.reversi.player.ai.function.PositionEvaluationFunction;

import java.util.List;

/**
 * This player uses alpha-beta prunning for minimax algorithm.
 * The calculation depth is 8 turns on early and late stages of the game
 * and 6 in the middle as the number of allowed turn is very much.
 */
public class AlphaBetaPrunningPlayer extends AIPlayer {
    /**
     * The function used to evaluate the position.
     */
    @NotNull
    private PositionEvaluationFunction function;

    /**
     * Current maximum depth of evaluation.
     */
    private int maxDepth = 8;

    /**
     * Constructs a new player with given evaluator.
     */
    public AlphaBetaPrunningPlayer(@NotNull PositionEvaluationFunction function) {
        this.function = function;
    }

    /**
     * {@link Player#getName()}
     */
    @Override
    public String getName() {
        return "Alpha-beta prunning (" + function.getName() + ")";
    }

    /**
     * The actual evaluating function.
     *
     * @param field    The field for which the optimal value should be evaluated.
     * @param curDepth The current depth of evaluation.
     * @param alpha    The current maximum value the black player could get.
     * @param beta     The current minimum value the white player could get.
     * @return The value of the position.
     */
    private int getValue(@NotNull Field field, int curDepth, int alpha, int beta) {
        if (curDepth == maxDepth) {
            return function.evaluate(field);
        } else if (field.isBlackTurn()) {
            int value = Integer.MIN_VALUE;
            List<Turn> turns = field.getAllowedTurns();
            if (turns.isEmpty())
                turns.add(new Turn(-1, -1));
            for (Turn t : turns) {
                value = Integer.max(value, getValue(Field.makeTurnInField(field, t), curDepth + 1, alpha, beta));
                alpha = Integer.max(alpha, value);
                if (beta <= alpha) {
                    break;
                }
            }
            return value;
        } else {
            int value = Integer.MAX_VALUE;
            List<Turn> turns = field.getAllowedTurns();
            if (turns.isEmpty())
                turns.add(new Turn(-1, -1));
            for (Turn t : turns) {
                value = Integer.min(value, getValue(Field.makeTurnInField(field, t), curDepth + 1, alpha, beta));
                beta = Integer.min(beta, value);
                if (beta <= alpha) {
                    break;
                }
            }
            return value;
        }
    }

    /**
     * {@link Player#makeTurn(Field)}
     */
    @Override
    public @NotNull Turn makeTurn(@NotNull Field field) {
        if (field.getWhiteScore() + field.getBlackScore() >= 15 && field.getWhiteScore() + field.getBlackScore() <= 40)
            maxDepth = 6;
        else
            maxDepth = 8;
        List<Turn> turns = field.getAllowedTurns();
        Turn ans = new Turn(-1, -1);
        int bestValue;
        if (field.isBlackTurn()) {
            bestValue = Integer.MIN_VALUE;
            for (Turn t : turns) {
                int value = getValue(Field.makeTurnInField(field, t), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                if (value >= bestValue) {
                    bestValue = value;
                    ans = t;
                }
            }
        } else {
            bestValue = Integer.MAX_VALUE;
            for (Turn t : turns) {
                int value = getValue(Field.makeTurnInField(field, t), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                if (value <= bestValue) {
                    bestValue = value;
                    ans = t;
                }
            }
        }
        return ans;
    }

    /**
     * Gets the maximum depth for the last evaluation.
     */
    public int getMaxDepth() {
        return maxDepth;
    }
}
