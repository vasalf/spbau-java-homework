package ru.spbau.alferov.javahw.reversi.player.ai;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.BaseTest;
import ru.spbau.alferov.javahw.reversi.logic.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomPlayerTest extends BaseTest {
    /**
     * Tests that Random player does not fail during the game.
     */
    @Test
    public void testRandomAgainstRandom() throws Exception {
        @NotNull Game game = new Game(new RandomPlayer(), new RandomPlayer());
        game.play();
    }
}