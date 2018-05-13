package ru.spbau.alferov.javahw.simpleftp.server;

/**
 * This represents any exception that brings the server down.
 */
public class ServerException extends Exception {
    /**
     * Constructs the exception with reason.
     */
    public ServerException(String reasonMessage, Throwable reason) {
        super("Server has stopped working: " + reasonMessage, reason);
    }
}
