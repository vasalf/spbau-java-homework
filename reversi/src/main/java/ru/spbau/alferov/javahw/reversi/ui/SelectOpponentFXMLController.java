package ru.spbau.alferov.javahw.reversi.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import ru.spbau.alferov.javahw.reversi.ReversiApplication;

/**
 * This is the FXML controller for the opponent selection screen.
 */
public class SelectOpponentFXMLController {
    public ChoiceBox<String> selectOpponentChoice;
    public RadioButton blackSide;
    public RadioButton whiteSide;
    public Button newGame;

    public void startGame(ActionEvent actionEvent) {
        ReversiApplication.getInstance().startSinglePlayerGameOpposing(selectOpponentChoice.getValue(), blackSide.isSelected());
    }
}
