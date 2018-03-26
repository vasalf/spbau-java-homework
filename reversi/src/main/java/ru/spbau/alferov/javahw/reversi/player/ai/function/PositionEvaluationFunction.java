package ru.spbau.alferov.javahw.reversi.player.ai.function;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.logic.Field;
/**
 * The abstract class for the evaluators used by players to evaluate the score
 * for the position.
 */
public abstract class PositionEvaluationFunction {
    /**
     * The name of the function (used for displaying on the opponent selection
     * screen)
     */
    public abstract String getName();

    /**
     * <p>Evaluates the function for given field.</p>
     *
     * <p>The more the value is, the more black player's chances are supposed
     * to be.</p>
     */
    public abstract int evaluate(@NotNull Field field);
}
