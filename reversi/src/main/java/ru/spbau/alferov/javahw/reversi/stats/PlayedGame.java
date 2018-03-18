package ru.spbau.alferov.javahw.reversi.stats;

import org.jetbrains.annotations.NotNull;

public class PlayedGame {
    @NotNull
    private String blackPlayerName;

    @NotNull
    private String whitePlayerName;

    private int blackPlayerScore;

    private int whitePlayerScore;

    public PlayedGame(@NotNull String blackPlayerName, @NotNull String whitePlayerName, int blackPlayerScore, int whitePlayerScore) {
        this.blackPlayerName = blackPlayerName;
        this.whitePlayerName = whitePlayerName;
        this.blackPlayerScore = blackPlayerScore;
        this.whitePlayerScore = whitePlayerScore;
    }

    @NotNull
    public String getBlackPlayerName() {
        return blackPlayerName;
    }

    @NotNull
    public String getWhitePlayerName() {
        return whitePlayerName;
    }

    public int getBlackPlayerScore() {
        return blackPlayerScore;
    }

    public int getWhitePlayerScore() {
        return whitePlayerScore;
    }
}
