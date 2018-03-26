package ru.spbau.alferov.javahw.reversi.player.ai;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.SquareType;
import ru.spbau.alferov.javahw.reversi.logic.Turn;
import ru.spbau.alferov.javahw.reversi.player.Player;

import javax.xml.ws.soap.MTOM;
import java.util.List;
import java.util.Random;

/**
 * This player performs the random turns from the set of available turns.
 */
public class RandomPlayer extends AIPlayer {
    /**
     * Random generator used by the player.
     */
    private @NotNull Random random = new Random();

    /**
     * {@link Player#makeTurn(Field)}
     */
    @Override
    public @NotNull Turn makeTurn(@NotNull Field field) {
        List<Turn> turns = field.getAllowedTurns(field.isBlackTurn() ? SquareType.BLACK : SquareType.WHITE);
        return turns.get(random.nextInt(turns.size()));
    }

    /**
     * {@link Player#getName()}
     */
    @Override
    public String getName() {
        return "Random";
    }
}
