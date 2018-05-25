package ru.spbau.alferov.javahw.reversi.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import ru.spbau.alferov.javahw.reversi.ReversiApplication;

public class NetworkMenuFXMLController {
    public Button becomeServer;
    public Button connectToServer;

    public void handleBecomeServerButtonAction(ActionEvent event) {
        ReversiApplication.getInstance().showServerMenu();
    }

    public void handleConnectToServerAction(ActionEvent event) {
        ReversiApplication.getInstance().showClientMenu();
    }
}
