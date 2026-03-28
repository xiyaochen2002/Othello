package  playersystem;
import java.util.Scanner;

public class Main_34 {

    static int[][] dir = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int N;
        N = input.nextInt();
        int[][][] arr = new int[N][8][8];
        int[][][] arr4 = new int[N][8][8];
        int[] arr1 = new int[N];
        for (int i = 0; i < N; i++) {

            arr1[i] = input.nextInt();

            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    arr[i][j][k] = input.nextInt();
                    if (arr1[i] == 1) {
                        arr[i][j][k] = -arr[i][j][k];
                    }
                    arr4[i][j][k] = arr[i][j][k];
                }
            }
        }


        for (int i = 0; i < N; i++) {

            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (arr[i][x][y] == -1) {
                        for (int s = 0; s < 8; s++) {

                            int p = dir[s][0];
                            int q = dir[s][1];
                            if ((x + p < 8) && (y + q < 8) && (x + p >= 0) && (y + q >= 0)) {
                                int tx = x + p;
                                int ty = y + q;
                                if (arr[i][tx][ty] == 1) {
                                    for (int c = tx, d = ty; (c + p < 8) && (c + p >= 0) && (d + q < 8) && (d + q >= 0); c += p, d += q) {

                                        if ((c + p >= 0) && (d + q >= 0) && (c + p < 8) && (d + q < 8) && (arr[i][c + p][d + q] != 1)) {


                                            if (arr[i][c + p][d + q] == 0) arr4[i][c + p][d + q] = 1;

                                            break;

                                        }

                                    }

                                }
                            }

                        }

                    }
                }
            }

        }


        for (int i = 0; i < N; i++) {
            int h = 64;

            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {


                    if ((arr[i][j][k] == 0) && (arr4[i][j][k] == 1)) {
                        arr4[i][j][k] = 1;

                    } else {
                        arr4[i][j][k] = 0;
                        h--;
                    }
                }
            }


            if (h <= 0)
                System.out.printf("%d\n", -1);
            else {
                for (int j = 0; j < 8; j++) {
                    for (int k = 0; k < 8; k++) {
                        System.out.printf("%3d", arr4[i][j][k]);
                    }
                    System.out.print('\n');
                }


            }
        }
    }
}