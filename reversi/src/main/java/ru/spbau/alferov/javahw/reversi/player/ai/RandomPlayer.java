package ru.spbau.alferov.javahw.reversi.player.ai;

import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.SquareType;
import ru.spbau.alferov.javahw.reversi.logic.Turn;

import java.util.List;
import java.util.Random;

public class RandomPlayer extends AIPlayer {
    Random random = new Random();
    @Override
    public Turn makeTurn(Field field) throws GameInterruptedException {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            throw new GameInterruptedException();
        }
        List<Turn> turns = field.getAllowedTurns(field.isBlackTurn() ? SquareType.BLACK : SquareType.WHITE);
        return turns.get(random.nextInt(turns.size()));
    }

    @Override
    public String getName() {
        return "Random";
    }
}
