package ru.spbau.alferov.javacw.md5;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

class ChecksumCalculatorTest {
    /**
     * Converts a byte array to human-readable format
     */
    private String toHexString(byte[] bytes) {
        StringBuilder ans = new StringBuilder();
        for (byte b : bytes) {
            ans.append(String.format("%02x", b));
        }
        return ans.toString();
    }

    /**
     * Tests the given calculator on file test1.txt from resources
     */
    private void fileResultTest(ChecksumCalculator calculator) throws Exception {
        String expected = "c4ca4238a0b923820dcc509a6f75849b";
        byte[] ans = calculator.calculate(new File(ChecksumCalculator.class.getResource("/test1.txt").toURI()));
        assertEquals(expected, toHexString(ans));
    }

    /**
     * Tests the single-thread calculator on file test1.txt from resources
     */
    @Test
    public void singleThreadCalculatorFileResultTest() throws Exception {
        fileResultTest(ChecksumCalculatorFactory.createSingleThreadCalculator());
    }

    /**
     * Tests the fork-join calculator on file test1.txt from resources
     */
    @Test
    public void forkJoinCalculatorFileResultTest() throws Exception {
        fileResultTest(ChecksumCalculatorFactory.createForkJoinCalculator());
    }

    /**
     * Tests the given calculator on directory test2 from resources
     */
    private void directoryResultTest(ChecksumCalculator calculator) throws Exception {
        String expected = "c131c4409c30a757bcf9cbce9869a2e3";
        byte[] ans = calculator.calculate(new File(ChecksumCalculator.class.getResource("/test2").toURI()));
        assertEquals(expected, toHexString(ans));
    }

    /**
     * Tests the single-thread calculator on directory test2 from resources
     */
    @Test
    public void singleThreadCalculatorDirectoryResultTest() throws Exception {
        directoryResultTest(ChecksumCalculatorFactory.createSingleThreadCalculator());
    }

    /**
     * Tests the fork-join calculator on directory test2 from resources
     */
    @Test
    public void forkJoinCalculatorDirectoryResultTest() throws Exception {
        directoryResultTest(ChecksumCalculatorFactory.createForkJoinCalculator());
    }
}