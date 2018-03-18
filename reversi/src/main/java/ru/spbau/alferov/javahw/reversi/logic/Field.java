package ru.spbau.alferov.javahw.reversi.logic;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Field {
    @NotNull
    private SquareType[][] field;

    private int blackScore, whiteScore;
    private boolean blackTurn;

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

    public SquareType getSquare(int row, int column) {
        return field[row][column];
    }

    private final int[] dx = {-1,  0,  1, -1, 1, -1, 0, 1};
    private final int[] dy = {-1, -1, -1,  0, 0,  1, 1, 1};

    private boolean isInField(int row, int column) {
        return row >= 0 && row < 8 && column >= 0 && column < 8;
    }

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

    public boolean isAllowedTurn(int row, int column) {
        return isAllowedTurnFor(row, column, blackTurn ? SquareType.BLACK : SquareType.WHITE);
    }

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

    public List<Turn> getAllowedTurns(SquareType forPlayer) {
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

    public List<Turn> getAllowedTurns() {
        return getAllowedTurns(blackTurn ? SquareType.BLACK : SquareType.WHITE);
    }

    public int getWhiteScore() {
        return whiteScore;
    }

    public int getBlackScore() {
        return blackScore;
    }

    public boolean isBlackTurn() {
        return blackTurn;
    }

    public static Field makeTurnInField(Field field, Turn turn) {
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
