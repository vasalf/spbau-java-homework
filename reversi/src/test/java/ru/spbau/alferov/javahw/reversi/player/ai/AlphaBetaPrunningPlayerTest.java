package ru.spbau.alferov.javahw.reversi.player.ai;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import ru.spbau.alferov.javahw.reversi.logic.BaseLogicTest;
import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.Game;
import ru.spbau.alferov.javahw.reversi.player.ai.function.ScoreEvaluation;

import static org.junit.jupiter.api.Assertions.*;

class AlphaBetaPrunningPlayerTest extends BaseLogicTest {
    /**
     * Tests the player on some position where black wins.
     * Test is running against random player and every time it should win.
     */
    @RepeatedTest(10)
    public void testAlphaBetaPrunning() throws Exception {
        String[] position = {
                ".WWWWWWB",
                "WBBBBBB.",
                "WWWWWWWW",
                "WBBBBBBW",
                "WWWWWWWW",
                "WWWWWWWW",
                ".WWWWWW.",
                "BBBBBBBB"
        };
        Field f = createFieldFromPosition(position);
        Game game = new Game(new AlphaBetaPrunningPlayer(new ScoreEvaluation()), new RandomPlayer());
        setGameField(game, f);
        game.play();
        assertEquals(Game.Result.BLACK_WINS, game.getResult());
    }
}