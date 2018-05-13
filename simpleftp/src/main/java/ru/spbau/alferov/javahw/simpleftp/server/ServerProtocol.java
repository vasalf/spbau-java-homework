package ru.spbau.alferov.javahw.simpleftp.server;


import java.io.*;

/**
 * This class is used to interact with clients.
 */
public class ServerProtocol {
    /**
     * The server working directory.
     */
    private final File workingDir;

    /**
     * Constructs an instance of the protocol.
     *
     * @param workingDir Must be an existent directory
     */
    public ServerProtocol(File workingDir) {
        this.workingDir = workingDir;
    }

    /**
     * Interacts with the client. This function can be called from any number of threads simultaneously.
     */
    public void interactWithClient(DataInputStream in, DataOutputStream out) {
        while (true) {
            int request;

            try {
                request = in.readInt();
                if (request == 1) {
                    String path = in.readUTF();
                    File toRead = new File(workingDir, path);

                    if (!isInServerDirectory(toRead)) {
                        System.err.println("Security violation: client tried to read the prohibited file " + toRead.getAbsolutePath());
                        return;
                    }

                    if (!toRead.exists()) {
                        out.writeInt(0);
                    } else if (!toRead.isDirectory()) {
                        out.writeInt(1);
                        out.writeUTF(toRead.getName());
                        out.writeBoolean(false);
                    } else {
                        File[] list = toRead.listFiles();

                        out.writeInt(list.length);
                        for (File innerFile : list) {
                            out.writeUTF(innerFile.getName());
                            out.writeBoolean(innerFile.isDirectory());
                        }
                    }
                } else if (request == 2) {
                    String path = in.readUTF();
                    File toRead = new File(workingDir, path);

                    if (!isInServerDirectory(toRead)) {
                        System.err.println("Security violation: client tried to read the prohibited file " + toRead.getAbsolutePath());
                        return;
                    }

                    if (!toRead.exists() || !toRead.isFile()) {
                        out.writeLong(0);
                    } else {
                        RandomAccessFile f = new RandomAccessFile(toRead, "r");
                        byte[] toSend = new byte[(int) f.length()];
                        f.readFully(toSend);

                        out.writeLong(toSend.length);
                        out.write(toSend);
                    }
                } else {
                    System.err.println("Unknown request number: " + Integer.toString(request));
                    return;
                }
            } catch (IOException e) {
                System.err.println("The client has closed the connection: " + e.getMessage());
                return;
            }
        }
    }

    /**
     * Checks whether the requested file lies inside the server directory (so that the client is allowed to read the file).
     */
    private boolean isInServerDirectory(File file) {
        File canonicalServerDir, canonicalFile;
        try {
            canonicalServerDir = workingDir.getCanonicalFile();
            canonicalFile = file.getCanonicalFile();
        } catch (IOException e) {
            throw new RuntimeException("Could not get the canonical files", e);
        }

        while (canonicalFile.getParentFile() != null) {
            if (canonicalFile.equals(canonicalServerDir))
                return true;
            canonicalFile = canonicalFile.getParentFile();
        }
        return false;
    }
}
