package ru.spbau.alferov.javahw.simpleftp.common;

/**
 * Represents a file on remote host. Used to represent the "list" command answers
 */
public class FTPFile {
    /**
     * The file name
     */
    private String name;

    /**
     * True if this is a directory, false otherwise
     */
    private boolean isDir;

    /**
     * @return Getter for the filename
     */
    public String getName() {
        return name;
    }

    /**
     * @return True if this is a directory, false otherwise
     */
    public boolean isDir() {
        return isDir;
    }

    /**
     * Constructs a file.
     *
     * @param name  The filename
     * @param isDir True if this is a directory, false otherwise
     */
    public FTPFile(String name, boolean isDir) {
        this.name = name;
        this.isDir = isDir;
    }

    /**
     * Compares two files on being equal.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof FTPFile)) {
            return false;
        }
        FTPFile otherFile = (FTPFile)other;
        return name.equals(otherFile.getName()) && isDir == otherFile.isDir();
    }
}
