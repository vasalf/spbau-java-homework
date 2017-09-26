import ru.spbau.alferov.javahw.matrices.Matrix;

public class Main {
    public static void main(String[] argv) {
        int[][] cur = {{3,2,1},{4,9,8},{5,6,7}};
        Matrix m = new Matrix(cur);
        m.sortColumns();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(m.getElement(i, j));
                System.out.print(" ");
            }
            System.out.println();
        }

        m.printSpiralBypass();
    }
}
