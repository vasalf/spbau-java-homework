package ru.spbau.alferov.javahw.simpleftp.gui.filetree;

/**
 * This class represents a directory on the remote host.
 */
public class RemoteDirectory extends RemoteFileNode {
    /**
     * These are files inside the directory.
     */
    private RemoteFileNode[] nodes;

    /**
     * @return true
     */
    @Override
    public boolean isDirectory() {
        return true;
    }

    /**
     * This returns list of children.
     */
    @Override
    public RemoteFileNode[] listFiles() {
        return nodes;
    }

    /**
     * This accepts a {@link Visitor}.
     */
    @Override
    public void accept(Visitor v) throws VisitorException{
        v.visitDirectory(this);
        for (RemoteFileNode node : nodes) {
            v.visit(node);
        }
    }

    /**
     * This constructs a remote directory.
     */
    public RemoteDirectory(String dirName, String dirAbsolutePath) {
        super(dirName, dirAbsolutePath);
    }

    /**
     * This lets to change the children.
     */
    public void setNodes(RemoteFileNode[] nodes) {
        this.nodes = nodes;
    }
}
