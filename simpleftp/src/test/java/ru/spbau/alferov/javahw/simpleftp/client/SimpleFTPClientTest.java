package ru.spbau.alferov.javahw.simpleftp.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import ru.spbau.alferov.javahw.simpleftp.TestWithPipedClientServerStreams;
import ru.spbau.alferov.javahw.simpleftp.common.FTPFile;

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.net.Socket;

class SimpleFTPClientTest extends TestWithPipedClientServerStreams {
    private SimpleFTPClient client;

    @BeforeEach
    public void setUpClientFields() throws Exception {
        client = new SimpleFTPClient("127.0.0.1", 179);

        Field clientInField = SimpleFTPClient.class.getDeclaredField("in");
        clientInField.setAccessible(true);
        Field clientOutField = SimpleFTPClient.class.getDeclaredField("out");
        clientOutField.setAccessible(true);
        Field clientSocketField = SimpleFTPClient.class.getDeclaredField("socket");
        clientSocketField.setAccessible(true);

        clientInField.set(client, clientIn);
        clientOutField.set(client, clientOut);

        Socket socket = mock(Socket.class);
        when(socket.isConnected()).thenReturn(true);

        clientSocketField.set(client, socket);
    }

    /**
     * Tests that list call parses the returned empty list correctly.
     */
    @Test
    public void testListEmptyDirectory() throws Exception {
        serverOut.writeInt(0);

        assertEquals(0, client.list("/empty_dir").length);

        assertEquals(1, serverIn.readInt());
        assertEquals("/empty_dir", serverIn.readUTF());
    }

    /**
     * Tests that list call parses the returned non-empty list correctly.
     */
    @Test
    public void testListNonEmptyDirectory() throws Exception {
        serverOut.writeInt(3);
        serverOut.writeUTF("b.txt");
        serverOut.writeBoolean(false);
        serverOut.writeUTF("a.txt");
        serverOut.writeBoolean(false);
        serverOut.writeUTF("dir");
        serverOut.writeBoolean(true);

        FTPFile[] ans = client.list("/");
        FTPFile[] expected = {
                new FTPFile("b.txt", false),
                new FTPFile("a.txt", false),
                new FTPFile("dir", true)
        };

        assertArrayEquals(expected, ans);
        assertEquals(1, serverIn.readInt());
        assertEquals("/", serverIn.readUTF());
    }

    /**
     * This tests that reading content of empty file works correctly.
     */
    @Test
    public void testGetEmptyFile() throws Exception {
        serverOut.writeLong(0);

        byte[] ans = client.get("/empty_file");
        assertEquals(0, ans.length);

        assertEquals(2, serverIn.readInt());
        assertEquals("/empty_file", serverIn.readUTF());
    }

    /**
     * This tests that reading content of non-empty file works correctly.
     */
    @Test
    public void testGetNonEmptyFile() throws Exception {
        byte[] secret = "TOP_SECRET".getBytes();

        serverOut.writeLong(secret.length);
        serverOut.write(secret);

        byte[] ans = client.get("/secret_file");
        assertArrayEquals(secret, ans);

        assertEquals(2, serverIn.readInt());
        assertEquals("/secret_file", serverIn.readUTF());
    }
}