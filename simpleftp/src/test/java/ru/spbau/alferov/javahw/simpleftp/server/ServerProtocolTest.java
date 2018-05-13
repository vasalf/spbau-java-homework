package ru.spbau.alferov.javahw.simpleftp.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.spbau.alferov.javahw.simpleftp.TestWithPipedClientServerStreams;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This tests the {@link ServerProtocol} class on the test resources.
 */
class ServerProtocolTest extends TestWithPipedClientServerStreams {
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

    /**
     * This tests that listing of non-existent directory works as specified.
     */
    @Test
    public void testNonExistentDirectoryList() throws Exception {
        clientOut.writeInt(1);
        clientOut.writeUTF("/non_existent/directory");
        clientOut.writeInt(3);

        protocol.interactWithClient(serverIn, serverOut);

        assertEquals(0, clientIn.readInt());
    }

    /**
     * This tests that reading content of non-existent file works as specified.
     */
    @Test
    public void testNonExistentFileGet() throws Exception {
        clientOut.writeInt(2);
        clientOut.writeUTF("/non_existent/file");
        clientOut.writeInt(3);

        protocol.interactWithClient(serverIn, serverOut);

        assertEquals(0, clientIn.readInt());
    }

    /**
     * This tests that listing a file works correctly.
     */
    @Test
    public void testListFile() throws Exception {
        clientOut.writeInt(1);
        clientOut.writeUTF("/a.txt");
        clientOut.writeInt(3);

        protocol.interactWithClient(serverIn, serverOut);

        assertEquals(1, clientIn.readInt());
        assertEquals("a.txt", clientIn.readUTF());
        assertFalse(clientIn.readBoolean());
    }
}