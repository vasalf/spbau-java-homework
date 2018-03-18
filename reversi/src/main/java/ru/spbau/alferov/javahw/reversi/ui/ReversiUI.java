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

public class ReversiUI {
    private static class Square {
        private static final Image emptyImage = new Image("empty.png");
        private static final Image blackImage = new Image("black.png");
        private static final Image whiteImage = new Image("white.png");

        private ImageView element;

        private int row, column;

        private void onClickAction() {
            HumanPlayer player = HumanPlayer.getCurrentWaitingPlayer();
            if (player != null) {
                player.tryMakeTurn(new Turn(7 - row, column));
            }
        }

        public Square(int r, int c) {
            row = r;
            column = c;
            element = new ImageView(emptyImage);
            element.setOnMouseClicked(e -> onClickAction());
        }

        public void setType(SquareType type) {
            if (type == SquareType.EMPTY) {
                element.setImage(emptyImage);
            } else if (type == SquareType.BLACK) {
                element.setImage(blackImage);
            } else {
                element.setImage(whiteImage);
            }
        }

        public String coordinate() {
            return "" + (char)('a' + column) + (char)('8' - row);
        }
    }

    @NotNull
    private Stage stage;
    
    @NotNull
    private Scene startMenu;

    @NotNull
    private Scene selectOpponentScene;

    @NotNull
    private Scene gameScreen;

    @NotNull
    private Scene statsScreen;

    private @NotNull TableView<PlayedGame> statsTable;

    @NotNull
    private Square[][] squares;

    private void initStartMenu() throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("startMenu.fxml"));
        startMenu = new Scene(root, 680, 480);
    }

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

    private void initStage() throws IOException {
        stage.setTitle("Reversi");
        stage.setScene(startMenu);
        stage.show();
    }

    public ReversiUI(@NotNull Stage primaryStage) throws IOException {
        stage = primaryStage;
        initStartMenu();
        initSelectOpponentScene();
        initGameScreen();
        initStatistics();
        initStage();
    }

    public void switchToOpponentSelection() {
        stage.setScene(selectOpponentScene);
    }

    public void switchToGameScreen() {
        stage.setScene(gameScreen);
    }

    public void switchToMainMenu() {
        stage.setScene(startMenu);
    }

    public void switchToStatsScreen() {
        stage.setScene(statsScreen);
    }

    private void setLabelText(Label label, String text) {
        Platform.runLater(() -> label.setText(text));
    }

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

    public void updateStatsList(List<PlayedGame> stats) {
        statsTable.setItems(FXCollections.observableList(stats));
    }
}
