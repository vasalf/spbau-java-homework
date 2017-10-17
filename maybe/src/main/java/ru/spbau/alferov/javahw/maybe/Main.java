package ru.spbau.alferov.javahw.maybe;

import java.io.*;
import java.util.ArrayList;


public class Main {
    private static ArrayList<Maybe<Integer>> readFromFile(String filename) throws IOException {
        BufferedReader isr = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        ArrayList<Maybe<Integer>> res  = new ArrayList<>();
        String line = isr.readLine();
        while (line != null) {
            try {
                Integer read = Integer.parseInt(line);
                res.add(Maybe.just(read));
            } catch (NumberFormatException e) {
                res.add(Maybe.nothing());
            }
            line = isr.readLine();
        }
        return res;
    }

    private static void writeSquaresToFile(String filename, ArrayList<Maybe<Integer>> list) throws IOException, MaybeException {
        PrintStream ps = new PrintStream(filename);
        for (Maybe<Integer> mb : list) {
            Maybe<Long> out = mb.map(x -> (long)x * (long)x);
            if (out.isPresent()) {
                ps.println(out.get());
            } else {
                ps.println("null");
            }
        }
    }

    public static void main(String args[]) {
        if (args.length != 2) {
            System.err.println("Wrong number of arguments");
            return;
        }
        try {
            ArrayList<Maybe<Integer>> al = readFromFile(args[0]);
            writeSquaresToFile(args[1], al);
        } catch (IOException e) {
            System.err.print("IO Exception: ");
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (MaybeException e) {
            System.err.print("MaybeException: ");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
