package ru.spbau.alferov.javahw.reversi.player;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.Turn;

/**
 * The base class for all of the players.
 */
public abstract class Player {
    /**
     * Exception thrown when the game is somehow interrupted (basically this
     * happens when the new game is started).
     */
    public static class GameInterruptedException extends Exception {
        /**
         * Constructs the exception.
         */
        public GameInterruptedException() {
            super("Uncaught game interruption happened");
        }
    }

    /**
     * This function asks player which turn would he make in this field.
     */
    public abstract @NotNull Turn makeTurn(Field field) throws GameInterruptedException;

    /**
     * The name of the player (used by UI to be displayed on the opponent
     * selection screen).
     */
    public abstract String getName();
}
