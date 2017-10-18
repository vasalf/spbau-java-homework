package ru.spbau.alferov.javahw.matrices;

import java.util.Arrays;

/**
 * Square matrix of ints NxN (N is odd).
 * The Matrix is stored as an ArrayList of its Columns.
 */
public class Matrix {
    /**
     * Class for Columns of Matrix.
     *
     * Written as a separate class as the columns should be
     * Comparable by first elements.
     */
    private class Column implements Comparable<Column> {
        private int[] col;

        public Column() {
            col = new int[Matrix.this.size];
        }

        /**
         * Gets element by index.
         */
        public int getElement(int index) {
            return col[index];
        }

        /**
         * Sets an element by index
         */
        public void setElement(int index, int value) {
            col[index] = value;
        }

        /**
         * Compares columns by first elements.
         */
        public int compareTo(Column c) {
            Integer myFirst = getElement(0);
            Integer cFirst = c.getElement(0);
            return myFirst.compareTo(cFirst);
        }
    }

    /**
     * Size of the matrix (must be odd).
     */
    private int size;

    /**
     * An array where the columns are stored.
     */
    private Column[] columns;

    /**
     * Constructs a Matrix of zeroes of size NxN.
     *
     * @throws IllegalArgumentException If an even size is given.
     */
    public Matrix(int n) throws IllegalArgumentException {
        if (n % 2 == 0) {
            throw new IllegalArgumentException("Size of the Matrix must be odd.");
        }
        size = n;
        columns = new Column[size];
        for (int i = 0; i < size; i++) {
            columns[i] = new Column();
        }
    }

    /**
     * Constructs a Matrix of integers by given array.
     *
     * @throws IllegalArgumentException In case of some of following
     * is true:
     * <ol>
     *     <li>Lengths of given rows are not equal to each other.</li>
     *     <li>Given matrix is not a square matrix.</li>
     *     <li>Size of given matrix is even.</li>
     * </ol>
     */
    public Matrix(int array[][]) throws IllegalArgumentException {
        size = array.length;
        for (int i = 0; i < size; i++) {
            if (array[i].length != size)
                throw new IllegalArgumentException("Not a square matrix is given.");
        }
        if (size % 2 == 0)
            throw new IllegalArgumentException("Size of given matrix must be odd.");

        columns = new Column[size];
        for (int i = 0; i < size; i++) {
            columns[i] = new Column();
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                columns[j].setElement(i, array[i][j]);
            }
        }
    }

    /**
     * Sort the Matrix columns by its first elements.
     */
    public void sortColumns() {
        Arrays.sort(columns);
    }

    public int getElement(int row, int column) {
        return columns[column].getElement(row);
    }

    /**
     * <p>Get the array of Matrix elements in the spiral order starting
     * in the central cell. First step is to the right, the second
     * step is to the down.</p>
     *
     * <p>Example: for this Matrix of size 5x5 (indices from top to
     * bottom, from left to right).</p>
     *
     * <table border=1 style="text-align: center; border-collapse: collapse;">
     * <tr><td>21</td> <td>22</td> <td>23</td> <td>24</td> <td>25</td></tr>
     * <tr><td>20</td> <td> 7</td> <td> 8</td> <td> 9</td> <td>10</td></tr>
     * <tr><td>19</td> <td> 6</td> <td> 1</td> <td> 2</td> <td>11</td></tr>
     * <tr><td>18</td> <td> 5</td> <td> 4</td> <td> 3</td> <td>12</td></tr>
     * <tr><td>17</td> <td>16</td> <td>15</td> <td>14</td> <td>13</td></tr>
     * </table>
     *
     * <p>The resulting array will be
     * 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25.
     * </p>
     */
    public int[] spiralBypass() {
        int[] result = new int[size * size];
        int nextIndex = 0;
        for (int loop = 0; loop <= (size / 2); loop++) {
            // Beginning of the current loop position.
            int loopStartRow;
            int loopStartColumn;
            if (loop == 0) {
                loopStartRow = loopStartColumn = size / 2;
            } else {
                loopStartRow = (size / 2) - (loop - 1);
                loopStartColumn = (size / 2) + loop;
            }
            // Numbers of step we go in each direction.
            int downSteps, leftSteps, upSteps, rightSteps;
            if (loop == 0) {
                downSteps = leftSteps = upSteps = rightSteps = 0;
            } else {
                downSteps = 2 * (loop - 1) + 1;
                leftSteps = 2 * loop;
                upSteps = 2 * loop;
                rightSteps = 2 * loop;
            }
            /* We go downSteps steps down, then leftSteps steps left,
             * then upSteps steps up, then rightSteps steps right.
             * Before each step we add the current number to the
             * answer. In the end we add the last number to the
             * answer.
             */
            int curRow = loopStartRow, curColumn = loopStartColumn;
            for (int i = 0; i < downSteps; i++) {
                result[nextIndex++] = getElement(curRow, curColumn);
                curRow++;
            }
            for (int i = 0; i < leftSteps; i++) {
                result[nextIndex++] = getElement(curRow, curColumn);
                curColumn--;
            }
            for (int i = 0; i < upSteps; i++) {
                result[nextIndex++] = getElement(curRow, curColumn);
                curRow--;
            }
            for (int i = 0; i < rightSteps; i++) {
                result[nextIndex++] = getElement(curRow, curColumn);
                curColumn++;
            }
            result[nextIndex++] = getElement(curRow, curColumn);
        }
        return result;
    }

    /**
     * Outputs the result of {@link #spiralBypass() spiralBypass} to
     * the console.
     */
    public void printSpiralBypass() {
        int[] toPrint = spiralBypass();
        for (int u : toPrint) {
            System.out.print(u);
            System.out.print(" ");
        }
        System.out.println();
    }
}
