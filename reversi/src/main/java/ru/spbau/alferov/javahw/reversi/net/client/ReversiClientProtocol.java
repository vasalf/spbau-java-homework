package ru.spbau.alferov.javahw.reversi.net.client;

import ru.spbau.alferov.javahw.reversi.ReversiApplication;
import ru.spbau.alferov.javahw.reversi.logic.Game;
import ru.spbau.alferov.javahw.reversi.net.RemotePlayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ReversiClientProtocol {
    public void interactWithServer(DataInputStream inputStream, DataOutputStream outputStream) throws IOException {
        boolean localWhite = inputStream.readBoolean();
        Game game = ReversiApplication.getInstance().startNetworkGame(!localWhite, new RemotePlayer(inputStream, outputStream));

        try {
            game.waitTillGameEnd();
        } catch (InterruptedException e) {
            // Do nothing
        }
    }
}
