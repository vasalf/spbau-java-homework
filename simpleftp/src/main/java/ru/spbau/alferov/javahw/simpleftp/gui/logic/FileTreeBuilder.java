package ru.spbau.alferov.javahw.simpleftp.gui.logic;

import ru.spbau.alferov.javahw.simpleftp.client.ClientException;
import ru.spbau.alferov.javahw.simpleftp.client.SimpleFTPClient;
import ru.spbau.alferov.javahw.simpleftp.common.FTPFile;
import ru.spbau.alferov.javahw.simpleftp.gui.filetree.*;

import java.io.File;

/**
 * This is a {@link Visitor} implementation which walks through the remote host and creates a file tree.
 */
public class FileTreeBuilder implements Visitor {
    /**
     * The connection used to walk.
     */
    private SimpleFTPClient clientConnection;

    /**
     * This creates a builder.
     */
    public FileTreeBuilder(SimpleFTPClient client) {
        clientConnection = client;
    }

    /**
     * This builds a file tree.
     *
     * @return Root of the file tree.
     */
    public RemoteFileNode build() throws VisitorException {
        RemoteDirectory root = new RemoteDirectory("/", "/");
        visit(root);
        return root;
    }

    /**
     * Helper function which merges two absolute paths into one.
     */
    private String newAbsolutePath(String parent, String child) {
        return new File(parent + "/" + child).toURI().toString().substring("file:///".length());
    }

    /**
     * Visits a remote directory.
     *
     * @throws VisitorException In case of dropped connection.
     */
    @Override
    public void visitDirectory(RemoteDirectory directory) throws VisitorException {
        try {
            FTPFile[] children = clientConnection.list(directory.getAbsolutePath());
            RemoteFileNode[] toSet = new RemoteFileNode[children.length];
            for (int i = 0; i < children.length; i++) {
                if (children[i].isDir()) {
                    toSet[i] = new RemoteDirectory(children[i].getName(), newAbsolutePath(directory.getAbsolutePath(), children[i].getName()));
                } else {
                    toSet[i] = new RemoteFile(children[i].getName(), newAbsolutePath(directory.getAbsolutePath(), children[i].getName()));
                }
            }
            directory.setNodes(toSet);
        } catch (ClientException e) {
            throw new VisitorException("Attempt to build the file tree has failed: " + e.getMessage(), e);
        }
    }

    /**
     * Visits a remote file.
     */
    @Override
    public void visitFile(RemoteFile file) {
        // Do nothing
    }
}
