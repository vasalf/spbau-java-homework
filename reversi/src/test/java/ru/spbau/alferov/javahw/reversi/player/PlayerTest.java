package ru.spbau.alferov.javahw.reversi.player;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import ru.spbau.alferov.javahw.reversi.BaseTest;
import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.Turn;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest extends BaseTest {
    /**
     * This tests that the GameInterruptedException can be thrown by the player.
     */
    @Test
    public void testInterruptedExceptionCreation() {
        Player player = new Player() {
            @NotNull
            @Override
            public Turn makeTurn(Field field) throws GameInterruptedException {
                throw new GameInterruptedException();
            }

            @Override
            public String getName() {
                return null;
            }
        };
        assertThrows(Player.GameInterruptedException.class, () -> player.makeTurn(new Field()), "Uncaught game interruption happened");
    }
}