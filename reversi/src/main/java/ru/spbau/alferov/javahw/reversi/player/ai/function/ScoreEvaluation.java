package ru.spbau.alferov.javahw.reversi.player.ai.function;

import ru.spbau.alferov.javahw.reversi.logic.Field;

public class ScoreEvaluation extends PositionEvaluationFunction {
    @Override
    public String getName() {
        return "score";
    }

    @Override
    public int evaluate(Field field) {
        return field.getBlackScore() - field.getWhiteScore();
    }
}
