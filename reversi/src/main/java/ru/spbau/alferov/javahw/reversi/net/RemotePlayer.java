package ru.spbau.alferov.javahw.reversi.net;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.ReversiApplication;
import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.Turn;
import ru.spbau.alferov.javahw.reversi.player.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RemotePlayer extends Player {
    @NotNull
    private DataInputStream inputStream;
    @NotNull
    private DataOutputStream outputStream;

    public RemotePlayer(@NotNull DataInputStream inputStream, @NotNull DataOutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public @NotNull Turn makeTurn(Field field) throws GameInterruptedException {
        int turnRow, turnCol;
        try {
            turnRow = inputStream.readInt();
            turnCol = inputStream.readInt();
        } catch (IOException e) {
            ReversiApplication.getInstance().indicateIOException(e);
            throw new GameInterruptedException();
        }
        return new Turn(turnRow, turnCol);
    }

    @Override
    public String getName() {
        return "Myself";
    }

    @Override
    public void processOtherPlayerTurn(Turn turn) throws GameInterruptedException {
        try {
            outputStream.writeInt(turn.getRow());
            outputStream.writeInt(turn.getColumn());
        } catch (IOException e) {
            ReversiApplication.getInstance().indicateIOException(e);
            throw new GameInterruptedException();
        }
    }
}
