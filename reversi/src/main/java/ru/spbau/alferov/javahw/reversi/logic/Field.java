package ru.spbau.alferov.javahw.reversi.logic;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a current position in some game.
 * Note that the Field instance represents only a local state
 * of game and does not know anything about the whole game.
 * For the game information refer to {@link Game}
 */
public class Field {
    /**
     * The current field.
     */
    @NotNull
    private SquareType[][] field;

    /**
     * Current score of black and white players.
     * This score is calulcated incrementally.
     */
    private int blackScore, whiteScore;

    /**
     * Variable representing whose turn it is now.
     */
    private boolean blackTurn;

    /**
     * Constructs the start field.
     */
    public Field() {
        field = new SquareType[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                field[i][j] = SquareType.EMPTY;
            }
        }
        field[3][3] = SquareType.BLACK;
        field[3][4] = SquareType.WHITE;
        field[4][3] = SquareType.WHITE;
        field[4][4] = SquareType.BLACK;
        blackScore = 2;
        whiteScore = 2;
        blackTurn = true;
    }

    /**
     * Get the square type for some square in the position.
     *
     * @param row    Row of desired square.
     * @param column Column of desired square.
     * @return Type of desired square.
     */
    public SquareType getSquare(int row, int column) {
        return field[row][column];
    }

    /**
     * Helper arrays for possible directions of seeking for
     * pieces to invert.
     */
    private final int[] dx = {-1,  0,  1, -1, 1, -1, 0, 1};
    private final int[] dy = {-1, -1, -1,  0, 0,  1, 1, 1};

    /**
     * Helper function that checks whether given square is inside the field.
     */
    private boolean isInField(int row, int column) {
        return row >= 0 && row < 8 && column >= 0 && column < 8;
    }

    /**
     * This function starts from some position in the field and seeks for the
     * given square type in given direction skipping all of the squares of
     * another given cell type.
     *
     * @param startRow       Row number of the start square
     * @param startColumn    Column number of the start square
     * @param rowMovement    Number added to the the row number on every iteration
     * @param columnMovement Number added to the the column number on every iteration
     * @param skipType       Cell type that should be skipped during the search
     * @param lookupType     Cell type we are looking for
     * @return               true if the search is successfull, false otherwise.
     */
    private boolean findSameCellInDirection(int startRow, int startColumn,
                                            int rowMovement, int columnMovement,
                                            SquareType skipType, SquareType lookupType) {
        int row = startRow + rowMovement;
        int column = startColumn + columnMovement;
        if (!isInField(row, column) || field[row][column] != skipType) {
            return false;
        }
        while (isInField(row, column) && field[row][column] == skipType) {
            row += rowMovement;
            column += columnMovement;
        }
        return isInField(row, column) && field[row][column] == lookupType;
    }

    /**
     * Checks whether player can place his piece on the given square or not.
     *
     * @param row     Row of the square
     * @param column  Column of the square
     * @param toPlace Type of player's square
     * @return        true if player can place his piece on the cell.
     */
    private boolean isAllowedTurnFor(int row, int column, SquareType toPlace) {
        if (field[row][column] != SquareType.EMPTY) {
            return false;
        }
        SquareType opponent = toPlace == SquareType.BLACK ? SquareType.WHITE : SquareType.BLACK;
        for (int i = 0; i < 8; i++) {
            if (findSameCellInDirection(row, column, dx[i], dy[i], opponent, toPlace)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether player whose turn it is now can place his piece on the
     * given square.
     *
     * @param row    Row of the square
     * @param column Column of the square
     * @return       true if player can place his piece on the cell
     */
    public boolean isAllowedTurn(int row, int column) {
        return isAllowedTurnFor(row, column, blackTurn ? SquareType.BLACK : SquareType.WHITE);
    }

    /**
     * Changes the square type and recaluclates the black and white scores.
     *
     * @param row    Row of the square
     * @param column Column of the square
     * @param type   New type for the cell
     */
    private void changeSquare(int row, int column, SquareType type) {
        if (field[row][column] == SquareType.WHITE) {
            whiteScore--;
        } else if (field[row][column] == SquareType.BLACK) {
            blackScore--;
        }

        if (type == SquareType.WHITE) {
            whiteScore++;
        } else {
            // type == SquareType.BLACK (by convention)
            blackScore++;
        }

        field[row][column] = type;
    }

    /**
     * Makes the turn for current player. Note that this function does not
     * check the turn for correctness.
     *
     * @param row    Row of the square or -1 for skipping.
     * @param column Column of the square or -1 for skipping.
     */
    private void makeTurn(int row, int column) {
        if (row != -1) {
            SquareType toPlace = blackTurn ? SquareType.BLACK : SquareType.WHITE;
            SquareType opponent = blackTurn ? SquareType.WHITE : SquareType.BLACK;
            changeSquare(row, column, toPlace);
            for (int i = 0; i < 8; i++) {
                if (findSameCellInDirection(row, column, dx[i], dy[i], opponent, toPlace)) {
                    int currentRow = row + dx[i], currentColumn = column + dy[i];
                    while (field[currentRow][currentColumn] == opponent) {
                        changeSquare(currentRow, currentColumn, toPlace);
                        currentRow += dx[i];
                        currentColumn += dy[i];
                    }
                }
            }
        }
        blackTurn = !blackTurn;
    }

    /**
     * Returns the list of allowed turns (used in the computer players
     * implementation.
     *
     * @param forPlayer Player for which the list will be returned.
     * @return          The list of alllowed turns.
     */
    public @NotNull List<Turn> getAllowedTurns(SquareType forPlayer) {
        List<Turn> result = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isAllowedTurnFor(i, j, forPlayer)) {
                    result.add(new Turn(i, j));
                }
            }
        }
        return result;
    }

    /**
     * @return The list of allowed turns for current player.
     */
    public @NotNull List<Turn> getAllowedTurns() {
        return getAllowedTurns(blackTurn ? SquareType.BLACK : SquareType.WHITE);
    }

    /**
     * Returns the score of white player.
     */
    public int getWhiteScore() {
        return whiteScore;
    }

    /**
     * Returns the score of black player.
     */
    public int getBlackScore() {
        return blackScore;
    }

    /**
     * Returns true if it is black turn now.
     */
    public boolean isBlackTurn() {
        return blackTurn;
    }

    /**
     * Returns a new Field in which the new
     *
     * @param field The field in which the turn should be made.
     * @param turn  The turn to be made.
     * @return      The new field.
     */
    public static @NotNull Field makeTurnInField(@NotNull Field field, @NotNull Turn turn) {
        Field ret = new Field();
        for (int i = 0; i < 8; i++) {
            System.arraycopy(field.field[i], 0, ret.field[i], 0, 8);
        }
        ret.blackScore = field.blackScore;
        ret.whiteScore = field.whiteScore;
        ret.blackTurn = field.blackTurn;
        ret.makeTurn(turn.getRow(), turn.getColumn());
        return ret;
    }
}
