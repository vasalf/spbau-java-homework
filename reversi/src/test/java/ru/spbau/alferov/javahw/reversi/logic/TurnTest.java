package ru.spbau.alferov.javahw.reversi.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TurnTest {
    /**
     * Tests the turn setter.
     */
    @Test
    public void testSetTurn() {
        Turn turn = new Turn(-1, -1);
        turn.setTurn(new Turn(2, 3));
        assertEquals(2, turn.getRow());
        assertEquals(3, turn.getColumn());
    }
}