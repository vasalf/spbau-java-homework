package ru.spbau.alferov.javahw.simpleftp.gui.filetree;

/**
 * This represents a file on remote host.
 */
public class RemoteFile extends RemoteFileNode {
    /**
     * @return false
     */
    @Override
    public boolean isDirectory() {
        return false;
    }

    /**
     * @return Empty array
     */
    @Override
    public RemoteFileNode[] listFiles() {
        return new RemoteFileNode[0];
    }

    /**
     * This accepts a {@link Visitor}.
     */
    @Override
    public void accept(Visitor v) throws VisitorException {
        v.visitFile(this);
    }

    /**
     * This constructs a file.
     */
    public RemoteFile(String fileName, String absolutePath) {
        super(fileName, absolutePath);
    }
}
