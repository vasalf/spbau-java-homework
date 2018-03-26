package ru.spbau.alferov.javahw.reversi.ui;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.ReversiApplication;
import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.Game;
import ru.spbau.alferov.javahw.reversi.logic.SquareType;
import ru.spbau.alferov.javahw.reversi.logic.Turn;
import ru.spbau.alferov.javahw.reversi.player.HumanPlayer;
import ru.spbau.alferov.javahw.reversi.stats.PlayedGame;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This is the basic UI controller.
 */
public class ReversiUI {
    /**
     * This class is representing a square from the field on the game screen.
     */
    private static class Square {
        /**
         * There the images for the square types are stored.
         */
        private static final Image emptyImage = new Image("empty.png");
        private static final Image blackImage = new Image("black.png");
        private static final Image whiteImage = new Image("white.png");

        /**
         * This is the ImageView for the square.
         */
        private ImageView element;

        /**
         * These are the coordinates for the square (top-left is 0, 0)
         */
        private int row, column;

        /**
         * This is the function to be called whenever the ImageView is clicked.
         */
        private void onClickAction() {
            HumanPlayer player = HumanPlayer.getCurrentWaitingPlayer();
            if (player != null) {
                player.tryMakeTurn(new Turn(7 - row, column));
            }
        }

        /**
         * The basic constructor.
         */
        public Square(int r, int c) {
            row = r;
            column = c;
            element = new ImageView(emptyImage);
            element.setOnMouseClicked(e -> onClickAction());
        }

        /**
         * Sets the image for the square.
         */
        public void setType(SquareType type) {
            if (type == SquareType.EMPTY) {
                element.setImage(emptyImage);
            } else if (type == SquareType.BLACK) {
                element.setImage(blackImage);
            } else {
                element.setImage(whiteImage);
            }
        }
    }

    /**
     * This is the JavaFX Stage.
     */
    @NotNull
    private Stage stage;

    /**
     * This is the start menu scene.
     */
    @NotNull
    private Scene startMenu;

    /**
     * This is the opponent selection scene.
     */
    @NotNull
    private Scene selectOpponentScene;

    /**
     * This is the game scene.
     */
    @NotNull
    private Scene gameScreen;

    /**
     * This is the statistics viewer scene.
     */
    @NotNull
    private Scene statsScreen;

    /**
     * This is the statistics table.
     */
    private @NotNull TableView<PlayedGame> statsTable;

    /**
     * This is the array for the field on game screen.
     */
    @NotNull
    private Square[][] squares;

