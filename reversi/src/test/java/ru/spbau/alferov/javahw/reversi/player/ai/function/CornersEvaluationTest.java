package ru.spbau.alferov.javahw.reversi.player.ai.function;

import org.junit.jupiter.api.Test;
import ru.spbau.alferov.javahw.reversi.logic.BaseLogicTest;
import ru.spbau.alferov.javahw.reversi.logic.Field;

import static org.junit.jupiter.api.Assertions.*;

public class CornersEvaluationTest extends BaseLogicTest {
    @Test
    public void testNonEmptyName() {
        assertNotEquals("", new CornersEvaluation().getName());
    }

    @Test
    public void testNoCornersScore() {
        String[] position = {
                ".WWWWWW.",
                "BBBBBBBB",
                "WWWWWWWW",
                "BBBBBBBB",
                "WWWWWWWW",
                "BBBBBBBB",
                "WWWWWWWW",
                ".BBBBBB."
        };
        Field field = createFieldFromPosition(position);
        assertEquals(field.getBlackScore() - field.getWhiteScore(), new CornersEvaluation().evaluate(field));
    }

    @Test
    public void testBlackCornersScore() {
        String[] position = {
                "B......B",
                "........",
                "........",
                "........",
                "........",
                "........",
                "........",
                "B......B"
        };
        Field field = createFieldFromPosition(position);
        assertEquals(40, new CornersEvaluation().evaluate(field));
    }

    @Test
    public void testWhiteCornersScore() {
        String[] position = {
                "W......W",
                "........",
                "........",
                "........",
                "........",
                "........",
                "........",
                "W......W"
        };
        Field field = createFieldFromPosition(position);
        assertEquals(-40, new CornersEvaluation().evaluate(field));
    }
}