package ru.spbau.alferov.javahw.reversi.net;

public class ReversiNetworkException extends Exception {
    public ReversiNetworkException(Throwable reason) {
        super("An I/O error occurred while interacting through network", reason);
    }
}
