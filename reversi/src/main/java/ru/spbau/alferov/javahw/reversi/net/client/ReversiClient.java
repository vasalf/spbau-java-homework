package ru.spbau.alferov.javahw.reversi.net.client;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.ReversiApplication;
import ru.spbau.alferov.javahw.reversi.net.ReversiNetworkException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReversiClient {
    @NotNull
    private final String serverAddress;

    private final int serverPort;

    private final ReversiClientProtocol protocol;

    @NotNull
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ReversiClient(@NotNull String serverAddress, int serverPort, ReversiClientProtocol protocol) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.protocol = protocol;
    }

    public void run() throws ReversiNetworkException {
        Socket socket;
        DataInputStream inputStream;
        DataOutputStream outputStream;
        try {
            socket = new Socket(serverAddress, serverPort);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            ReversiApplication.getInstance().indicateIOException(e);
            throw new ReversiNetworkException(e);
        }

        executor.submit(() -> {
           try {
               protocol.interactWithServer(inputStream, outputStream);
               socket.close();
           } catch (IOException e) {
               ReversiApplication.getInstance().indicateIOException(e);
           }
        });
    }

    public static void interruptExecutor() {
        executor.shutdownNow();
    }
}
