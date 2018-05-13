package ru.spbau.alferov.javahw.simpleftp.server;

import java.io.File;

/**
 * This class is used to run the server.
 */
public class SimpleFTPServerMain {
    /**
     * Runs the server.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("You should provide the server directoty as first parameter and the port number as second parameter.");
            return;
        }

        File workDir = new File(args[0]);
        if (!workDir.exists()) {
            System.out.println("The directory " + args[0] + " does not exist!");
            return;
        } else if (!workDir.isDirectory()) {
            System.out.println("File " + args[0] + "is not a directory!");
            return;
        }

        ServerProtocol protocol = new ServerProtocol(workDir);

        int portNumber;
        try {
            portNumber = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Could not parse the port number from string " + args[1]);
            return;
        }

        try {
            new SimpleFTPServer(protocol, portNumber, 2).run();
        } catch (ServerException e) {
            e.printStackTrace();
        }
    }
}
