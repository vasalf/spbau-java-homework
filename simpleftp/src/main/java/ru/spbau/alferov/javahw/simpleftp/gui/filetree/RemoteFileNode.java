package ru.spbau.alferov.javahw.simpleftp.gui.filetree;

/**
 * This is an abstract class for directories/files that can be visited by {@link Visitor}.
 */
public abstract class RemoteFileNode {
    /**
     * This is the file name.
     */
    private String fileName;

    /**
     * @return The file name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return The file absolute path.
     */
    public String getAbsolutePath() {
        return absolutePath;
    }

    /**
     * This is the file absolute path.
     */
    private String absolutePath;

    /**
     * This constructs a remote file.
     */
    protected RemoteFileNode(String name, String absolutePath) {
        fileName = name;
        this.absolutePath = absolutePath;
    }

    /**
     * @return True if is a directory, false otherwise.
     */
    abstract public boolean isDirectory();

    /**
     * @return List of children if this is a directory, empty array otherwise.
     */
    abstract public RemoteFileNode[] listFiles();

    /**
     * This accepts a {@link Visitor}.
     */
    abstract public void accept(Visitor v) throws VisitorException;
}
