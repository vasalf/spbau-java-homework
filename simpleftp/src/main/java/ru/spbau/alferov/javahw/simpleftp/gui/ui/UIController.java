package ru.spbau.alferov.javahw.simpleftp.gui.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.alferov.javahw.simpleftp.gui.filetree.RemoteFileNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is the JavaFX UI controller.
 */
public class UIController {
    /**
     * This is the primary stage.
     */
    private Stage stage;

    /**
     * This is the connection scene.
     */
    private Scene connectionScene;

    /**
     * This inits the connection scene.
     */
    private void setUpConnectionScene() throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("connection.fxml"));
        connectionScene = new Scene(root, 400, 120);
    }

    /**
     * This is the remote scene.
     */
    private Scene remoteScene;

    /**
     * This is the TreeView showing the remote files.
     */
    private TreeView remoteFiles;

    /**
     * This inits the remote scene.
     */
    private void setUpRemoteScene() throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("remote.fxml"));
        remoteScene = new Scene(root, 600, 450);
        remoteFiles = (TreeView)remoteScene.lookup("#files");
    }

    /**
     * This creates the controller.
     */
    public UIController(@NotNull Stage primaryStage) throws IOException {
        stage = primaryStage;
        stage.setTitle("SimpleFTP");

        setUpConnectionScene();
        setUpRemoteScene();

        stage.setScene(connectionScene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * This indicates an error.
     */
    public void showErrorMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);

        alert.showAndWait();
    }

    /**
     * This refreshes the represented file system.
     */
    public void setRemoteFiles(RemoteFileNode root) {
        TreeItem<String> treeRoot = TreeViewBuilder.build(root);
        remoteFiles.setRoot(treeRoot);
    }

    /**
     * This switches to the remote scene.
     */
    public void showRemoteScene() {
        stage.setScene(remoteScene);
        stage.setMinHeight(400);
        stage.setMinWidth(600);
        stage.setResizable(true);
    }

    /**
     * This returns the path to the currently selected item.
     * @return null if no item is selected, the path otherwise.
     */
    @Nullable
    public String getSelectedItemPath() {
        TreeItem<String> item = (TreeItem<String>)remoteFiles.getFocusModel().getFocusedItem();
        if (item == null) {
            return null;
        } else {
            List<String> path = new ArrayList<>();
            while (item != null) {
                path.add(item.getValue());
                item = item.getParent();
            }
            Collections.reverse(path);
            StringBuilder sb = new StringBuilder();
            for (String s : path) {
                sb.append('/');
                sb.append(s);
            }
            return sb.toString();
        }
    }

    /**
     * This asks the user where to save the file.
     */
    @Nullable
    public File selectWhereToSaveFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Download file");
        return fileChooser.showSaveDialog(stage);
    }
}
