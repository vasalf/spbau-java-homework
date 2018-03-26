package ru.spbau.alferov.javahw.reversi.player.ai.function;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.SquareType;

/**
 * Evaluates the weighted score: the score of all fields except corners is 1,
 * the score of the corners is 10.
 */
public class CornersEvaluation extends PositionEvaluationFunction {
    /**
     * {@link PositionEvaluationFunction#getName()}
     */
    @Override
    public String getName() {
        return "corners";
    }

    /**
     * {@link PositionEvaluationFunction#evaluate(Field)}
     */
    @Override
    public int evaluate(@NotNull Field field) {
        int score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int coef = 1;
                if ((i == 0 || i == 7) && (j == 0 || j == 7))
                    coef = 10;
                if (field.getSquare(i, j) == SquareType.BLACK)
                    score += coef;
                else if (field.getSquare(i, j) == SquareType.WHITE)
                    score -= coef;
            }
        }
        return score;
    }
}
