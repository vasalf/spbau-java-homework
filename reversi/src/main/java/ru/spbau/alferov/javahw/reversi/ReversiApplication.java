package ru.spbau.alferov.javahw.reversi;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.Game;
import ru.spbau.alferov.javahw.reversi.net.RemotePlayer;
import ru.spbau.alferov.javahw.reversi.net.ReversiNetworkException;
import ru.spbau.alferov.javahw.reversi.net.client.ReversiClient;
import ru.spbau.alferov.javahw.reversi.net.client.ReversiClientProtocol;
import ru.spbau.alferov.javahw.reversi.net.server.ReversiServer;
import ru.spbau.alferov.javahw.reversi.net.server.ReversiServerProtocol;
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

import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * That is the main controller for the application/
 */
public class ReversiApplication extends Application {
    /**
     * The main function, standard for JavaFX applications, except for
     * joining the game thread in the end.
     */
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
        ReversiServer.interruptExecutor();
        ReversiClient.interruptExecutor();
    }

    /**
     * UI controller for the application.
     */
    private ReversiUI ui;

    /**
     * AI storage for the application.
     */
    private @NotNull AIStorage aiStorage = new AIStorage();

    /**
     * Statistics controller for the application.
     */
    private @NotNull StatisticsController stats = new StatisticsController();

    /**
     * Instance created by JavaFX.
     */
    private static ReversiApplication instance = null;

    /**
     * Gets the main controller instance.
     */
    public static ReversiApplication getInstance() {
        return instance;
    }

    /**
     * Adds all of the written AI players to the AI controller.
     */
    private void initAIPlayers() {
        aiStorage.registerAIPlayer(new RandomPlayer());
        aiStorage.registerAIPlayer(new AlphaBetaPrunningPlayer(new ScoreEvaluation()));
        aiStorage.registerAIPlayer(new AlphaBetaPrunningPlayer(new NumberOfTurnsEvaluation()));
        aiStorage.registerAIPlayer(new AlphaBetaPrunningPlayer(new CornersEvaluation()));
        aiStorage.registerAIPlayer(new BestEndScorePlayer(new AlphaBetaPrunningPlayer(new NumberOfTurnsEvaluation())));
        aiStorage.registerAIPlayer(new BestEndScorePlayer(new AlphaBetaPrunningPlayer(new CornersEvaluation())));
    }

    /**
     * The start function for the JavaFX application.
     * Does all the things the constructor usually do.
     */
    @Override
    public void start(@NotNull Stage primaryStage) throws IOException {
        instance = this;
        aiStorage = new AIStorage();
        initAIPlayers();
        ui = new ReversiUI(primaryStage);
    }

    /**
     * This function should be called whenever the corresponding button in the
     * main menu is pressed.
     */
    public void startSinglePlayerGame() {
        ui.switchToOpponentSelection();
    }

    /**
     * Starts a new game opposing the selected opponent.
     *
     * @param opponent     The selected opponent.
     * @param isHumanBlack True if human wanted to play with black
     */
    public void startSinglePlayerGameOpposing(String opponent, boolean isHumanBlack) {
        ui.switchToGameScreen();
        if (isHumanBlack) {
            startNewGame(new HumanPlayer(), aiStorage.getPlayers().get(opponent));
        } else {
            startNewGame(aiStorage.getPlayers().get(opponent), new HumanPlayer());
        }
    }

    /**
     * Starts a new multiplayer game in the game screen.
     */
    public void startMultiPlayerGame() {
        ui.switchToGameScreen();
        newMultiplayerGame();
    }

    /**
     * Starts a new network game in the game screen.
     *
     * @param isLocalBlack Is local player playing with black?
     * @return The started game
     */
    @NotNull
    public Game startNetworkGame(boolean isLocalBlack, @NotNull RemotePlayer remotePlayer) {
       ui.switchToGameScreen();
       interruptCurrentGame();
       if (isLocalBlack) {
           return startNewGame(new HumanPlayer(), remotePlayer);
       } else {
           return startNewGame(remotePlayer, new HumanPlayer());
       }
    }

    /**
     * This switches to the network menu
     */
    public void startNetworkGame() {
        ui.switchToNetworkMenu();
    }

    /**
     * This switches to the server menu
     */
    public void showServerMenu() {
        ui.switchToServerMenu();
    }

    /**
     * This switches to the client menu
     */
    public void showClientMenu() {
        ui.switchToClientMenu();
    }

    public int parsePortNumber(String port) {
        int portNumber;
        try {
            portNumber = Integer.parseInt(port);
            System.out.println(portNumber);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(ERROR);

            alert.setTitle("Number format error");
            alert.setHeaderText("Could not parse the port number");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

            ui.resetServerScene();
            return -1;
        }
        return portNumber;
    }

    /**
     * This creates a server object
     */
    public void createServer(String port, boolean isLocalBlack) {
        int portNumber = parsePortNumber(port);
        if (portNumber == -1) {
            return;
        }
        ReversiServer server = new ReversiServer(portNumber, new ReversiServerProtocol(isLocalBlack));
        new Thread(() -> {
            try {
                server.run();
            } catch (ReversiNetworkException e) {
                ui.resetServerScene();
            }
        }).start();
    }

    /**
     * This creates a client object
     */
    public void createClient(String server, String serverPort) {
        int portNumber = parsePortNumber(serverPort);
        if (portNumber == -1) {
            return;
        }
        ReversiClient client = new ReversiClient(server, portNumber, new ReversiClientProtocol());
        new Thread(() -> {
            try {
                client.run();
            } catch (ReversiNetworkException e) {
                // Do nothing
            }
        }).start();
    }

    /**
     * This is the thread in which the current game is held (or null
     */
    @Nullable
    private Thread currentGameThread = null;

    /**
     * Function is used whenever we need to stop the current game.
     */
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

    /**
     * This should be called whenever the corresponding button is pressed.
     */
    public void backToMainMenu() {
        interruptCurrentGame();
        ui.switchToMainMenu();
    }

    /**
     * This should be called whenever the corresponding button is pressed.
     */
    public void startFromBeginning() {
        Player black = currentGameHolder.getGame().getBlackPlayer();
        Player white = currentGameHolder.getGame().getWhitePlayer();
        interruptCurrentGame();
        startNewGame(black, white);
    }

    /**
     * This should be called whenever the corresponding button is pressed.
     */
    public void showStatistics() {
        ui.switchToStatsScreen();
    }

    /**
     * This class is used to safely synchronize by game.
     */
    private static class GameHolder {
        /**
         * The current game.
         */
        @Nullable
        private Game game = null;

        /**
         * Getter for the current game.
         */
        @Nullable
        public synchronized Game getGame() {
            return game;
        }

        /**
         * Setter for the current game.
         */
        private synchronized void setGame(@Nullable Game game) {
            this.game = game;
        }
    }

    /**
     * The current game to be synchronized on and stored.
     */
    private final GameHolder currentGameHolder = new GameHolder();

    /**
     * This interrupts the current game and starts a new game.
     */
    private void newMultiplayerGame() {
        interruptCurrentGame();
        startNewGame(new HumanPlayer(), new HumanPlayer());
    }

    /**
     * This starts a new game with given players.
     * The previous game should be interrupted!
     */
    @NotNull
    private Game startNewGame(Player blackPlayer, Player whitePlayer) {
        Game game = new Game(blackPlayer, whitePlayer);
        currentGameHolder.setGame(game);
        currentGameThread = new Thread(() -> {
            try {
                currentGameHolder.getGame().play();
            } catch (Player.GameInterruptedException exc) {
                // Do nothing
            }
        });
        currentGameThread.start();
        return game;
    }

    /**
     * This updates the Field in the UI.
     */
    public void updateField(Field field) {
        ui.displayField(field, currentGameHolder.getGame());
    }

    /**
     * This returns the AIStorage.
     */
    public @NotNull AIStorage getAiStorage() {
        return aiStorage;
    }

    /**
     * That returns the statistics controller.
     */
    public StatisticsController getStatsController() {
        return stats;
    }

    /**
     * That shows a notification about an IO error during a net multiplayer game.
     */
    public void indicateIOException(IOException e) {
        ui.indicateIOException(e);
    }

    /**
     * This updates the stats in the UI.
     */
    public void updateStatsList(List<PlayedGame> stats) {
        ui.updateStatsList(stats);
    }
}
