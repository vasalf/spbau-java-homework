package ru.spbau.alferov.javahw.reversi.player;

import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.Turn;

public abstract class Player {
    public static class GameInterruptedException extends Exception {
        public GameInterruptedException() {
            super("Uncaught game interruption happened");
        }
    }
    public abstract Turn makeTurn(Field field) throws GameInterruptedException;
    public abstract String getName();
}
