package ru.spbau.alferov.javahw.reversi.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.Turn;

public class HumanPlayer extends Player {
    @Nullable
    private static HumanPlayer currentWaitingPlayer = null;

    @NotNull
    private final Turn currentTurn = new Turn(-1, -1);

    @Override
    @NotNull
    public Turn makeTurn(Field field) throws GameInterruptedException {
        // The Game is guaranteed to be running in one thread so
        // all of concurrency we need to handle is related to
        // JavaFX threads.
        currentTurn.setTurn(new Turn(-1, -1));
        currentWaitingPlayer = this;
        synchronized (currentTurn) {
            while (true) {
                try {
                    currentTurn.wait();
                } catch (InterruptedException ie) {
                    // This happens when a new game is started so the previous
                    // is interrupted by the global controller.
                    throw new GameInterruptedException();
                }
                if (currentTurn.getRow() != -1 && field.isAllowedTurn(currentTurn.getRow(), currentTurn.getColumn())) {
                    currentWaitingPlayer = null;
                    break;
                }
            }
        }
        return currentTurn;
    }

    @Override
    public String getName() {
        return "Human";
    }

    public void tryMakeTurn(Turn turn) {
        synchronized (currentTurn) {
            currentTurn.setTurn(turn);
            currentTurn.notify();
        }
    }

    @Nullable
    public static HumanPlayer getCurrentWaitingPlayer() {
        return currentWaitingPlayer;
    }
}
