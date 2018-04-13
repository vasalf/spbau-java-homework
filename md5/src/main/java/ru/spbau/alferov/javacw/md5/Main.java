package ru.spbau.alferov.javacw.md5;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;

public class Main {
    /**
     * Represents the result of {@link #execute(ChecksumCalculator)} invocation
     */
    private static class ChecksumResult {
        /**
         * Time elapsed
         */
        private long executedTime;
        /**
         * Returned value
         */
        private @NotNull byte[] answer;

        /**
         * Basic constructor
         */
        public ChecksumResult(long executedTime, @NotNull byte[] answer) {
            this.executedTime = executedTime;
            this.answer = answer;
        }

        /**
         * @return The time elapsed
         */
        public long getExecutedTime() {
            return executedTime;
        }

        /**
         * @return The returned value
         */
        public @NotNull byte[] getAnswer() {
            return answer;
        }
    }

    /**
     * Runs a calculator on the test from the resources
     * @param calculator The calculator to be run
     * @return The result of the invocation
     */
    private static @NotNull ChecksumResult execute(@NotNull ChecksumCalculator calculator) throws Exception {
        long startTime = System.currentTimeMillis();
        File file = new File(Main.class.getResource("/test").toURI());
        byte[] answer = calculator.calculate(file);
        long stopTime = System.currentTimeMillis();
        return new ChecksumResult(stopTime - startTime, answer);
    }

    /**
     * Tells the execution time for two versions of the calculator.
     */
    public static void main(String[] args) {
        ChecksumResult singleThread, forkJoin;
        try {
            singleThread = execute(ChecksumCalculatorFactory.createSingleThreadCalculator());
            forkJoin = execute(ChecksumCalculatorFactory.createForkJoinCalculator());
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while testing calculators", e);
        }
        if (!Arrays.equals(singleThread.getAnswer(), forkJoin.getAnswer())) {
            System.out.println("Results does not match!");
        } else {
            System.out.printf("Single thread version did this in %d milliseconds\n", singleThread.getExecutedTime());
            System.out.printf("Fork-join version did this in %d milliseconds\n", forkJoin.getExecutedTime());
        }
    }
}
