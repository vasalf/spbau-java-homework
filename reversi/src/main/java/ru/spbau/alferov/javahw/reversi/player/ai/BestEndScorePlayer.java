package ru.spbau.alferov.javahw.reversi.player.ai;

import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.Turn;
import ru.spbau.alferov.javahw.reversi.player.Player;
import ru.spbau.alferov.javahw.reversi.player.ai.function.PositionEvaluationFunction;
import ru.spbau.alferov.javahw.reversi.player.ai.function.ScoreEvaluation;

public class BestEndScorePlayer extends AIPlayer {
    private AIPlayer startAndMiddlePlayer;
    private AlphaBetaPrunningPlayer endPlayer;

    public BestEndScorePlayer(AIPlayer player) {
        startAndMiddlePlayer = player;
        endPlayer = new AlphaBetaPrunningPlayer(new ScoreEvaluation());
    }

    @Override
    public String getName() {
        return startAndMiddlePlayer.getName() + " + best score in the end";
    }

    @Override
    public Turn makeTurn(Field field) throws GameInterruptedException {
        if (field.getBlackScore() + field.getWhiteScore() + endPlayer.getMaxDepth() >= 64)
            return endPlayer.makeTurn(field);
        else
            return startAndMiddlePlayer.makeTurn(field);
    }
}
