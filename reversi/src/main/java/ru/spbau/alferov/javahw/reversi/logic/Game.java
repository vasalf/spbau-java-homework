package ru.spbau.alferov.javahw.reversi.logic;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.ReversiApplication;
import ru.spbau.alferov.javahw.reversi.player.Player;
import ru.spbau.alferov.javahw.reversi.stats.PlayedGame;

/**
 * This class represents the whole state of the whole game.
 * This class is used whenever we need to create a new game with two players
 * and play it.
 */
public class Game {
    /**
     * Player playing black.
     */
    @NotNull
    private Player black;

    /**
     * Player playing white.
     */
    @NotNull
    private Player white;

    /**
     * The current field for the game.
     */
    @NotNull
    private Field field;

    /**
     * Constructs a new game with empty field. Does not play it.
     *
     * @param blackPlayer Player playing black
     * @param whitePlayer Player playing white
     */
    public Game(@NotNull Player blackPlayer, @NotNull Player whitePlayer) {
        black = blackPlayer;
        white = whitePlayer;
        field = new Field();
    }

    /**
     * The result of the game.
     */
    public enum Result {
        IN_PROGRESS,
        BLACK_WINS,
        WHITE_WINS,
        DRAW
    }

    /**
     * Gets the result of the current game.
     */
    public Result getResult() {
        if (!field.getAllowedTurns(SquareType.BLACK).isEmpty() || !field.getAllowedTurns(SquareType.WHITE).isEmpty()) {
            return Result.IN_PROGRESS;
        } else if (field.getBlackScore() > field.getWhiteScore()) {
            return Result.BLACK_WINS;
        } else if (field.getBlackScore() == field.getWhiteScore()) {
            return Result.DRAW;
        } else {
            return Result.WHITE_WINS;
        }
    }

    /**
     * Starts playing the game.
     *
     * @throws Player.GameInterruptedException In case the game in some player's
     *                                         turn was interrupted.
     */
    public void play() throws Player.GameInterruptedException {
        ReversiApplication.getInstance().updateField(field);
        while (getResult() == Result.IN_PROGRESS) {
            Turn nextTurn;
            if (field.isBlackTurn()) {
                if (field.getAllowedTurns(SquareType.BLACK).isEmpty()) {
                    nextTurn = new Turn(-1, -1);
                } else {
                    nextTurn = black.makeTurn(field);
                }
            } else {
                if (field.getAllowedTurns(SquareType.WHITE).isEmpty()) {
                    nextTurn = new Turn(-1, -1);
                } else {
                    nextTurn = white.makeTurn(field);
                }
            }
            field = Field.makeTurnInField(field, nextTurn);
            ReversiApplication.getInstance().updateField(field);
        }
        ReversiApplication.getInstance().getStatsController().add(new PlayedGame(
                black.getName(), white.getName(), field.getBlackScore(), field.getWhiteScore()));
    }

    /**
     * Get the player playing black.
     */
    public @NotNull Player getBlackPlayer() {
        return black;
    }

    /**
     * Get the player playing white.
     */
    public @NotNull Player getWhitePlayer() {
        return white;
    }
}
