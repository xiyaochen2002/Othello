package  playersystem;
import java.util.Scanner;

public class Main_35 {

    static int[][] move = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    static int[][] map = new int[8][8];

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int N, COLOR;
        N = input.nextInt();
        COLOR = input.nextInt();
        int[] arr3 = new int[N];
        arr3[0] = COLOR;
        for (int b = 1; b < N; b++) {
            arr3[b] = -arr3[b - 1];
        }


        int[][] arr1 = new int[N][2];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                map[i][j] = input.nextInt();


            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < 2; j++) {
                arr1[i][j] = input.nextInt();


            }
        }

        for (int i = 0; i < N; i++) {

            if(judge(arr1[i][0],arr1[i][1],arr3[i])==0)
                break;
            draw(arr1[i][0],arr1[i][1],arr3[i]);
        }
        for (int j = 0; j < 8; j++) {
            for (int k = 0; k < 8; k++) {
                System.out.printf("%3d", map[j][k]);
            }
            System.out.print('\n');
        }
    }
    static int judge(int x, int y,  int COLOR) // 判断当前是否可以落下，同draw函数
    {
        if (map[x][y] != 0)      // 如果当前不是空的返回0值
            return 0;

        int i, x1, y1;
        int n = 0, sign;
        for (i = 0; i < 8; i++) {
            sign = 0;
            x1 = x + move[i][0];
            y1 = y + move[i][1];
            while (0 <= x1 && x1 < 8 && 0 <= y1 && y1 < 8 && map[x1][y1] != 0) {
                if (map[x1][y1] == -COLOR)
                    sign++;
                else {
                    n += sign;
                    break;
                }
                x1 += move[i][0];
                y1 += move[i][1];
            }
        }
        return n;  // 返回可吃棋数
    }
    static void draw(int x, int y, int COLOR) // 下当前子
    {
        // 敌方子
        int i, x1, y1, x2, y2;
        boolean sign;
        for (i = 0; i < 8; i++)
        {
            sign = false;
            x1 = x + move[i][0];
            y1 = y + move[i][1];
            while (0 <= x1 && x1 < 8 && 0 <= y1 && y1 < 8 && map[x1][y1]!=0)
            {
                if(map[x1][y1] == -COLOR)
                    sign = true;
                else
                {
                    if(sign)
                    {
                        x1 -= move[i][0];
                        y1 -= move[i][1];
                        x2 = x + move[i][0];
                        y2 = y + move[i][1];
                        while (((x <= x2 && x2 <= x1) || (x1 <= x2 && x2 <= x)) && ((y <= y2 && y2 <= y1) || (y1 <= y2 && y2 <= y)))
                        {
                            map[x2][y2] = COLOR;
                            x2 += move[i][0];
                            y2 += move[i][1];
                        }
                    }
                    break;
                }
                x1 += move[i][0];
                y1 += move[i][1];
            }
        }
        map[x][y] = COLOR;
    }
}
