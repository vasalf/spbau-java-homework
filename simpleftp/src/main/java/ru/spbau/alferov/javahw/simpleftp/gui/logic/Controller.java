package ru.spbau.alferov.javahw.simpleftp.gui.logic;

import ru.spbau.alferov.javahw.simpleftp.client.ClientException;
import ru.spbau.alferov.javahw.simpleftp.client.SimpleFTPClient;
import ru.spbau.alferov.javahw.simpleftp.gui.SimpleFTPApplication;
import ru.spbau.alferov.javahw.simpleftp.gui.filetree.RemoteFileNode;
import ru.spbau.alferov.javahw.simpleftp.gui.filetree.VisitorException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This controls the logic of the application.
 */
public class Controller {
    /**
     * This is host to which the application is connected.
     */
    private String host;

    /**
     * This is the port to which the application is connected.
     */
    private int port;

    /**
     * This refreshes the tree shown at the remote screen.
     */
    public void refresh() {
        SimpleFTPClient client = new SimpleFTPClient(host, port);

        try {
            client.connect();
        } catch (ClientException e) {
            SimpleFTPApplication.getInstance().getUi().showErrorMessage("Connection error", e.getMessage());
            return;
        }

        RemoteFileNode root;
        try {
            root = new FileTreeBuilder(client).build();
        } catch (VisitorException e) {
            SimpleFTPApplication.getInstance().getUi().showErrorMessage("Connection error", e.getMessage());
            return;
        }

        try {
            client.disconnect();
        } catch (ClientException e) {
            SimpleFTPApplication.getInstance().getUi().showErrorMessage("Connection error", e.getMessage());
            return;
        }

        SimpleFTPApplication.getInstance().getUi().setRemoteFiles(root);
    }

    /**
     * This performes the first connection to the server.
     */
    public void connect(String remoteHost, String remotePort) {
        host = remoteHost;
        try {
            port = Integer.parseInt(remotePort);
        } catch (NumberFormatException e) {
            SimpleFTPApplication.getInstance().getUi().showErrorMessage("Connection error", "Could not parse the port " + remotePort);
            return;
        }

        refresh();

        SimpleFTPApplication.getInstance().getUi().showRemoteScene();
    }

    /**
     * This downloads a remote file.
     *
     * @param path Path on remote host.
     * @param file Local file to be saved.
     */
    public void save(String path, File file) {
        SimpleFTPClient client = new SimpleFTPClient(host, port);

        try {
            client.connect();
        } catch (ClientException e) {
            SimpleFTPApplication.getInstance().getUi().showErrorMessage("Connection error", e.getMessage());
            return;
        }

        byte[] content;
        try {
            content = client.get(path);
            client.disconnect();
        } catch (ClientException e) {
            SimpleFTPApplication.getInstance().getUi().showErrorMessage("Connection error", e.getMessage());
            return;
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(content);
            outputStream.close();
        } catch (FileNotFoundException e) {
            SimpleFTPApplication.getInstance().getUi().showErrorMessage("File not found!", e.getMessage());
        } catch (IOException e) {
            SimpleFTPApplication.getInstance().getUi().showErrorMessage("I/O error", e.getMessage());
        }
    }
}
