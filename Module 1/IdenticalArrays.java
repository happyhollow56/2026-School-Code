import java.util.Scanner;

public class IdenticalArrays {
    public static boolean equals(int[][] m1, int[][] m2) {
        if (m1.length != m2.length) {
            return false;
        }
        for (int i = 0; i < m1.length; i++) {
            if (m1[i].length != m2[i].length) {
                return false;
            }
            for (int j = 0; j < m1[i].length; j++) {
                if (m1[i][j] != m2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int[][] readMatrix(Scanner scanner, int index) {
        int[][] m = new int[3][3];
        System.out.println("Enter 9 integers for 3x3 array " + index + ":");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m[i][j] = scanner.nextInt();
            }
        }
        return m;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[][] m1 = readMatrix(scanner, 1);
        int[][] m2 = readMatrix(scanner, 2);
        if (equals(m1, m2)) {
            System.out.println("The two arrays are identical");
        } else {
            System.out.println("The two arrays are not identical");
        }
        scanner.close();
    }
}

