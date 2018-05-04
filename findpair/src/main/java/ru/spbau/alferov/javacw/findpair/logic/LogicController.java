package ru.spbau.alferov.javacw.findpair.logic;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javacw.findpair.FindPairApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is the main controller for the game logic.
 */
public class LogicController {
    /**
     * The field size given in command line arguments.
     */
    private int fieldSize;

    /**
     * The generated field for the game.
     */
    private int[][] field;

    /**
     * This generates a new field of size {@link #fieldSize}.
     *
     * Here we generate a random permutation of numbers 1..fieldSize * fieldSize / 2,
     * each repeated twice.
     */
    private void createField() {
        List<Integer> permutation = new ArrayList<>(fieldSize * fieldSize);
        for (int i = 0; i < fieldSize * fieldSize; i++) {
            permutation.add(i / 2 + 1);
        }
        Collections.shuffle(permutation);
        field = new int[fieldSize][fieldSize];
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                field[i][j] = permutation.get(i * fieldSize + j);
            }
        }
    }

    /**
     * Base constructor
     */
    public LogicController(int fieldSize) {
        this.fieldSize = fieldSize;
        createField();
    }

    /**
     * @return Size of the field
     */
    public int getFieldSize() {
        return fieldSize;
    }

    /**
     * @return The number in cell at the given position.
     */
    public int getCell(int i, int j) {
        return field[i][j];
    }

    /**
     *  This helper class represents the state of current game.
     *  Namely, currently the game may wait for the first or the second pressed cell.
     */
    private static class ClickedState {
        private int x = -1;
        private int y = -1;

        /**
         * @return true if the game is waiting for the second pressed cell, false otherwise.
         */
        public boolean isClicked() {
            return x != -1;
        }

        /**
         * Sets the first pressed cell.
         */
        public void setClicked(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Unsets the first pressed cell.
         */
        public void setNotClicked() {
            x = -1;
            y = -1;
        }

        /**
         * Get the X coordinate of the first pressed cell.
         */
        public int getX() {
            return x;
        }

        /**
         * Get the Y coordinate of the first pressed cell.
         */
        public int getY() {
            return y;
        }
    }

    /**
     * Current clicked state.
     */
    @NotNull
    private ClickedState state = new ClickedState();

    /**
     * Number of guessed squares.
     */
    private int score = 0;

    /**
     * This should be called whenever a cell is pressed.
     */
    public void clickCell(int i, int j) {
        if (!state.isClicked()) {
            state.setClicked(i, j);
            FindPairApplication.getInstance().getUI().getButton(i, j).showNumber();
        } else {
            if (field[state.getX()][state.getY()] == field[i][j]) {
                FindPairApplication.getInstance().getUI().getButton(state.getX(), state.getY()).setDeactivated();
                FindPairApplication.getInstance().getUI().getButton(i, j).setDeactivated();
                score += 2;
                if (score == fieldSize * fieldSize) {
                    FindPairApplication.getInstance().getUI().setWin();
                }
            } else {
                FindPairApplication.getInstance().getUI().getButton(i, j).showNumber();
                FindPairApplication.getInstance().getUI().disableClicks();
                FindPairApplication.getInstance().getUI().waitAndHide();
            }
            state.setNotClicked();
        }
    }
}