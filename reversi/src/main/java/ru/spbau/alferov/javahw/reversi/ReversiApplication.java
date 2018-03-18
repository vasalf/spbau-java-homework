package ru.spbau.alferov.javahw.reversi;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.Game;
import ru.spbau.alferov.javahw.reversi.player.HumanPlayer;
import ru.spbau.alferov.javahw.reversi.player.Player;
import ru.spbau.alferov.javahw.reversi.player.ai.AIStorage;
import ru.spbau.alferov.javahw.reversi.player.ai.AlphaBetaPrunningPlayer;
import ru.spbau.alferov.javahw.reversi.player.ai.BestEndScorePlayer;
import ru.spbau.alferov.javahw.reversi.player.ai.RandomPlayer;
import ru.spbau.alferov.javahw.reversi.player.ai.function.CornersEvaluation;
import ru.spbau.alferov.javahw.reversi.player.ai.function.NumberOfTurnsEvaluation;
import ru.spbau.alferov.javahw.reversi.player.ai.function.ScoreEvaluation;
import ru.spbau.alferov.javahw.reversi.stats.PlayedGame;
import ru.spbau.alferov.javahw.reversi.stats.StatisticsController;
import ru.spbau.alferov.javahw.reversi.ui.ReversiUI;

import java.io.IOException;
import java.util.List;

public class ReversiApplication extends Application {

    public static void main(String[] args) {
        launch(args);

        if (getInstance().currentGameHolder.getGame() != null) {
            getInstance().currentGameThread.interrupt();
            try {
                getInstance().currentGameThread.join();
            } catch (InterruptedException ie) {
                // Do nothing
            }
        }
    }

    private ReversiUI ui;

    private AIStorage aiStorage = new AIStorage();

    private StatisticsController stats = new StatisticsController();

    private static ReversiApplication instance = null;

    public static ReversiApplication getInstance() {
        return instance;
    }

    private void initAIPlayers() {
        aiStorage.registerAIPlayer(new RandomPlayer());
        aiStorage.registerAIPlayer(new AlphaBetaPrunningPlayer(new ScoreEvaluation()));
        aiStorage.registerAIPlayer(new AlphaBetaPrunningPlayer(new NumberOfTurnsEvaluation()));
        aiStorage.registerAIPlayer(new AlphaBetaPrunningPlayer(new CornersEvaluation()));
        aiStorage.registerAIPlayer(new BestEndScorePlayer(new AlphaBetaPrunningPlayer(new NumberOfTurnsEvaluation())));
        aiStorage.registerAIPlayer(new BestEndScorePlayer(new AlphaBetaPrunningPlayer(new CornersEvaluation())));
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        instance = this;
        aiStorage = new AIStorage();
        initAIPlayers();
        ui = new ReversiUI(primaryStage);
    }

    public void startSinglePlayerGame() {
        ui.switchToOpponentSelection();
    }

    public void startSinglePlayerGameOpposing(String opponent, boolean isHumanBlack) {
        ui.switchToGameScreen();
        if (isHumanBlack) {
            startNewGame(new HumanPlayer(), aiStorage.getPlayers().get(opponent));
        } else {
            startNewGame(aiStorage.getPlayers().get(opponent), new HumanPlayer());
        }
    }

    public void startMultiPlayerGame() {
        ui.switchToGameScreen();
        newMultiplayerGame();
    }

    public Thread currentGameThread = null;

    private void interruptCurrentGame() {
        if (currentGameHolder.getGame() != null) {
            synchronized (currentGameHolder) {
                if (currentGameHolder.getGame() != null) {
                    try {
                        currentGameThread.interrupt();
                        currentGameThread.join();
                    } catch (InterruptedException ie) {
                        throw new RuntimeException("Interrupted while waiting for game thread to join", ie);
                    }
                }
            }
        }
    }

    public void backToMainMenu() {
        interruptCurrentGame();
        ui.switchToMainMenu();
    }

    public void startFromBeginning() {
        Player black = currentGameHolder.getGame().getBlackPlayer();
        Player white = currentGameHolder.getGame().getWhitePlayer();
        interruptCurrentGame();
        startNewGame(black, white);
    }

    public void showStatistics() {
        ui.switchToStatsScreen();
    }

    public static class GameHolder {
        @Nullable
        private Game game = null;

        @Nullable
        public synchronized Game getGame() {
            return game;
        }

        private synchronized void setGame(@Nullable Game game) {
            this.game = game;
        }
    }

    public GameHolder currentGameHolder = new GameHolder();

    public void newMultiplayerGame() {
        interruptCurrentGame();
        startNewGame(new HumanPlayer(), new HumanPlayer());
    }

    private void startNewGame(Player blackPlayer, Player whitePlayer) {
        currentGameHolder.setGame(new Game(blackPlayer, whitePlayer));
        currentGameThread = new Thread(() -> {
            try {
                currentGameHolder.getGame().play();
            } catch (Player.GameInterruptedException exc) {
                // Do nothing
            }
        });
        currentGameThread.start();
    }

    public void updateField(Field field) {
        ui.displayField(field, currentGameHolder.getGame());
    }

    public AIStorage getAiStorage() {
        return aiStorage;
    }

    public StatisticsController getStatsController() {
        return stats;
    }

    public void updateStatsList(List<PlayedGame> stats) {
        ui.updateStatsList(stats);
    }
}
