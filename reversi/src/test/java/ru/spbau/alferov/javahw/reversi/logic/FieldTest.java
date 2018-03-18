package ru.spbau.alferov.javahw.reversi.logic;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest extends BaseLogicTest {
    @Test
    public void testInitialPosition() {
        String[] initialPosition = {
                "........",
                "........",
                "........",
                "...WB...",
                "...BW...",
                "........",
                "........",
                "........"
        };
        Field f = new Field();
        assertArrayEquals(initialPosition, createPositionFromField(f));
        assertTrue(f.isBlackTurn());
    }

    @Test
    public void testIsAllowedTurnForInitialPosition() {
        Field f = new Field();
        assertTrue(f.isAllowedTurn(5, 3));
        assertTrue(f.isAllowedTurn(3, 5));
        assertFalse(f.isAllowedTurn(2, 3));
        assertFalse(f.isAllowedTurn(3, 2));
        setBlackTurnInField(f, false);
        assertFalse(f.isAllowedTurn(5, 3));
        assertFalse(f.isAllowedTurn(3, 5));
        assertTrue(f.isAllowedTurn(2, 3));
        assertTrue(f.isAllowedTurn(3, 2));
    }

    @Test
    public void testFarTurnIsNotAllowed() {
        String[] position = {
                "BBBB....",
                "BBBB....",
                "BBBB....",
                "........",
                "........",
                "....WWWW",
                "....WWWW",
                "....WWWW"
        };
        Field f = createFieldFromPosition(position);
        assertFalse(f.isAllowedTurn(7, 7));
    }

    @Test
    public void testTurnInversionUp() {
        String[] startPosition = {
                "....W...",
                "....B...",
                "....B...",
                "...B....",
                "...BW...",
                "........",
                "........",
                "........"
        };
        String[] endPosition = {
                "....W...",
                "....W...",
                "....W...",
                "...BW...",
                "...BW...",
                "........",
                "........",
                "........"
        };
        Field startField = createFieldFromPosition(startPosition);
        setBlackTurnInField(startField, false);
        assertTrue(startField.isAllowedTurn(4, 4));
        Field endField = Field.makeTurnInField(startField, new Turn(4, 4));
        assertArrayEquals(endPosition, createPositionFromField(endField));
        assertEquals(2, endField.getBlackScore());
        assertEquals(5, endField.getWhiteScore());
    }

    @Test
    public void testTurnInversionDown() {
        String[] startPosition = {
                "........",
                "........",
                "....B...",
                "...BWW..",
                "...BW.B.",
                "......B.",
                "......W.",
                "........"
        };
        String[] endPosition = {
                "........",
                "........",
                "....B...",
                "...BWWW.",
                "...BW.W.",
                "......W.",
                "......W.",
                "........"
        };
        Field startField = createFieldFromPosition(startPosition);
        setBlackTurnInField(startField, false);
        assertTrue(startField.isAllowedTurn(4, 6));
        Field endField = Field.makeTurnInField(startField, new Turn(4, 6));
        assertArrayEquals(endPosition, createPositionFromField(endField));
        assertEquals(3, endField.getBlackScore());
        assertEquals(7, endField.getWhiteScore());
    }

    @Test
    public void testTurnInversionLeft() {
         String[] startPosition = {
                "........",
                "........",
                "........",
                "...BB...",
                ".BWW.B..",
                "....W...",
                "........",
                "........"
        };
        String[] endPosition = {
                "........",
                "........",
                "........",
                "...BB...",
                ".BBBBB..",
                "....W...",
                "........",
                "........"
        };
        Field startField = createFieldFromPosition(startPosition);
        setBlackTurnInField(startField, true);
        assertTrue(startField.isAllowedTurn(3, 4));
        Field endField = Field.makeTurnInField(startField, new Turn(3, 4));
        assertArrayEquals(endPosition, createPositionFromField(endField));
        assertEquals(7, endField.getBlackScore());
        assertEquals(1, endField.getWhiteScore());
    }

    @Test
    public void testTurnInversionRight() {
         String[] startPosition = {
                "........",
                "........",
                "........",
                ".BBBBBBW",
                "...WW...",
                "........",
                "........",
                "........"
        };
        String[] endPosition = {
                "........",
                "........",
                "........",
                "WWWWWWWW",
                "...WW...",
                "........",
                "........",
                "........"
        };
        Field startField = createFieldFromPosition(startPosition);
        setBlackTurnInField(startField, false);
        assertTrue(startField.isAllowedTurn(4, 0));
        Field endField = Field.makeTurnInField(startField, new Turn(4, 0));
        assertArrayEquals(endPosition, createPositionFromField(endField));
        assertEquals(0, endField.getBlackScore());
        assertEquals(10, endField.getWhiteScore());
    }

    @Test
    public void testTurnInsertionUpRight() {
         String[] startPosition = {
                "........",
                "......W.",
                ".....B..",
                "...BB...",
                "...BB...",
                "..B.....",
                "........",
                "........"
        };
        String[] endPosition = {
                "........",
                "......W.",
                ".....W..",
                "...BW...",
                "...WB...",
                "..W.....",
                ".W......",
                "........"
        };
        Field startField = createFieldFromPosition(startPosition);
        setBlackTurnInField(startField, false);
        assertTrue(startField.isAllowedTurn(1, 1));
        Field endField = Field.makeTurnInField(startField, new Turn(1, 1));
        assertArrayEquals(endPosition, createPositionFromField(endField));
        assertEquals(2, endField.getBlackScore());
        assertEquals(6, endField.getWhiteScore());
    }

    @Test
    public void testTurnInversionDownRight() {
         String[] startPosition = {
                "........",
                "........",
                "........",
                "........",
                "........",
                ".W......",
                "..W.....",
                "...B...."
        };
        String[] endPosition = {
                "........",
                "........",
                "........",
                "........",
                "B.......",
                ".B......",
                "..B.....",
                "...B...."
        };
        Field startField = createFieldFromPosition(startPosition);
        setBlackTurnInField(startField, true);
        assertTrue(startField.isAllowedTurn(3, 0));
        Field endField = Field.makeTurnInField(startField, new Turn(3, 0));
        assertArrayEquals(endPosition, createPositionFromField(endField));
        assertEquals(4, endField.getBlackScore());
        assertEquals(0, endField.getWhiteScore());
    }

    @Test
    public void testTurnInversionDownLeft() {
         String[] startPosition = {
                ".....B..",
                "....W...",
                "...W....",
                "..W.....",
                ".W......",
                "........",
                "........",
                "........"
        };
        String[] endPosition = {
                ".....B..",
                "....B...",
                "...B....",
                "..B.....",
                ".B......",
                "B.......",
                "........",
                "........"
        };
        Field startField = createFieldFromPosition(startPosition);
        setBlackTurnInField(startField, true);
        assertTrue(startField.isAllowedTurn(2, 0));
        Field endField = Field.makeTurnInField(startField, new Turn(2, 0));
        assertArrayEquals(endPosition, createPositionFromField(endField));
        assertEquals(6, endField.getBlackScore());
        assertEquals(0, endField.getWhiteScore());
    }

    @Test
    public void testTurnInversionUpLeft() {
         String[] startPosition = {
                "........",
                "........",
                "........",
                "...BB...",
                "...BB...",
                ".....W..",
                "........",
                "........"
        };
        String[] endPosition = {
                "........",
                "........",
                "........",
                "...BB...",
                "...BB...",
                ".....B..",
                "......B.",
                "........"
        };
        Field startField = createFieldFromPosition(startPosition);
        setBlackTurnInField(startField, true);
        assertTrue(startField.isAllowedTurn(1, 6));
        Field endField = Field.makeTurnInField(startField, new Turn(1, 6));
        assertArrayEquals(endPosition, createPositionFromField(endField));
        assertEquals(6, endField.getBlackScore());
        assertEquals(0, endField.getWhiteScore());
    }

    @Test
    public void testTurnComplexInversion() {
         String[] startPosition = {
                "W...W...",
                ".B..B..W",
                "..B.B.B.",
                "...BBB..",
                "WBBB.BBW",
                "...BBB..",
                "..B.B.B.",
                ".W..W..W"
        };
        String[] endPosition = {
                "W...W...",
                ".W..W..W",
                "..W.W.W.",
                "...WWW..",
                "WWWWWWWW",
                "...WWW..",
                "..W.W.W.",
                ".W..W..W"
        };
        Field startField = createFieldFromPosition(startPosition);
        setBlackTurnInField(startField, false);
        assertTrue(startField.isAllowedTurn(3, 4));
        Field endField = Field.makeTurnInField(startField, new Turn(3, 4));
        assertArrayEquals(endPosition, createPositionFromField(endField));
        assertEquals(0, endField.getBlackScore());
        assertEquals(28, endField.getWhiteScore());
    }

    @Test
    public void testGetAllowedTurns() {
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
        assertTurnSetsEqual(Arrays.asList(
                new Turn(3, 1),
                new Turn(6, 1),
                new Turn(7, 4)), field.getAllowedTurns());
        setBlackTurnInField(field, false);
        assertTurnSetsEqual(Arrays.asList(
                new Turn(3, 6),
                new Turn(2, 4),
                new Turn(2, 5),
                new Turn(2, 6)), field.getAllowedTurns());
    }

    @Test
    public void testGetSquare() {
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
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (position[7 - i].charAt(j) == '.') {
                    assertEquals(SquareType.EMPTY, field.getSquare(i, j));
                } else if (position[7 - i].charAt(j) == 'W') {
                    assertEquals(SquareType.WHITE, field.getSquare(i, j));
                } else {
                    assertEquals(SquareType.BLACK, field.getSquare(i, j));
                }
            }
        }
    }

    @Test
    public void testSkipTurn() {
        String[] position = createPositionFromField(new Field());
        Field startField = new Field();
        Field endField = Field.makeTurnInField(startField, new Turn(-1, -1));
        assertFalse(endField.isBlackTurn());
        assertArrayEquals(position, createPositionFromField(endField));
    }
}