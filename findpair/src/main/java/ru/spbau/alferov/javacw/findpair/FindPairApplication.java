package ru.spbau.alferov.javacw.findpair;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javacw.findpair.logic.LogicController;
import ru.spbau.alferov.javacw.findpair.ui.UIController;

/**
 * This is the JavaFX Application of the game.
 *
 * The application should be built with gradle :jar task.
 * The jar file should be launched with a single comand line argument.
 * It is the field size. This should be an even integer greater than or equal to 4.
 */
public class FindPairApplication extends Application {
    /**
     * Instance of the application.
     */
    private static FindPairApplication instance;

    /**
     * The logic controller.
     */
    private LogicController logic;

    /**
     * The UI controller.
     */
    private UIController ui;

    /**
     * This is thrown if the arguments aren't set up properly.
     */
    private static class InvalidLaunchArgumentException extends Exception {
        public InvalidLaunchArgumentException(String message) {
            super(message);
        }

        public InvalidLaunchArgumentException(String message, Throwable reason) {
            super(message, reason);
        }
    }

    /**
     * This creates a logic controller.
     * @throws InvalidLaunchArgumentException
     */
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
        if (parsedFieldSize < 4) {
            throw new InvalidLaunchArgumentException("The field size should be greater than or equal to 4.");
        }
        logic = new LogicController(parsedFieldSize);
    }

    /**
     * The start method of the FX Application.
     */
    @Override
    public void start(@NotNull Stage primaryStage) throws Exception {
        instance = this;
        setUpLogic();
        ui = new UIController(primaryStage, logic);
    }

    /**
     * Gets the instance of the application.
     */
    public static FindPairApplication getInstance() {
        return instance;
    }

    /**
     * Gets the instance of the logic controller.
     */
    public LogicController getLogic() {
        return logic;
    }

    /**
     * Gets the instance of the UI controller.
     */
    public UIController getUI() {
        return ui;
    }

    /**
     * Launches the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
