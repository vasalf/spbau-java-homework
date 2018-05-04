package ru.spbau.alferov.javacw.findpair.logic;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javacw.findpair.FindPairApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogicController {
    private int fieldSize;
    private int[][] field;

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

    public LogicController(int fieldSize) {
        this.fieldSize = fieldSize;
        createField();
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public int getCell(int i, int j) {
        return field[i][j];
    }

    private static class ClickedState {
        private int x = -1;
        private int y = -1;

        public boolean isClicked() {
            return x != -1;
        }

        public void setClicked(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setNotClicked() {
            x = -1;
            y = -1;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    @NotNull
    private ClickedState state = new ClickedState();

    private int score = 0;

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