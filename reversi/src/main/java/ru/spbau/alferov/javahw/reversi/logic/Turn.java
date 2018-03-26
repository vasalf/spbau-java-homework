package ru.spbau.alferov.javahw.reversi.logic;


/**
 * The class representing some turn to be returned by the player.
 */
public class Turn {
    /**
     * The row of the square for the turn.
     */
    private int row;

    /**
     * The column of the square for the turn.
     */
    private int column;

    /**
     * Constructs a new turn in the given square.
     *
     * @param r Row of the square.
     * @param c Column of the square.
     */
    public Turn(int r, int c) {
        row = r;
        column = c;
    }

    /**
     * Copies the turn to this.
     *
     * @param turn Turn to be copied.
     */
    public void setTurn(Turn turn) {
        row = turn.row;
        column = turn.column;
    }

    /**
     * Get the row for the current turn.
     */
    public int getRow() {
        return row;
    }

    /**
     * Get the column for the current turn.
     */
    public int getColumn() {
        return column;
    }
}
