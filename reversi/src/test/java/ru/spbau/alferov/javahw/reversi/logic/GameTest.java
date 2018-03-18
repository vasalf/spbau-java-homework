package ru.spbau.alferov.javahw.reversi.logic;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import ru.spbau.alferov.javahw.reversi.player.Player;
import ru.spbau.alferov.javahw.reversi.player.ai.AIPlayer;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest extends BaseLogicTest {
    private static class SkippingIdentifiedPlayer extends AIPlayer {
        private int id;

        public SkippingIdentifiedPlayer(int id) {
            this.id = id;
        }

        @Override
        public Turn makeTurn(Field field) {
            return new Turn(-1, -1);
        }

        @Override
        public String getName() {
            return String.valueOf(id);
        }
    }

    private static class RecordedPlayer extends AIPlayer {
        @NotNull
        private Turn[] toMake;
        private int nextTurnIndex = 0;

        private static Turn parseTurn(String position) {
            return new Turn(position.charAt(1) - '1', position.charAt(0) - 'a');
        }

        public RecordedPlayer(String[] turns) {
            toMake = Arrays.stream(turns).map(RecordedPlayer::parseTurn).toArray(Turn[]::new);
        }

        @Override
        public Turn makeTurn(Field field) {
            Turn ret = toMake[nextTurnIndex];
            nextTurnIndex++;
            return ret;
        }

        @Override
        public String getName() {
            return "Recorded";
        }
    }

    @Test
    public void testGetPlayer() {
        Game game = new Game(new SkippingIdentifiedPlayer(179), new SkippingIdentifiedPlayer(239));
        assertEquals("179", game.getBlackPlayer().getName());
        assertEquals("239", game.getWhitePlayer().getName());
    }

    @Test
    public void testInProgressGameResult() {
        String[] position = {
                "WWWWW.BB",
                "WWBWBBBB",
                "WBWBBWBB",
                "WBWBBWBB",
                "WBBWWWBB",
                "WBWBWWBB",
                "WWBBBWWB",
                "WBBBBBBB"
        };
        Field field = createFieldFromPosition(position);
        setBlackTurnInField(field, false);
        Game game = new Game(new SkippingIdentifiedPlayer(179), new SkippingIdentifiedPlayer(239));
        setGameField(game, field);
        assertEquals(Game.Result.IN_PROGRESS, game.getResult());
    }

    @Test
    public void testBlackWinsGameResult() {
        String[] position = {
                "WWWWWwBB",
                "WWBWwBBB",
                "WBWwBWBB",
                "WBWBBWBB",
                "WBBWWWBB",
                "WBWBWWBB",
                "WWBBBWWB",
                "WBBBBBBB"
        };
        Field field = createFieldFromPosition(position);
        setBlackTurnInField(field, false);
        Game game = new Game(new SkippingIdentifiedPlayer(179), new SkippingIdentifiedPlayer(239));
        setGameField(game, field);
        assertEquals(Game.Result.BLACK_WINS, game.getResult());
    }

    @Test
    public void testWhiteWinsGameResult() {
        String[] position = {
                "WWWWWWWW",
                "WWWWWWWW",
                "WWWWWWWW",
                "WWWWWWWW",
                "WWWWWWWW",
                "WWWWWWWW",
                "WWWWWWWW",
                "WWWWWWWW"
        };
        Field field = createFieldFromPosition(position);
        setBlackTurnInField(field, true);
        Game game = new Game(new SkippingIdentifiedPlayer(179), new SkippingIdentifiedPlayer(239));
        setGameField(game, field);
        assertEquals(Game.Result.WHITE_WINS, game.getResult());
    }

    @Test
    public void testDrawGameResult() {
        String[] position = {
                "WBWBWBWB",
                "BWBWBWBW",
                "WBWBWBWB",
                "BWBWBWBW",
                "WBWBWBWB",
                "BWBWBWBW",
                "WBWBWBWB",
                "BWBWBWBW"
        };
        Field field = createFieldFromPosition(position);
        setBlackTurnInField(field, true);
        Game game = new Game(new SkippingIdentifiedPlayer(179), new SkippingIdentifiedPlayer(239));
        setGameField(game, field);
        assertEquals(Game.Result.DRAW, game.getResult());
    }

    @Test
    public void testPlay() throws Player.GameInterruptedException {
        String[] blackTurns = {"d6", "b3", "f5", "c5", "f3", "d3", "b4", "b6", "g4", "f7", "b5", "c7", "e2", "h5", "g5",
                               "h7", "d7", "e8", "c2", "g3", "d1", "a2", "b1", "f2", "a7", "b2", "b8", "h8", "h1", "g7"};
        String[] whiteTurns = {"c4", "f6", "c6", "c3", "a3", "e6", "f4", "a6", "e3", "a4", "a5", "c8", "g6", "f8", "h6",
                               "e1", "d8", "e7", "h3", "c1", "f1", "a1", "d2", "g2", "b7", "h2", "g8", "h4", "g1", "a8"};
        String[] finalPosition = {
                "WBBBBBBB",
                "WWBBBBBB",
                "WBWWWBBB",
                "WBBWBWWB",
                "WBBBWWWB",
                "WBBWBWWB",
                "WBBBBBWB",
                "WBBBWWWB"
        };
        Game game = new Game(new RecordedPlayer(blackTurns), new RecordedPlayer(whiteTurns));
        game.play();
        Field finalField = getGameField(game);
        assertArrayEquals(finalPosition, createPositionFromField(finalField));
        assertEquals(Game.Result.BLACK_WINS, game.getResult());
    }

    @Test
    public void testBlackSkippingPlay() throws Player.GameInterruptedException {
        String[] blackTurns = {"f4", "f2", "d6", "g5", "d7", "g4", "e6", "g6", "c8", "b7", "c5", "g3", "e3", "b6", "c3",
                               "e8", "b5", "d3", "b3", "e2", "g2", "b2", "g8", "a5", "d2", "b1", "h2", "h7", "d1", "g1"};
        String[] whiteTurns = {"f3", "f5", "f1", "c7", "f6", "c6", "h4", "d8", "b8", "a8", "e7", "h5", "c4", "a7", "h3",
                               "h6", "c2", "f7", "f8", "a3", "h1", "a1", "a6", "b4", "h8", "a4", "a2", "g7", "c1", "e1"};
        String[] finalPosition = {
                "WWWWWWWW",
                "WWWWWWWB",
                "WWWWBWWB",
                "WWWBWBWB",
                "WWWBBWWB",
                "WWWBBWWB",
                "WWWWWBBB",
                "WWWWWWBW"
        };
        Game game = new Game(new RecordedPlayer(blackTurns), new RecordedPlayer(whiteTurns));
        game.play();
        Field finalField = getGameField(game);
        assertArrayEquals(finalPosition, createPositionFromField(finalField));
        assertEquals(Game.Result.WHITE_WINS, game.getResult());
    }

    @Test
    public void testWhiteSkippingPlay() throws Player.GameInterruptedException {
        String[] blackTurns = {"d6", "b6", "f6", "f4", "g4", "f8", "h6", "g3", "h4", "h8", "h2", "g7", "c5", "b4", "c7",
                               "d2", "a8", "c8", "e7", "a7", "d8", "a5", "b3", "c1", "e1", "a3", "f1", "a1", "b1", "g2",
                               "h1"};
        String[] whiteTurns = {"c6", "f5", "f7", "f3", "h5", "g5", "h7", "h3", "g6", "e6", "d3", "a6", "c4", "e3", "b7",
                               "c3", "b8", "d7", "g8", "e8", "a4", "b5", "c2", "d1", "e2", "b2", "f2", "a2", "g1"};
        String[] finalPosition = {
                "BBBBBBBB",
                "BBBBBBBB",
                "BBBBBBBB",
                "BBBBBBBB",
                "BBBBBBBB",
                "BBBBWBBB",
                "WBBBBWBB",
                "BBBBBBBB",
        };
        Game game = new Game(new RecordedPlayer(blackTurns), new RecordedPlayer(whiteTurns));
        game.play();
        Field finalField = getGameField(game);
        assertArrayEquals(finalPosition, createPositionFromField(finalField));
        assertEquals(Game.Result.BLACK_WINS, game.getResult());
    }

    @Test
    public void testGameEndOnNonFullField() throws Player.GameInterruptedException {
        String[] blackTurns = {"e3", "g6", "g3", "f4", "c3", "c5", "f2", "b2", "g5", "g7", "g8", "g1", "h4", "d2", "g4",
                               "e6", "a3", "d7", "b5", "b7", "g2", "b8", "b1", "e1", "a5"};
        String[] whiteTurns = {"f5", "f3", "h7", "d3", "h2", "b3", "f1", "a1", "f6", "h8", "f8", "h1", "f7", "h6", "h5",
                               "h3", "d6", "b6", "c6", "a7", "a8", "a6", "d1", "c1", "e8", "c8", "a4", "a2", "c2", "b4"};
        String[] finalPosition = {
                "WWW.WWWW",
                "WW.W.WWW",
                "WWWWWWWW",
                "WWWWWWWW",
                "WW.WWWWW",
                "WWWWWWWW",
                "WWWW.WWW",
                "WWWWBWWW"
        };
        Game game = new Game(new RecordedPlayer(blackTurns), new RecordedPlayer(whiteTurns));
        game.play();
        Field finalField = getGameField(game);
        assertArrayEquals(finalPosition, createPositionFromField(finalField));
        assertEquals(Game.Result.WHITE_WINS, game.getResult());
    }
}