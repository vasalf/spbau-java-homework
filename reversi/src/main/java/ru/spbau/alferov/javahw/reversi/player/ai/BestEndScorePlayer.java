package ru.spbau.alferov.javahw.reversi.player.ai;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.Turn;
import ru.spbau.alferov.javahw.reversi.player.Player;
import ru.spbau.alferov.javahw.reversi.player.ai.function.PositionEvaluationFunction;
import ru.spbau.alferov.javahw.reversi.player.ai.function.ScoreEvaluation;

/**
 * This is the player which makes some another player playing better.
 * Basically it plays as the wrapped player but in the last eight turns
 * it switches to alpha-beta evaluation giving the best end score.
 */
public class BestEndScorePlayer extends AIPlayer {
    /**
     * The wrapped player.
     */
    private @NotNull AIPlayer startAndMiddlePlayer;

    /**
     * The player used in the end.
     */
    private @NotNull AlphaBetaPrunningPlayer endPlayer;

    /**
     * Constructs a new player wrapping the given.
     */
    public BestEndScorePlayer(@NotNull AIPlayer player) {
        startAndMiddlePlayer = player;
        endPlayer = new AlphaBetaPrunningPlayer(new ScoreEvaluation());
    }

    /**
     * {@link Player#getName()}
     */
    @Override
    public String getName() {
        return startAndMiddlePlayer.getName() + " + best score in the end";
    }

    /**
     * {@link Player#makeTurn(Field)}
     */
    @Override
    public @NotNull Turn makeTurn(@NotNull Field field) throws GameInterruptedException {
        if (field.getBlackScore() + field.getWhiteScore() + endPlayer.getMaxDepth() >= 64)
            return endPlayer.makeTurn(field);
        else
            return startAndMiddlePlayer.makeTurn(field);
    }
}
