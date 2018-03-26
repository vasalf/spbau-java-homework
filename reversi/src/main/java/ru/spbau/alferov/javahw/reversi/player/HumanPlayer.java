package ru.spbau.alferov.javahw.reversi.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.Turn;

/**
 * This is the class for human player.
 */
public class HumanPlayer extends Player {
    /**
     * If some HumanPlayer is waiting for the human to make his turn,
     * this would not be null.
     */
    @Nullable
    private static HumanPlayer currentWaitingPlayer = null;

    /**
     * That is the last turn made by human.
     */
    @NotNull
    private final Turn currentTurn = new Turn(-1, -1);

    /**
     * {@link Player#makeTurn(Field)}
     */
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

    /**
     * {@link Player#getName()}
     */
    @Override
    public String getName() {
        return "Human";
    }

    /**
     * This function is called by the UI when human has made the decision.
     *
     * @param turn The human's decision.
     */
    public void tryMakeTurn(Turn turn) {
        synchronized (currentTurn) {
            currentTurn.setTurn(turn);
            currentTurn.notify();
        }
    }

    /**
     * Returns the current waiting player or null if nobody's waiting.
     */
    @Nullable
    public static HumanPlayer getCurrentWaitingPlayer() {
        return currentWaitingPlayer;
    }
}
