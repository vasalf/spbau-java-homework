package ru.spbau.alferov.javahw.simpleftp.gui.ui;

import org.jetbrains.annotations.Nullable;
import ru.spbau.alferov.javahw.simpleftp.gui.SimpleFTPApplication;

import java.io.File;

/**
 * This is a remote screen FXML controller.
 */
public class RemoteScreenFXMLController {
    /**
     * This is called any time Refresh button is pressed.
     */
    public void refresh() {
        SimpleFTPApplication.getInstance().getController().refresh();
    }

    /**
     * This is called any time Save button is pressed.
     */
    public void save() {
        @Nullable String path = SimpleFTPApplication.getInstance().getUi().getSelectedItemPath();
        if (path != null) {
            File file = SimpleFTPApplication.getInstance().getUi().selectWhereToSaveFile();
            if (file != null) {
                SimpleFTPApplication.getInstance().getController().save(path, file);
            }
        }
    }
}
