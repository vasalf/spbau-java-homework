package ru.spbau.alferov.javahw.reversi.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ru.spbau.alferov.javahw.reversi.ReversiApplication;

public class ClientMenuFXMLController {
    public TextField port;
    public TextField server;
    public Button start;

    public void handleStart(ActionEvent event) {
        ReversiApplication.getInstance().createClient(server.getText(), port.getText());
    }
}
