package ru.spbau.alferov.javahw.reversi.player.ai.function;

import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.SquareType;

public class NumberOfTurnsEvaluation extends PositionEvaluationFunction {
    @Override
    public String getName() {
        return "number of turns";
    }

    @Override
    public int evaluate(Field field) {
        if (field.isBlackTurn())
            return -field.getAllowedTurns(SquareType.WHITE).size();
        else
            return field.getAllowedTurns(SquareType.BLACK).size();
    }
}
