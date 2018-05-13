package ru.spbau.alferov.javahw.simpleftp;

import org.junit.jupiter.api.BeforeEach;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class TestWithPipedClientServerStreams {
    /**
     * This is the stream in which the client (tester) should write the data.
     */
    protected DataOutputStream clientOut;

    /**
     * This is the stream from which the server receives the data.
     */
    protected DataInputStream serverIn;

    /**
     * This is the stream to which the server writes the data.
     */
    protected DataOutputStream serverOut;

    /**
     * This is the stream from which the client (tester) should receive the data.
     */
    protected DataInputStream clientIn;

    /**
     * This pipes the {@link #serverIn}, {@link #serverOut}, {@link #clientIn}, {@link #clientOut} streams together.
     */
    @BeforeEach
    public void setUpPipes() throws Exception {
        PipedOutputStream clientOutStream = new PipedOutputStream();
        PipedInputStream serverInStream = new PipedInputStream(clientOutStream);
        PipedOutputStream serverOutStream = new PipedOutputStream();
        PipedInputStream clientInStream = new PipedInputStream(serverOutStream);

        clientOut = new DataOutputStream(clientOutStream);
        serverIn = new DataInputStream(serverInStream);
        serverOut = new DataOutputStream(serverOutStream);
        clientIn = new DataInputStream(clientInStream);
    }
}
