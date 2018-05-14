package ru.spbau.alferov.javahw.simpleftp.client;

import ru.spbau.alferov.javahw.simpleftp.common.FTPFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The implementation of SimpleFTP protocol.
 */
public class SimpleFTPClient {
    /**
     * This is the server to connect to.
     */
    private final String server;

    /**
     * This is the port used to interact.
     */
    private final int portNumber;

    /**
     * This is the socket used to interact.
     */
    private Socket socket;

    /**
     * This is the input stream that reads data from socket.
     */
    private DataInputStream in;

    /**
     * This is the output stream that writes data through the socket.
     */
    private DataOutputStream out;

    /**
     * Creates an instance of the client to be connected in future.
     */
    public SimpleFTPClient(String server, int portNumber) {
        this.server = server;
        this.portNumber = portNumber;
    }

    /**
     * Connects to the server.
     *
     * @throws ClientException In case of any connection errors.
     */
    public void connect() throws ClientException {
        try {
            socket = new Socket(server, portNumber);
        } catch (IOException e) {
            throw new ClientException("I/O exception occurred while trying to connect to the server", e);
        }

        System.out.println("Yup, the connection is established");

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new ClientException("I/O exception occurred while trying to set up the data streams", e);
        }
    }

    /**
     * Closes the connection to the server.
     */
    public void disconnect() throws ClientException {
        if (socket.isConnected()) {
            try {
                socket.close();
            } catch (IOException e) {
                throw new ClientException("I/O exception occurred while trying to close the connection", e);
            }
        }
    }

    /**
     * Requests the content of some remote directory.
     *
     * {@see The protocol description.}
     */
    public FTPFile[] list(String path) throws ClientException {
        if (!socket.isConnected()) {
            throw new ClientException("Connection to the server is not established.");
        }

        FTPFile[] result;

        try {
            out.writeInt(1);
            out.writeUTF(path);

            int size = in.readInt();
            result = new FTPFile[size];
            for (int i = 0; i < size; i++) {
                String fileName = in.readUTF();
                boolean isDir = in.readBoolean();
                result[i] = new FTPFile(fileName, isDir);
            }
        } catch (IOException e) {
            throw new ClientException("I/O exception occurred while interacting with server", e);
        }

        return result;
    }

    /**
     *  Requests the content of some remote file.
     *
     *  {@see The protocol description.}
     */
    public byte[] get(String path) throws ClientException {
        if (!socket.isConnected()) {
            throw new ClientException("Connection to the server is not established.");
        }

        byte[] result;

        try {
            out.writeInt(2);
            out.writeUTF(path);

            long size = in.readLong();
            result = new byte[(int)size];
            in.readFully(result);
        } catch (IOException e) {
            throw new ClientException("I/O exception occurred while interacting with server", e);
        }

        return result;
    }
}
