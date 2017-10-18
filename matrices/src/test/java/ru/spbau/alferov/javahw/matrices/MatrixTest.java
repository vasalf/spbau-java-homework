package ru.spbau.alferov.javahw.matrices;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.util.Scanner;

import static org.junit.Assert.*;

public class MatrixTest {
    /*
     * We need to test
     */
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStdoutCapture() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void cleanUpStdoutCapture() {
        System.setOut(null);
    }


    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * Checks that constructor Matrix(N) does not fail if N is odd.
     */
    @Test
    public void constructionByOddNumber() {
        new Matrix(1);
        new Matrix(3);
        new Matrix(5);
        new Matrix(179);
    }

    /**
     * Checks that constructor Matrix(N) throws a correct exception
     * if N is even.
     */
    @Test
    public void constructByEvenNumber() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Size of the Matrix must be odd.");
        Matrix m = new Matrix(2);
    }

    /**
     * Checks that constructor Matrix(array) does not fail if array
     * is a correct square matrix of odd size.
     */
    @Test
    public void constructByOddSquareMatrix() {
        int[][] a1 = {{1}};
        int[][] a3 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        Matrix m1 = new Matrix(a1);
        Matrix m3 = new Matrix(a3);
    }

    /**
     * Checks that constructor Matrix(array) throws a correct
     * exception if array consists of rows of different lengths.
     */
    @Test
    public void constructByDifferentLengths() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Not a square matrix is given.");

        int[][] a = {{1}, {2, 3}, {4, 5, 6}};
        Matrix m = new Matrix(a);
    }

    /**
     * Checks that constructor Matrix(array) throws a correct
     * exception if array is not a square matrix.
     */
    @Test
    public void constructByNotSquareMatrix() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Not a square matrix is given.");

        int[][] a = {{ 1,  2,  3,  4,  5},
                     { 6,  7,  8,  9, 10},
                     {11, 12, 13, 14, 15}};
        Matrix m = new Matrix(a);
    }

    /**
     * Checks that constructor Matrix(array) throws a correct
     * exception if array is a square matrix of even size.
     */
    @Test
    public void constructByEvenSquareMatrix() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Size of given matrix must be odd.");

        int[][] a = {{ 1,  2,  3,  4},
                     { 5,  6,  7,  8},
                     { 9, 10, 11, 12},
                     {13, 14, 15, 16}};
        Matrix m = new Matrix(a);
    }

    /**
     * Checks that the Matrix constructed from an array consists of
     * elements from this array.
     */
    @Test
    public void getElement() {
        int[][] a = new int[179][179];
        for (int i = 0; i < 179; i++) {
            for (int j = 0; j < 179; j++) {
                a[i][j] = 179 * i + j;
            }
        }
        Matrix m = new Matrix(a);
        for (int i = 0; i < 179; i++) {
            for (int j = 0; j < 179; j++) {
                assertEquals(a[i][j], m.getElement(i, j));
            }
        }
    }

    /**
     * Checks that constructor Matrix(N) constructs a matrix filled
     * with zeroes.
     */
    @Test
    public void constructByNumberFillsWithZeroes() {
        Matrix m = new Matrix(179);
        for (int i = 0; i < 179; i++) {
            for (int j = 0; j < 179; j++)
                assertEquals(0, m.getElement(i, j));
        }
    }

    /**
     * Checks that columns are sorted by first element after calling
     * sortColumns.
     */
    @Test
    public void sortColumns() {
        int[][] a = {{5, 1, 1, 7, 2},
                     {0, 0, 0, 0, 0},
                     {0, 1, 2, 0, 0},
                     {0, 0, 0, 0, 0},
                     {0, 0, 0, 0, 0}};
        Matrix m = new Matrix(a);
        m.sortColumns();
        for (int i = 0; i < 4; i++) {
            assertTrue(m.getElement(0, i) <= m.getElement(0, i + 1));
        }
    }

    /**
     * Checks sortColumns on a test with unique answer.
     */
    @Test
    public void sortColumnsUniqueAnswer() {
        int[][] a = {{3, 1, 2},
                     {4, 5, 6},
                     {7, 8, 9}};
        int[][] expected = {{1, 2, 3},
                            {5, 6, 4},
                            {8, 9, 7}};
        Matrix m = new Matrix(a);
        m.sortColumns();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals( expected[i][j], m.getElement(i, j));
            }
        }
    }

    /**
     * Checks the spiral bypass with N=5 on correctness.
     */
    @Test
    public void spiralBypass() {
        int[][] a = {{21, 22, 23, 24, 25},
                     {20,  7,  8,  9, 10},
                     {19,  6,  1,  2, 11},
                     {18,  5,  4,  3, 12},
                     {17, 16, 15, 14, 13}};
        int[] expected = { 1,  2,  3,  4,  5,
                           6,  7,  8,  9, 10,
                          11, 12, 13, 14, 15,
                          16, 17, 18, 19, 20,
                          21, 22, 23, 24, 25};
        Matrix m = new Matrix(a);
        assertArrayEquals(expected, m.spiralBypass());
    }

    /**
     * Checks an extreme case: bypass of Matrix of size 1.
     */
    @Test
    public void spiralBypassOfSize1() {
        int[][] a = {{1}};
        int[] expected = {1};
        Matrix m = new Matrix(a);
        assertArrayEquals(expected, m.spiralBypass());
    }

    /**
     * Checks the printSpiralBypass output.
     */
    @Test
    public void printSpiralBypass() {
        int[][] a = {{7, 8, 9},
                     {6, 1, 2},
                     {5, 4, 3}};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Matrix m = new Matrix(a);
        m.printSpiralBypass();

        Scanner outputScanner = new Scanner(outContent.toString());
        int currentIndex = 0;
        while (outputScanner.hasNext()) {
            assertNotEquals(currentIndex, 9);
            int x = outputScanner.nextInt();
            assertEquals(expected[currentIndex++], x);
        }
        assertEquals(currentIndex, 9);
    }

    /**
     * Checks that construction from a null array fails.
     */
    @Test(expected = NullPointerException.class)
    public void testNullConstruction() {
        Matrix m = new Matrix(null);
    }
}