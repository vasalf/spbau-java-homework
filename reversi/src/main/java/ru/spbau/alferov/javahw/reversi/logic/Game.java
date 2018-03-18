package ru.spbau.alferov.javahw.reversi.logic;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.ReversiApplication;
import ru.spbau.alferov.javahw.reversi.player.Player;
import ru.spbau.alferov.javahw.reversi.stats.PlayedGame;

public class Game {
    @NotNull
    private Player black;

    @NotNull
    private Player white;

    @NotNull
    private Field field;

    public Game(Player blackPlayer, Player whitePlayer) {
        black = blackPlayer;
        white = whitePlayer;
        field = new Field();
    }

    public enum Result {
        IN_PROGRESS,
        BLACK_WINS,
        WHITE_WINS,
        DRAW
    }

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

    public Player getBlackPlayer() {
        return black;
    }

    public Player getWhitePlayer() {
        return white;
    }
}
