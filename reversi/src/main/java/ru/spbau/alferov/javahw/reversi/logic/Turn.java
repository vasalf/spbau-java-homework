package ru.spbau.alferov.javahw.reversi.logic;

import ru.spbau.alferov.javahw.reversi.player.Player;

public class Turn {
    private int row;
    private int column;

    public Turn(int r, int c) {
        row = r;
        column = c;
    }

    public void setTurn(Turn turn) {
        row = turn.row;
        column = turn.column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
