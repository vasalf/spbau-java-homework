package ru.spbau.alferov.javahw.simpleftp.client;

import ru.spbau.alferov.javahw.simpleftp.common.FTPFile;

import java.io.*;
import java.util.StringTokenizer;

/**
 * This is a simple CLI for the SimpleFTP connection.
 * See {@link #HELP_MESSAGE} for available commands.
 */
public class ClientCLI {
    /**
     * The help message displayed on help request.
     */
    private static final String HELP_MESSAGE =
            "Available commands:\n" +
                    "get <REMOTE-PATH> <LOCAL-PATH>    Save the remote file to local destination\n" +
                    "list <REMOTE-PATH>                List files in the remote directory\n" +
                    "quit | exit                       Exit the client\n" +
                    "help                              Print this message\n";

    /**
     * Start interacting with server.
     * @param client The protocol implementation to be used to interact
     */
    public static void interact(SimpleFTPClient client) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                System.out.print("> ");
                String line = in.readLine();

                StringTokenizer tokenizer = new StringTokenizer(line);
                if (!tokenizer.hasMoreTokens()) {
                    continue;
                }
                String command = tokenizer.nextToken();

                if (command.equals("quit") || command.equals("exit")) {
                    break;
                } else if (command.equals("list")) {
                    String path;

                    if (!tokenizer.hasMoreTokens()) {
                        System.out.println("Usage: list <REMOTE-PATH>");
                        continue;
                    } else {
                        path = tokenizer.nextToken();
                    }

                    try {
                        FTPFile[] files = client.list(path);
                        System.out.println(files.length);
                        for (FTPFile file : files) {
                            if (file.isDir()) {
                                System.out.print("D ");
                            } else {
                                System.out.print("F ");
                            }
                            System.out.println(file.getName());
                        }
                    } catch (ClientException e) {
                        e.printStackTrace();
                        return;
                    }
                } else if (command.equals("get")) {
                    String remotePath, localPath;

                    if (tokenizer.countTokens() != 2) {
                        System.out.println("Usage: get <REMOTE-PATH> <LOCAL-PATH>");
                        continue;
                    } else {
                        remotePath = tokenizer.nextToken();
                        localPath = tokenizer.nextToken();
                    }

                    try {
                        byte[] read = client.get(remotePath);
                        FileOutputStream out = new FileOutputStream(localPath);
                        out.write(read);
                    } catch (ClientException e) {
                        e.printStackTrace();
                        return;
                    } catch (IOException e) {
                        System.out.println("Could not write to file " + localPath);
                    }
                } else if (command.equals("help")) {
                    System.out.print(HELP_MESSAGE);
                } else {
                    System.out.println("Unknown command: " + command);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
