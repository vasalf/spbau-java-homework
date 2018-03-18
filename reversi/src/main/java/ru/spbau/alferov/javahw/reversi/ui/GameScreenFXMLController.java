package ru.spbau.alferov.javahw.reversi.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import ru.spbau.alferov.javahw.reversi.ReversiApplication;

public class GameScreenFXMLController {
    public GridPane field;
    public GridPane layout;
    public Label blackScore;
    public Label whiteScore;
    public Label gameStatus;
    public Button newGame;
    public Button mainMenu;

    public void newGameAction(ActionEvent actionEvent) {
        ReversiApplication.getInstance().startFromBeginning();
    }

    public void mainMenuAction(ActionEvent actionEvent) {
        ReversiApplication.getInstance().backToMainMenu();
    }
}
