package ru.spbau.alferov.javahw.reversi.player.ai.function;

import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.SquareType;

public class CornersEvaluation extends PositionEvaluationFunction {

    @Override
    public String getName() {
        return "corners";
    }

    @Override
    public int evaluate(Field field) {
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
