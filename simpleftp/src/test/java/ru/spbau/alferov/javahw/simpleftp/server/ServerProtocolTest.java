package ru.spbau.alferov.javahw.simpleftp.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This tests the {@link ServerProtocol} class on the test resources.
 */
class ServerProtocolTest {
    /**
     * This is a protocol instance to be tested. It is created before each test.
     */
    private ServerProtocol protocol;

    /**
     * This creates a protocol instance to be tested.
     */
    @BeforeEach
    public void setUpTestedProtocol() throws Exception {
        File file = new File(ServerProtocolTest.class.getClassLoader().getResource("test").toURI());
        protocol = new ServerProtocol(file);
    }

    /**
     * This is the stream in which the client (tester) should write the data.
     */
    private DataOutputStream clientOut;

    /**
     * This is the stream from which the server receives the data.
     */
    private DataInputStream serverIn;

    /**
     * This is the stream to which the server writes the data.
     */
    private DataOutputStream serverOut;

    /**
     * This is the stream from which the client (tester) should receive the data.
     */
    private DataInputStream clientIn;

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

    /**
     * This tests the list command on the server root.
     */
    @Test
    public void testListRoot() throws Exception {
        clientOut.writeInt(1);
        clientOut.writeUTF("/");
        clientOut.writeInt(3);

        protocol.interactWithClient(serverIn, serverOut);

        assertEquals(3, clientIn.readInt());
        Set<String> were = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            String name = clientIn.readUTF();
            assertTrue(name.equals("a.txt") || name.equals("b.txt") || name.equals("dir"));
            if (name.endsWith(".txt")) {
                assertFalse(clientIn.readBoolean());
            } else {
                assertTrue(clientIn.readBoolean());
            }
            were.add(name);
        }

        assertEquals(3, were.size());
    }

    /**
     * This tests the list command on a subdirectory.
     */
    @Test
    public void testListSubdir() throws Exception {
        clientOut.writeInt(1);
        clientOut.writeUTF("/dir/../dir/");
        clientOut.writeInt(3);

        protocol.interactWithClient(serverIn, serverOut);

        assertEquals(1, clientIn.readInt());
        assertEquals("c.txt", clientIn.readUTF());
        assertFalse(clientIn.readBoolean());
    }

    /**
     * This is a helper function which reads the given number of bytes from the {@link #clientIn} stream into a byte
     * array.
     */
    private byte[] readClientsBytes(int length) throws Exception {
        byte[] bytes = new byte[length];
        clientIn.readFully(bytes);
        return bytes;
    }

    /**
     * This tests the get function on files in the root directory and in subdirectory.
     */
    @Test
    public void testReadTextFiles() throws Exception {
        clientOut.writeInt(2);
        clientOut.writeUTF("/a.txt");
        clientOut.writeInt(2);
        clientOut.writeUTF("/b.txt");
        clientOut.writeInt(2);
        clientOut.writeUTF("/dir/c.txt");
        clientOut.writeInt(3);

        protocol.interactWithClient(serverIn, serverOut);

        byte[] aContent = "abacaba".getBytes();
        byte[] bContent = "asdf".getBytes();
        byte[] cContent = "abracadabra".getBytes();

        assertEquals(aContent.length, clientIn.readLong());
        assertArrayEquals(aContent, readClientsBytes(aContent.length));
        assertEquals(bContent.length, clientIn.readLong());
        assertArrayEquals(bContent, readClientsBytes(bContent.length));
        assertEquals(cContent.length, clientIn.readLong());
        assertArrayEquals(cContent, readClientsBytes(cContent.length));
    }
}