package ru.spbau.alferov.javahw.reversi.player.ai.function;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.logic.Field;

/**
 * Just returns the score (difference of black and white).
 */
public class ScoreEvaluation extends PositionEvaluationFunction {
    @Override
    public String getName() {
        return "score";
    }

    @Override
    public int evaluate(@NotNull Field field) {
        return field.getBlackScore() - field.getWhiteScore();
    }
}
