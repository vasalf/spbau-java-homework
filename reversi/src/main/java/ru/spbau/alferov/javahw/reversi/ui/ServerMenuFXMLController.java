package ru.spbau.alferov.javahw.reversi.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import ru.spbau.alferov.javahw.reversi.ReversiApplication;

public class ServerMenuFXMLController {
    public RadioButton whiteSide;
    public RadioButton blackSide;
    public Button start;
    public Label status;
    public TextField port;

    public void handleStart(ActionEvent event) {
        status.setText("Connecting...");
        ReversiApplication.getInstance().createServer(port.getText(), blackSide.isSelected());
        start.setDisable(true);
    }
}
