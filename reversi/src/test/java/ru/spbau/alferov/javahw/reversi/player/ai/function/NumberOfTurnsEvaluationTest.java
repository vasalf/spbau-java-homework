package ru.spbau.alferov.javahw.reversi.player.ai.function;

import org.junit.jupiter.api.Test;
import ru.spbau.alferov.javahw.reversi.logic.BaseLogicTest;
import ru.spbau.alferov.javahw.reversi.logic.Field;

import static org.junit.jupiter.api.Assertions.*;

public class NumberOfTurnsEvaluationTest extends BaseLogicTest {
    @Test
    public void testNonEmptyName() {
        assertNotEquals("", new NumberOfTurnsEvaluation().getName());
    }

    @Test
    public void testEvaluation() {
        String[] position = {
                ".WWW..W.",
                "..W.WW..",
                "WWWWWW..",
                "..WWW...",
                "..WWBB..",
                "..W.....",
                "..W.....",
                "........"
        };
        Field field = createFieldFromPosition(position);
        assertEquals(-4, new NumberOfTurnsEvaluation().evaluate(field));
        setBlackTurnInField(field, false);
        assertEquals(3, new NumberOfTurnsEvaluation().evaluate(field));
    }
}