package ru.spbau.alferov.javahw.reversi.player.ai;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.Turn;
import ru.spbau.alferov.javahw.reversi.player.ai.function.PositionEvaluationFunction;

import java.util.List;

public class AlphaBetaPrunningPlayer extends AIPlayer {
    @NotNull
    private PositionEvaluationFunction function;

    private int maxDepth = 8;

    public AlphaBetaPrunningPlayer(@NotNull PositionEvaluationFunction function) {
        this.function = function;
    }

    @Override
    public String getName() {
        return "Alpha-beta prunning (" + function.getName() + ")";
    }

    private int getValue(Field field, int curDepth, int alpha, int beta) {
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

    @Override
    public Turn makeTurn(Field field) throws GameInterruptedException {
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

    public int getMaxDepth() {
        return maxDepth;
    }
}
