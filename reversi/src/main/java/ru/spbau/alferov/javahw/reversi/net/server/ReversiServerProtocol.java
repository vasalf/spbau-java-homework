package ru.spbau.alferov.javahw.reversi.net.server;

import ru.spbau.alferov.javahw.reversi.ReversiApplication;
import ru.spbau.alferov.javahw.reversi.logic.Game;
import ru.spbau.alferov.javahw.reversi.net.RemotePlayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ReversiServerProtocol {
    private boolean serverPlaysWithBlack;

    public ReversiServerProtocol(boolean serverPlaysWithBlack) {
        this.serverPlaysWithBlack = serverPlaysWithBlack;
    }

    public void interactWithClient(DataInputStream inputStream, DataOutputStream outputStream) throws IOException {
        outputStream.writeBoolean(serverPlaysWithBlack);
        Game game = ReversiApplication.getInstance().startNetworkGame(serverPlaysWithBlack, new RemotePlayer(inputStream, outputStream));
        try {
            game.waitTillGameEnd();
        } catch (InterruptedException e) {
            // Do nothing
        }
    }
}