    /**
     * This initializes the start menu scene,
     */
    private void initStartMenu() throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("startMenu.fxml"));
        startMenu = new Scene(root, 680, 480);
    }

    /**
     * This initializes the opponent selection scene.
     */
    private void initSelectOpponentScene() throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("selectOpponent.fxml"));
        selectOpponentScene = new Scene(root, 680, 480);

        RadioButton black = (RadioButton)selectOpponentScene.lookup("#blackSide");
        RadioButton white = (RadioButton)selectOpponentScene.lookup("#whiteSide");
        ToggleGroup selGroup = new ToggleGroup();
        black.setToggleGroup(selGroup);
        white.setToggleGroup(selGroup);
        black.fire();

        List<String> items = ReversiApplication.getInstance()
                .getAiStorage()
                .getPlayers()
                .entrySet()
                .stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        ObservableList<String> observableList = FXCollections.observableList(items);
        ChoiceBox<String> choiceBox = (ChoiceBox<String>)selectOpponentScene.lookup("#selectOpponentChoice");
        choiceBox.setItems(observableList);
    }

    /**
     * This initializes the game screen.
     */
    private void initGameScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gameScreen.fxml"));
        gameScreen = new Scene(root, 680, 480);
        gameScreen.getStylesheets().add("gameScreen.css");
        GridPane field = (GridPane)gameScreen.lookup("#field");
        for (int col = 0; col < 8; col++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setMinWidth(40);
            columnConstraints.setMaxWidth(40);
            columnConstraints.setPrefWidth(40);
            columnConstraints.setHgrow(Priority.NEVER);
            field.getColumnConstraints().add(columnConstraints);
        }
        squares = new Square[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = new Square(row, col);
                field.add(squares[row][col].element, col, row);
                GridPane.setHalignment(squares[row][col].element, HPos.CENTER);
            }
        }
    }

    /**
     * This is the class for statistics columns created to avoid the code
     * repetition in {@link #initStatistics()}.
     *
     * @param <T> Type to be displayed.
     */
    private static class StatisticsTableColumn<T> extends TableColumn<PlayedGame, T> {
        public StatisticsTableColumn(String name,
                                     int width,
                                     Callback<CellDataFeatures<PlayedGame, T>, ObservableValue<T>> callback) {
            super(name);
            super.setCellValueFactory(callback);
            super.setMinWidth(width);
            super.setPrefWidth(width);
            super.setMaxWidth(width);
        }
    }

    /**
     * This initializes the statistics screen.
     */
    @SuppressWarnings("unchecked")
    private void initStatistics() throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("statistics.fxml"));
        statsScreen = new Scene(root, 680, 480);
        statsTable = (TableView<PlayedGame>)statsScreen.lookup("#statsTable");

        StatisticsTableColumn<String> blackNameCol = new StatisticsTableColumn<>("Black player", 180,
                p -> new SimpleStringProperty(p.getValue().getBlackPlayerName()));
        StatisticsTableColumn<String> whiteNameCol = new StatisticsTableColumn<>("White player", 180,
                p -> new SimpleStringProperty(p.getValue().getWhitePlayerName()));
        StatisticsTableColumn<Integer> blackScoreCol = new StatisticsTableColumn<>("Black", 70,
                p -> new SimpleIntegerProperty(p.getValue().getBlackPlayerScore()).asObject());
        StatisticsTableColumn<Integer> whiteScoreCol = new StatisticsTableColumn<>("White", 70,
                p -> new SimpleIntegerProperty(p.getValue().getWhitePlayerScore()).asObject());
        statsTable.getColumns().setAll(blackNameCol, whiteNameCol, blackScoreCol, whiteScoreCol);
    }

    /**
     * This initializes the overall stage.
     */
    private void initStage() throws IOException {
        stage.setTitle("Reversi");
        stage.setScene(startMenu);
        stage.show();
    }

    /**
     * This constructor initalizes everything.
     */
    public ReversiUI(@NotNull Stage primaryStage) throws IOException {
        stage = primaryStage;
        initStartMenu();
        initSelectOpponentScene();
        initGameScreen();
        initStatistics();
        initStage();
    }

    /**
     * This switches the scene to the opponent selection scene.
     */
    public void switchToOpponentSelection() {
        stage.setScene(selectOpponentScene);
    }

    /**
     * This switches the scene to the game scene.
     */
    public void switchToGameScreen() {
        stage.setScene(gameScreen);
    }

    /**
     * This switches the scene to the main menu scene.
     */
    public void switchToMainMenu() {
        stage.setScene(startMenu);
    }

    /**
     * This swtches the scene to the statistics scene.
     */
    public void switchToStatsScreen() {
        stage.setScene(statsScreen);
    }

    /**
     * Routine function called wherever it is necessary to set the label text.
     */
    private void setLabelText(Label label, String text) {
        Platform.runLater(() -> label.setText(text));
    }

    /**
     * This updates the game shown on the game screen.
     */
    public void displayField(Field field, Game game) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setType(field.getSquare(7 - row, col));
            }
        }

        Label blackScoreLabel = (Label)gameScreen.lookup("#blackScore");
        Label whiteScoreLabel = (Label)gameScreen.lookup("#whiteScore");
        setLabelText(blackScoreLabel, "Black: " + field.getBlackScore());
        setLabelText(whiteScoreLabel, "White: " + field.getWhiteScore());

        Label gameStatusLabel = (Label)gameScreen.lookup("#gameStatus");
        if (game.getResult() == Game.Result.IN_PROGRESS) {
            if (field.isBlackTurn()) {
                setLabelText(gameStatusLabel, "Black's turn.");
            } else {
                setLabelText(gameStatusLabel, "White's turn");
            }
        } else if (game.getResult() == Game.Result.BLACK_WINS) {
            setLabelText(gameStatusLabel, "Black wins!");
        } else if (game.getResult() == Game.Result.WHITE_WINS) {
            setLabelText(gameStatusLabel, "White wins!");
        } else {
            setLabelText(gameStatusLabel, "Draw.");
        }
    }

    /**
     * This updates the stats list.
     */
    public void updateStatsList(List<PlayedGame> stats) {
        statsTable.setItems(FXCollections.observableList(stats));
    }
}
