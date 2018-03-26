package ru.spbau.alferov.javahw.reversi.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import ru.spbau.alferov.javahw.reversi.ReversiApplication;

/**
 * This is the FXML controller for the start menu.
 */
public class StartMenuFXMLController {
    @FXML
    private Button startSinglePlayerButton;

    @FXML
    private Button startMultiPlayerButton;

    @FXML
    private Button statisticsButton;

    public void handleStartSinglePlayerButtonAction(ActionEvent event) {
        ReversiApplication.getInstance().startSinglePlayerGame();
    }

    public void handleStartMultiPlayerButtonAction(ActionEvent event) {
        ReversiApplication.getInstance().startMultiPlayerGame();
    }

    public void handleShowStatisticsButtonAction(ActionEvent event) {
        ReversiApplication.getInstance().showStatistics();
    }
}
