package ru.spbau.alferov.javacw.md5;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Interface for the calculations
 */
public interface ChecksumCalculator {
    /**
     * Calculates the checksum
     * @param ofFile File for which the checksum should be calculated
     * @return The desired checksum
     */
    @NotNull
    byte[] calculate(File ofFile) throws IOException;
}
