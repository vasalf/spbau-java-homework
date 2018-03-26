package ru.spbau.alferov.javahw.reversi.player.ai.function;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.SquareType;

/**
 * This function is used in attempt to reduce the opponent's number of allowed
 * turns.
 */
public class NumberOfTurnsEvaluation extends PositionEvaluationFunction {
    /**
     * {@link PositionEvaluationFunction#getName()}
     */
    @Override
    public String getName() {
        return "number of turns";
    }

    /**
     * {@link PositionEvaluationFunction#evaluate(Field)}
     */
    @Override
    public int evaluate(@NotNull Field field) {
        if (field.isBlackTurn())
            return -field.getAllowedTurns(SquareType.WHITE).size();
        else
            return field.getAllowedTurns(SquareType.BLACK).size();
    }
}
