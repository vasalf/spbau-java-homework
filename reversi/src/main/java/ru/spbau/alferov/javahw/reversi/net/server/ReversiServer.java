package ru.spbau.alferov.javahw.reversi.net.server;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.ReversiApplication;
import ru.spbau.alferov.javahw.reversi.net.ReversiNetworkException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReversiServer {
    private final int portNumber;
    private final ReversiServerProtocol protocol;
    private ServerSocket serverSocket;

    @NotNull
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();


    public ReversiServer(int portNumber, ReversiServerProtocol protocol) {
        this.portNumber = portNumber;
        this.protocol = protocol;
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket clientSocket) {
            socket = clientSocket;
        }

        @Override
        public void run() {
            try {
                @NotNull DataInputStream in = new DataInputStream(socket.getInputStream());
                @NotNull DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                protocol.interactWithClient(in, out);
                socket.close();
                serverSocket.close();
            } catch (IOException e) {
                ReversiApplication.getInstance().indicateIOException(e);
            }
        }
    }

    public void run() throws ReversiNetworkException {
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            throw new ReversiNetworkException(e);
        }

        try {
            Socket socket = serverSocket.accept();
            executor.submit(new ClientHandler(socket));
        } catch (IOException e) {
            throw new ReversiNetworkException(e);
        }
    }

    public static void interruptExecutor() {
        executor.shutdownNow();
    }
}
