package ru.spbau.alferov.javahw.simpleftp.gui.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ru.spbau.alferov.javahw.simpleftp.gui.SimpleFTPApplication;

/**
 * This is an FXML controller for the connection screen.
 */
public class ConnectionScreenFXMLController {
    @FXML
    public TextField host;

    @FXML
    public TextField port;

    /**
     * This is called any time the Connect button is pressed.
     */
    public void connect() {
        SimpleFTPApplication.getInstance().getController().connect(host.getText(), port.getText());
    }
}
