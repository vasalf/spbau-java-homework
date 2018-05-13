package ru.spbau.alferov.javahw.simpleftp.client;

/**
 * This is the main class for the simple client CLI.
 */
public class SimpleFTPClientMain {
    /**
     * Establishes the connection and uses the {@link ClientCLI} to interact.
     */
    public static void main(String args[]) {
        if (args.length != 2) {
            System.out.println("You should provide the server address as the first argument and the port number as the second.");
            return;
        }

        int portNumber;
        try {
            portNumber = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Could not parse the port number from " + args[1]);
            return;
        }

        SimpleFTPClient client = new SimpleFTPClient(args[0], portNumber);

        try {
            client.connect();
        } catch (ClientException e) {
            e.printStackTrace();
        }

        ClientCLI.interact(client);
    }
}
