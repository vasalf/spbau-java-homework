package ru.spbau.alferov.javahw.reversi.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import ru.spbau.alferov.javahw.reversi.ReversiApplication;

/**
 * This is the FXML controller for the statistics viewer.
 */
public class StatisticsFXMLController {
    public TableView statsTable;

    public void backToMainScreen(ActionEvent actionEvent) {
        ReversiApplication.getInstance().backToMainMenu();
    }
}
