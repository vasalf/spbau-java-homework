package ru.spbau.alferov.javacw.findpair.ui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This is the main JavaFX UI Controller.
 */
public class UIController {
    /**
     * This represents a cell in the game field.
     */
    public class Square {
        /**
         * Position in the pane.
         */
        private final int x, y;

        /**
         * The hidden number on the square.
         */
        private int number;

        /**
         * Constructs a cell.
         */
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

        /**
         * The button which the class represents.
         */
        private Button element;

        /**
         * This shows the number on the square.
         */
        public void showNumber() {
            element.setText(String.valueOf(number));
            shownButNotDisabledSet.add(this);
        }

        /**
         * This marks the square as a guessed one.
         */
        public void setDeactivated() {
            showNumber();
            element.setDisable(true);
            shownButNotDisabledSet.remove(this);
        }
    }

    /**
     * Squares on the field.
     */
    private Square[][] squares;

    /**
     * Set of the squares which should be hidden after a while.
     */
    @NotNull
    private Set<Square> shownButNotDisabledSet = new HashSet<>();

    /**
     * Getter for {@link #squares}.
     */
    @NotNull
    public Square getButton(int i, int j) {
        return squares[i][j];
    }

    /**
     * The executor for hiding the sown squares.
     */
    @NotNull
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    /**
     * This represents whether click events on the buttons are accepted.
     */
    boolean clicksEnabled = true;

    /**
     * Setter for {@link #clicksEnabled}.
     */
    public void disableClicks() {
        clicksEnabled = false;
    }

    /**
     * Setter for {@link #clicksEnabled}.
     */
    public void enableClicks() {
        clicksEnabled = true;
    }

    /**
     * This disables the click events, waits for 1 second and then hides the shown
     * non-guessed squares and enables the click events back.
     */
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

    /**
     * This sets the win label text to "You win!".
     */
    public void setWin() {
        winLabel.setText("You win!");
    }

    /**
     * This is the primary stage.
     */
    private Stage stage;

    /**
     * This is the only scene of the application.
     */
    private Scene mainScene;

    /**
     * This is the win label.
     */
    private Label winLabel;

    /**
     * This initializes the elements of the main scene.
     */
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

    /**
     * Constructs the controller.
     */
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
