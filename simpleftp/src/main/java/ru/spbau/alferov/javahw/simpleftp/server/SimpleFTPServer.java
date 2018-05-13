package ru.spbau.alferov.javahw.simpleftp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * This is the main server class.
 */
public class SimpleFTPServer {
    /**
     * The port to be opened.
     */
    private final int portNumber;

    /**
     * Number of threads used to accept the clients.
     */
    private final int nThreads;

    /**
     * The server socket.
     */
    private ServerSocket serverSocket;

    /**
     * The server protocol used to interact with clients.
     */
    private final ServerProtocol protocol;

    /**
     * Constructs the server.
     */
    public SimpleFTPServer(ServerProtocol protocol, int portNumber, int nThreads) {
        this.protocol = protocol;
        this.portNumber = portNumber;
        this.nThreads = nThreads;
    }

    /**
     * This is the handler for a connected client. This is used as a task for the thread pool.
     */
    private class ClientHandler implements Runnable {
        /**
         * The socket for the client connection.
         */
        private final Socket clientSocket;

        /**
         * Constructs the handler.
         */
        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        /**
         * Interacts with client using the protocol.
         */
        @Override
        public void run() {
            System.out.println("Yup, someone is trying to connect to me!");
            try {
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                protocol.interactWithClient(in, out);
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Aborting connection to the client due to an I/O exception:");
                e.printStackTrace();
            }
        }
    }

    /**
     * Runs the server.
     *
     * @throws ServerException In case of any I/O interaction errors
     */
    public void run() throws ServerException {
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            throw new ServerException("I/O exception occurred while trying to set up the socket", e);
        }

        ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(nThreads);
        while (true) {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                throw new ServerException("I/O exception occurred while trying to accept the connection from the client", e);
            }

            executor.submit(new ClientHandler(clientSocket));
        }
    }
}
