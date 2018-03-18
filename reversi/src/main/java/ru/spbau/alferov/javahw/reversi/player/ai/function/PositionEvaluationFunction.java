package ru.spbau.alferov.javahw.reversi.player.ai.function;

import ru.spbau.alferov.javahw.reversi.logic.Field;

public abstract class PositionEvaluationFunction {
    public abstract String getName();
    public abstract int evaluate(Field field);
}
