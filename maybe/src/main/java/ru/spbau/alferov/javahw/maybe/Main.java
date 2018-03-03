package ru.spbau.alferov.javahw.maybe;

import java.io.*;
import java.util.ArrayList;

/**
 * The application reads some literals from the file given as first
 * command line parameter. If a literal is an integer, then its square
 * is written to the second file. Otherwise, "null" is written.
 */
public class Main {
    /**
     * This function returns an ArrayList of integers that could be
     * read from each line of given file. If not an integer is stored
     * in some line, it writes Maybe.nothing() to the corresponding
     * cell of the returned value.
     */
    private static ArrayList<Maybe<Integer>> readFromFile(String filename) throws IOException {
        try (
                FileReader fileReader = new FileReader(filename);
                BufferedReader isr = new BufferedReader(fileReader)
        ) {
            ArrayList<Maybe<Integer>> res = new ArrayList<>();
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
    }

    /**
     * This function writes squares of given integers to the lines of
     * given file. If Maybe.nothing() is given, "null" is written to
     * the corresponding line of output file.
     *
     * @throws MaybeException is never thrown but javac can't prove
     * it, so this should be added to the throws() list.
     */
    private static void writeSquaresToFile(String filename, ArrayList<Maybe<Integer>> list) throws IOException, MaybeException {
        try (PrintStream ps = new PrintStream(filename)) {
            for (Maybe<Integer> mb : list) {
                Maybe<Long> out = mb.map(x -> (long) x * (long) x);
                if (out.isPresent()) {
                    ps.println(out.get());
                } else {
                    ps.println("null");
                }
            }
        }
    }

    /**
     * Application takes two parameters: input and output file.
     * For each line of input file it writes a new line to the
     * output file.
     * If an integer is written on some line of input file, it writes
     * its square to the corresponding line of output file, otherwise
     * it writes the string "null".
     */
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
