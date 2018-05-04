package ru.spbau.alferov.javacw.findpair;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javacw.findpair.logic.LogicController;
import ru.spbau.alferov.javacw.findpair.ui.UIController;

public class FindPairApplication extends Application {
    private static FindPairApplication instance;

    private LogicController logic;

    private UIController ui;

    private static class InvalidLaunchArgumentException extends Exception {
        public InvalidLaunchArgumentException(String message) {
            super(message);
        }

        public InvalidLaunchArgumentException(String message, Throwable reason) {
            super(message, reason);
        }
    }

    private void setUpLogic() throws InvalidLaunchArgumentException {
        Application.Parameters parameters = getParameters();
        if (parameters == null) {
           throw new InvalidLaunchArgumentException("This program should be launched from jar file, not from IDE.");
        }
        if (parameters.getUnnamed().isEmpty()) {
            throw new InvalidLaunchArgumentException("Couldn't find the field size in the command line arguments");
        }
        String fieldSize = parameters.getUnnamed().get(0);
        int parsedFieldSize;
        try {
            parsedFieldSize = Integer.parseInt(fieldSize);
        } catch (NumberFormatException e) {
            throw new InvalidLaunchArgumentException("Could not parse the field size from string \"" + fieldSize + "\"", e);
        }
        if (parsedFieldSize % 2 == 1) {
            throw new InvalidLaunchArgumentException("The field size should be an even number.");
        }
        logic = new LogicController(parsedFieldSize);
    }

    @Override
    public void start(@NotNull Stage primaryStage) throws Exception {
        instance = this;
        setUpLogic();
        ui = new UIController(primaryStage, logic);
    }

    public static FindPairApplication getInstance() {
        return instance;
    }

    public LogicController getLogic() {
        return logic;
    }

    public UIController getUI() {
        return ui;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
