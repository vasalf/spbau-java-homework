package ru.spbau.alferov.javacw.md5;

import org.jetbrains.annotations.NotNull;

/**
 * Factory for the checksum calculators
 */
public class ChecksumCalculatorFactory {
    /**
     * Creates a single-thread calculator
     */
    @NotNull
    public static ChecksumCalculator createSingleThreadCalculator() {
        return new SingleThreadChecksumCalculator();
    }

    /**
     * Creates the fork-join calculator
     */
    @NotNull
    public static ChecksumCalculator createForkJoinCalculator() {
        return new ForkJoinChecksumCalculator();
    }
}
