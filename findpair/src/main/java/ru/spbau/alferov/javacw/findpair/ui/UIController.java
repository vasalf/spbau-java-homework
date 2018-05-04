package ru.spbau.alferov.javacw.findpair.ui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javacw.findpair.FindPairApplication;
import ru.spbau.alferov.javacw.findpair.logic.LogicController;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UIController {
    public class Square {
        private final int x, y;
        private int number;

        public Square(int i, int j) {
            x = i;
            y = j;
            number = FindPairApplication.getInstance().getLogic().getCell(i, j);
            element = new Button();
            element.setMinSize(40, 40);
            element.setMaxSize(40, 40);
            element.setOnMouseClicked(event -> {
                if (clicksEnabled) {
                    FindPairApplication.getInstance().getLogic().clickCell(x, y);
                }
            });
        }

        private Button element;

        public void showNumber() {
            element.setText(String.valueOf(number));
            shownButNotDisabledSet.add(this);
        }

        public void setDeactivated() {
            showNumber();
            element.setDisable(true);
            shownButNotDisabledSet.remove(this);
        }
    }

    private Square[][] squares;

    @NotNull
    private Set<Square> shownButNotDisabledSet = new HashSet<>();

    @NotNull
    public Square getButton(int i, int j) {
        return squares[i][j];
    }

    @NotNull
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    boolean clicksEnabled = true;

    public void disableClicks() {
        clicksEnabled = false;
    }

    public void enableClicks() {
        clicksEnabled = true;
    }

    public void waitAndHide() {
        executor.schedule(() -> {
            Platform.runLater(() -> {
                for (Square s : shownButNotDisabledSet) {
                    s.element.setText("");
                }
                shownButNotDisabledSet.clear();
                enableClicks();
            });
        }, 1, TimeUnit.SECONDS);
    }

    public void setWin() {
        winLabel.setText("You win!");
    }

    private Stage stage;

    private Scene mainScene;

    private Label winLabel;

    private void setUpScene(@NotNull LogicController logic) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("mainScene.fxml"));
        int fieldSize = logic.getFieldSize();
        mainScene = new Scene(root, 60 * (fieldSize + 2), 60 * (fieldSize + 3));
        GridPane layout = (GridPane)mainScene.lookup("#layout");
        squares = new Square[fieldSize][fieldSize];
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                squares[i][j] = new Square(i, j);
                layout.add(squares[i][j].element, i, j);
                GridPane.setHalignment(squares[i][j].element, HPos.CENTER);
            }
        }
        layout.add(new Label(""), 0, fieldSize, fieldSize, 1);
        winLabel = new Label("The game is in progress.");
        layout.add(winLabel, 0, fieldSize + 1, fieldSize, 1);
        GridPane.setHalignment(winLabel, HPos.CENTER);
    }

    public UIController(@NotNull Stage primaryStage, @NotNull LogicController logic) throws IOException {
        stage = primaryStage;
        setUpScene(logic);

        stage.setOnCloseRequest(t -> {
            executor.shutdown();
            Platform.exit();
            System.exit(0);
        });

        stage.setTitle("Find pair");
        stage.setScene(mainScene);
        stage.setResizable(false);
        stage.show();
    }
}
