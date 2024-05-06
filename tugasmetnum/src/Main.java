import java.util.Scanner;

public class Main {
    public static double[][] transpose(double[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        double[][] result = new double[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return result;
    }

    public static double[][] matrixMultiply(double[][] matrix1, double[][] matrix2) {
        int m1 = matrix1.length;
        int n1 = matrix1[0].length;
        int m2 = matrix2.length;
        int n2 = matrix2[0].length;
        double[][] result = new double[m1][n2];
        for (int i = 0; i < m1; i++) {
            for (int j = 0; j < n2; j++) {
                for (int k = 0; k < n1; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return result;
    }

    public static double[][] matrixInverse(double[][] matrix) {
        double det = matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1]) -
                matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0]) +
                matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
        double invdet = 1 / det;
        double[][] inverse = new double[3][3];
        inverse[0][0] = (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1]) * invdet;
        inverse[0][1] = (matrix[0][2] * matrix[2][1] - matrix[0][1] * matrix[2][2]) * invdet;
        inverse[0][2] = (matrix[0][1] * matrix[1][2] - matrix[0][2] * matrix[1][1]) * invdet;
        inverse[1][0] = (matrix[1][2] * matrix[2][0] - matrix[1][0] * matrix[2][2]) * invdet;
        inverse[1][1] = (matrix[0][0] * matrix[2][2] - matrix[0][2] * matrix[2][0]) * invdet;
        inverse[1][2] = (matrix[0][2] * matrix[1][0] - matrix[0][0] * matrix[1][2]) * invdet;
        inverse[2][0] = (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]) * invdet;
        inverse[2][1] = (matrix[0][1] * matrix[2][0] - matrix[0][0] * matrix[2][1]) * invdet;
        inverse[2][2] = (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]) * invdet;
        return inverse;
    }

    public static double[][] luDecomposition(double[][] matrix) {
        int size = matrix.length;
        double[][] L = new double[size][size];
        double[][] U = new double[size][size];

        for (int i = 0; i < size; i++) {
            L[i][i] = 1.0;
        }

        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                double total = 0.0;
                for (int k = 0; k < i; k++) {
                    total += L[i][k] * U[k][j];
                }
                U[i][j] = matrix[i][j] - total;
            }

            for (int j = i + 1; j < size; j++) {
                double total = 0.0;
                for (int k = 0; k < i; k++) {
                    total += L[j][k] * U[k][i];
                }
                L[j][i] = (matrix[j][i] - total) / U[i][i];
            }
        }

        double[][] result = new double[2][size * size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[0][i * size + j] = L[i][j];
                result[1][i * size + j] = U[i][j];
            }
        }
        return result;
    }

    public static double[] solveUsingInverse(double[][] A, double[] b) {
        double[][] A_inv = matrixInverse(A);
        double[][] bMatrix = new double[1][b.length];
        for (int i = 0; i < b.length; i++) {
            bMatrix[0][i] = b[i];
        }
        double[][] xMatrix = matrixMultiply(A_inv, transpose(bMatrix));
        double[] x = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            x[i] = xMatrix[i][0];
        }
        return x;
    }

    public static double[] solveUsingLU(double[][] A, double[] b) {
        double[][] LU = luDecomposition(A);
        double[][] L = new double[A.length][A.length];
        double[][] U = new double[A.length][A.length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A.length; j++) {
                L[i][j] = LU[0][i * A.length + j];
                U[i][j] = LU[1][i * A.length + j];
            }
        }
        double[] y = new double[b.length];
        double[] x = new double[b.length];

        for (int i = 0; i < b.length; i++) {
            double total = 0.0;
            for (int j = 0; j < i; j++) {
                total += L[i][j] * y[j];
            }
            y[i] = b[i] - total;
        }

        for (int i = b.length - 1; i >= 0; i--) {
            double total = 0.0;
            for (int j = i + 1; j < b.length; j++) {
                total += U[i][j] * x[j];
            }
            x[i] = (y[i] - total) / U[i][i];
        }

        return x;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Tentukan Ukuran Matriks A:");
        int n = scanner.nextInt();
        System.out.println("Masukkan Isi Matriks A:");
        double[][] A = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.out.print("Masukkan Baris Ke-" + (i + 1) + " (pisahin Spasi): ");
            for (int j = 0; j < n; j++) {
                A[i][j] = scanner.nextDouble();
            }
        }
        System.out.println("Masukkan Isi Vektor b:");
        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
            b[i] = scanner.nextDouble();
        }

        System.out.println("\nMetode Yang Ingin Digunakan:");
        System.out.println("1. Metode Matriks Balikan");
        System.out.println("2. Metode Dekomposisi LU Gauss");
        System.out.println("3. Metode Dekomposisi Crout");
        int choice = scanner.nextInt();

        double[] x;
        String method;
        switch (choice) {
            case 1:
                x = solveUsingInverse(A, b);
                method = "Metode Matriks Balikan";
                break;
            case 2:
                x = solveUsingLU(A, b);
                method = "Metode Dekomposisi LU Gauss";
                break;
            default:
                System.out.println("Pilihan Tidak Valid.");
                return;
        }

        System.out.println("\nSolusi Menggunakan Metode Yang Ini Ya : " + method + ":");
        for (int i = 0; i < x.length; i++) {
            System.out.println("x_" + (i + 1) + " = " + x[i]);
        }
    }
}
