package ru.spbau.alferov.javahw.reversi.player.ai.function;

import org.junit.jupiter.api.Test;
import ru.spbau.alferov.javahw.reversi.logic.BaseLogicTest;
import ru.spbau.alferov.javahw.reversi.logic.Field;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreEvaluationTest extends BaseLogicTest {
    /**
     * Tests that the function does not have an empty name.
     */
    @Test
    public void testNonEmptyName() {
        assertNotEquals("", new ScoreEvaluation().getName());
    }

    /**
     * Tests the funciton on some position where black player won.
     */
    @Test
    public void testBlackWinPosition() {
        String[] position = {
                "BBBBBBBB",
                "BBBBBBBB",
                "BBBBBBBB",
                "BBBBBBBB",
                "BBBBBBBB",
                "BBBBBBBB",
                "BBBBBBBB",
                "BBBBBBBB"
        };
        Field field = createFieldFromPosition(position);
        assertTrue(new ScoreEvaluation().evaluate(field) > 0);
    }

    /**
     * Tests the funciton on some draw position.
     */
    @Test
    public void testDrawPosition() {
        String[] position = {
                "BBBBBBBB",
                "BBBBBBBB",
                "BBBBBBBB",
                "BBBBBBBB",
                "WWWWWWWW",
                "WWWWWWWW",
                "WWWWWWWW",
                "WWWWWWWW"
        };
        Field field = createFieldFromPosition(position);
        assertTrue(new ScoreEvaluation().evaluate(field) == 0);
    }

    /**
     * Tests the funciton on some position where white player won.
     */
    @Test
    public void testWhiteWinPosition() {
        String[] position = {
                "WWBBWWWWW",
                ".........",
                ".........",
                ".........",
                ".........",
                ".........",
                ".........",
                "........."
        };
        Field field = createFieldFromPosition(position);
        assertTrue(new ScoreEvaluation().evaluate(field) < 0);
    }
}