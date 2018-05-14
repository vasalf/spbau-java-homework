package ru.spbau.alferov.javahw.simpleftp.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.spbau.alferov.javahw.simpleftp.gui.logic.Controller;
import ru.spbau.alferov.javahw.simpleftp.gui.ui.UIController;

/**
 * This is the JavaFX GUI for SimpleFTP protocol.
 */
public class SimpleFTPApplication extends Application {
    /**
     * This is the instance of the application.
     */
    private static SimpleFTPApplication instance;

    /**
     * This is the UI controller used by the application.
     */
    private UIController ui;

    /**
     * This is the logic controller used by the application.
     */
    private Controller controller;

    /**
     * @return Instance of the application.
     */
    public static SimpleFTPApplication getInstance() {
        return instance;
    }

    /**
     * @return The UI controller.
     */
    public UIController getUi() {
        return ui;
    }

    /**
     * @return The logic controller
     */
    public Controller getController() {
        return controller;
    }

    /**
     * This starts the application and initializes the controllers.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        ui = new UIController(primaryStage);
        controller = new Controller();
    }

    /**
     * This launches the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
