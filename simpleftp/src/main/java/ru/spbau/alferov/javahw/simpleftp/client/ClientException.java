package ru.spbau.alferov.javahw.simpleftp.client;

/**
 * This class represents any exception to be thrown while interacting with server.
 */
public class ClientException extends Exception {
    /**
     * Constructs an exception with reason.
     */
    public ClientException(String reasonMessage, Throwable reason) {
        super("Client has stopped working: " + reasonMessage + ": " + reason.getMessage(), reason);
    }

    /**
     * Constructs an exception.
     */
    public ClientException(String message) {
        super("Client has stopped working: " + message);
    }
}
