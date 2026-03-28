package view;

import components.ChessGridComponent;
import model.ChessPiece;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class ChessBoardPanel extends JPanel {
    private static final int CHESS_COUNT = 8;
    private ChessGridComponent[][] chessGrids;
    public int[][] chessIntarr;
    static int[][] dir = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    ChessPiece AIplayer;

    public ChessBoardPanel(int width, int height) {
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.setBackground(Color.BLACK);
        int length = Math.min(width, height);
        this.setSize(length, length);
        ChessGridComponent.gridSize = length / CHESS_COUNT;
        ChessGridComponent.chessSize = (int) (ChessGridComponent.gridSize * 0.8);
        System.out.printf("width = %d height = %d gridSize = %d chessSize = %d\n",
                width, height, ChessGridComponent.gridSize, ChessGridComponent.chessSize);
        initialChessGrids();//return empty chessboard
        initialGame();//add initial four chess
        repaint();
    }

    public static int getCHESS_COUNT() {
        return CHESS_COUNT;
    }

    public void setChessintarr() {//以整型数组表示棋盘
        chessIntarr = new int[CHESS_COUNT][CHESS_COUNT];
        for (int i = 0; i < CHESS_COUNT; i++) {
            for (int j = 0; j < CHESS_COUNT; j++) {
                if (chessGrids[i][j].getChessPiece() != null) {
                    if (chessGrids[i][j].getChessPiece().getColor() == Color.BLACK)
                        chessIntarr[i][j] = -1;//-1表示黑色
                    else if (chessGrids[i][j].getChessPiece().getColor() == Color.WHITE)
                        chessIntarr[i][j] = 1;
                } else
                    chessIntarr[i][j] = 0;
            }
        }
    }

    /**
     * set an empty chessboard
     */
    public void initialChessGrids() {//初始化棋盘
        chessGrids = new ChessGridComponent[CHESS_COUNT][CHESS_COUNT];
        for (int i = 0; i < CHESS_COUNT; i++) {
            for (int j = 0; j < CHESS_COUNT; j++) {
                ChessGridComponent gridComponent = new ChessGridComponent(i, j);
                gridComponent.setLocation(j * ChessGridComponent.gridSize, i * ChessGridComponent.gridSize);
                chessGrids[i][j] = gridComponent;
                this.add(chessGrids[i][j]);
            }
        }
    }

    public void setChessGrids(int[][] set) {//通过整形数组画棋盘，可以记住棋盘
        for (int m = 0; m < 8; m++) {
            for (int n = 0; n < 8; n++) {
                if (set[m][n] == 1)
                    chessGrids[m][n].setChessPiece(ChessPiece.WHITE);
                else if (set[m][n] == -1)
                    chessGrids[m][n].setChessPiece(ChessPiece.BLACK);
                else if (set[m][n] == 0)
                    chessGrids[m][n].setChessPiece(null);
            }
        }
    }

    /**
     * initial origin four chess
     */
    public void initialGame() {
        chessGrids[3][3].setChessPiece(ChessPiece.BLACK);
        chessGrids[3][4].setChessPiece(ChessPiece.WHITE);
        chessGrids[4][3].setChessPiece(ChessPiece.WHITE);
        chessGrids[4][4].setChessPiece(ChessPiece.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }


    public boolean HintClickGrid(ChessPiece currentPlayer, boolean HintMode, boolean win) {//判断win和hint，同一段代码两用，通过win改变用处
        boolean allcheck = false;
        if (HintMode) {//如果在提示状态下
            setChessintarr();
            int[][] arr = new int[8][8];
            int[][] arr4 = new int[8][8];
 //1表示白色，-1表示黑色
            for (int k = 0; k < 8; k++) {
                for (int l = 0; l < 8; l++) {
                    arr[k][l] = chessIntarr[k][l];
                    if (currentPlayer.getColor() == Color.WHITE) {
                        arr[k][l] = -arr[k][l];
                    }
                    arr4[k][l] = arr[k][l];
                }
            }
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (arr[x][y] == -1) {
                        for (int s = 0; s < 8; s++) {

                            int p = dir[s][0];
                            int q = dir[s][1];
                            if ((x + p < 8) && (y + q < 8) && (x + p >= 0) && (y + q >= 0)) {
                                int tx = x + p;
                                int ty = y + q;
                                if (arr[tx][ty] == 1) {
                                    for (int c = tx, d = ty; (c + p < 8) && (c + p >= 0) && (d + q < 8) && (d + q >= 0); c += p, d += q) {
                                        if ((c + p >= 0) && (d + q >= 0) && (c + p < 8) && (d + q < 8) && (arr[c + p][d + q] != 1)) {
                                            if (arr[c + p][d + q] == 0) arr4[c + p][d + q] = 1;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            int h = 64;
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    if ((arr[j][k] == 0) && (arr4[j][k] == 1)) {
                        arr4[j][k] = 1;//找出的可下子
                    } else {
                        arr4[j][k] = 0;
                        h--;
                    }
                }
            }
            if (h > 0) {
                for (int j = 0; j < 8; j++) {
                    for (int k = 0; k < 8; k++) {
                        if (arr4[j][k] == 1) {
                            if (!win) {
                                chessGrids[j][k].setChessPiece(ChessPiece.BLUE);

                            }//如果在判断输赢时不需要标蓝色提示
                            allcheck = true;
                        }
                        repaint();
                    }
                }
            }
        } else {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    if (chessGrids[j][k].getChessPiece() != null && chessGrids[j][k].getChessPiece().getColor() == Color.BLUE)
                        chessGrids[j][k].setChessPiece(null);
                }
            }
            repaint();
            setChessintarr();
        }
        return allcheck;
    }

    public String Iswin() {//判断输赢
        String Whowins = "NO WINS";
        int sum = 0;//判断是否平局，
        setChessintarr();
        for (int a = 0; a < 8; a++) {
            for (int b = 0; b < 8; b++) {
                sum += chessIntarr[a][b];
            }
        }//如果黑白都不能下，就比谁的子多，如果一样多，就平局，如果有一方不能下，另一方能下，就换对方下。
        if (!HintClickGrid(ChessPiece.BLACK, true, true) && !HintClickGrid(ChessPiece.WHITE, true, true)) {
            if (sum > 0)
                Whowins = "WHITE WINS! ";
            else if (sum < 0)
                Whowins = "BLACK WINS! ";
            else
                Whowins = "This is a DRAW! ";
        }
        return Whowins;
    }

    public boolean IsswapPlayer(ChessPiece currentPlayer) {//如果在一方无子另一方有子可下，就换角色
        ChessPiece enemy = ChessPiece.WHITE;//设定对方颜色
        if (currentPlayer == ChessPiece.WHITE)
            enemy = ChessPiece.BLACK;
        return !HintClickGrid(currentPlayer, true, true) && HintClickGrid(enemy, true, true);
        //Hint状态下如果不能下也就是无子可下，如果自己不能下，对方能下，应该跳过，直接对方下
        //应该换对方下，

    }

    public boolean canClickGrid(int row, int col, ChessPiece currentPlayer, boolean cheatMode) {//判断是否能下，和作弊模式共用代码，
//1表示白色，-1表示黑色
        Color enemycolor = ChessPiece.WHITE.getColor();//设定对方颜色
        if (currentPlayer.getColor() == Color.WHITE)
            enemycolor = ChessPiece.BLACK.getColor();
        boolean can = false;

        if (chessGrids[row][col].getChessPiece() != null) {
            // 如果当前不是空的返回0值
            return false;
        }
        int i, x1, y1, x2, y2, temp;
        boolean sign;
        for (i = 0; i < 8; i++) {//对8个方向循环
            temp = 0;
            sign = cheatMode;//如果作弊模式的话可以直接下，非作弊模式需要判断是否可以下
            x1 = row + dir[i][0];
            y1 = col + dir[i][1];

            while (0 <= x1 && x1 < 8 && 0 <= y1 && y1 < 8 && chessGrids[x1][y1].getChessPiece() != null) {
                if (chessGrids[x1][y1].getChessPiece().getColor() == enemycolor) {
                    sign = true;
                    temp++;
                } else {
                    if (sign) {
                        x1 -= dir[i][0];
                        y1 -= dir[i][1];
                        x2 = row + dir[i][0];
                        y2 = col + dir[i][1];
                        while (((row <= x2 && x2 <= x1) || (x1 <= x2 && x2 <= row)) && ((col <= y2 && y2 <= y1) || (y1 <= y2 && y2 <= col))) {
                            GameFrame.controller.countScore(currentPlayer.getColor(), 1);
                            chessGrids[x2][y2].setChessPiece(currentPlayer);
                            GameFrame.controller.countScore(enemycolor, -1);//棋子的翻转
                            x2 += dir[i][0];
                            y2 += dir[i][1];

                        }
                        repaint();
                        can = true;
                    }
                    break;
                }
                x1 += dir[i][0];
                y1 += dir[i][1];
            }

        }
        // 返回可吃棋数
        setChessintarr();
        return can || cheatMode;
    }

    public boolean AI1(boolean AI1, ChessPiece currentPlayer, boolean win) {
        Color enemycolor = ChessPiece.WHITE.getColor();//设定对方颜色
        if (currentPlayer.getColor() == Color.WHITE)
            enemycolor = ChessPiece.BLACK.getColor();
        int bl = 0, wh = 0;
        boolean allcheck = false;
        AIplayer = currentPlayer;
        if (AI1 && !win) {
            setChessintarr();
            int[][] arr = new int[8][8];
            int[][] arr4 = new int[8][8];
            int[][] reminder = new int[8][8];
            Map<Integer, Integer> choice = new HashMap<>();
            List<Integer> xline = new ArrayList<>();
            List<Integer> yline = new ArrayList<>();
            for (int k = 0; k < 8; k++) {
                for (int l = 0; l < 8; l++) {
                    arr[k][l] = chessIntarr[k][l];
                    reminder[k][l] = 0;
                    if (currentPlayer.getColor() == Color.WHITE) {
                        arr[k][l] = -arr[k][l];
                    }
                    arr4[k][l] = arr[k][l];
                }
            }
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (arr[x][y] == -1) {
                        for (int s = 0; s < 8; s++) {

                            int p = dir[s][0];
                            int q = dir[s][1];
                            if ((x + p < 8) && (y + q < 8) && (x + p >= 0) && (y + q >= 0)) {
                                int tx = x + p;
                                int ty = y + q;
                                if (arr[tx][ty] == 1) {
                                    for (int c = tx, d = ty; (c + p < 8) && (c + p >= 0) && (d + q < 8) && (d + q >= 0); c += p, d += q) {
                                        if ((c + p >= 0) && (d + q >= 0) && (c + p < 8) && (d + q < 8) && (arr[c + p][d + q] != 1)) {
                                            if (arr[c + p][d + q] == 0) arr4[c + p][d + q] = 1;//记住变化的棋子，
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            int h = 64;
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    if ((arr[j][k] == 0) && (arr4[j][k] == 1)) {
                        arr4[j][k] = 1;//找出的可下子
                    } else {
                        arr4[j][k] = 0;
                        h--;
                    }
                }
            }
            if (h > 0) {
                int a = 0;
                for (int j = 0; j < 8; j++) {
                    for (int k = 0; k < 8; k++) {
                        if (arr4[j][k] == 1) {
                            reminder[j][k] = 1;
                            xline.add(j);
                            yline.add(k);
                            //如果在判断输赢时不需要标蓝色提示
                            allcheck = true;
                        }
                        repaint();
                    }
                }
            }
            Random random = new Random();
            int rad = random.nextInt(xline.size());
            chessGrids[xline.get(rad)][yline.get(rad)].setChessPiece(currentPlayer);
            boolean can = false;

            int i, x1, y1, x2, y2, temp;
            boolean sign = false;
            for (i = 0; i < 8; i++) {//对8个方向循环
                temp = 0;
                x1 = xline.get(rad) + dir[i][0];
                y1 = yline.get(rad) + dir[i][1];

                while (0 <= x1 && x1 < 8 && 0 <= y1 && y1 < 8 && chessGrids[x1][y1].getChessPiece() != null) {
                    if (chessGrids[x1][y1].getChessPiece().getColor() == enemycolor) {
                        sign = true;
                        temp++;
                    } else {
                        if (sign) {
                            x1 -= dir[i][0];
                            y1 -= dir[i][1];
                            x2 = xline.get(rad) + dir[i][0];
                            y2 = yline.get(rad) + dir[i][1];
                            while (((xline.get(rad) <= x2 && x2 <= x1) || (x1 <= x2 && x2 <= xline.get(rad))) && ((yline.get(rad) <= y2 && y2 <= y1) || (y1 <= y2 && y2 <= yline.get(rad)))) {
                                GameFrame.controller.countScore(currentPlayer.getColor(), 1);
                                chessGrids[x2][y2].setChessPiece(currentPlayer);
                                GameFrame.controller.countScore(enemycolor, -1);//棋子的翻转
                                x2 += dir[i][0];
                                y2 += dir[i][1];
                            }
                            repaint();
                            can = true;
                        }
                        break;
                    }
                    x1 += dir[i][0];
                    y1 += dir[i][1];
                }
            }
            // 返回可吃棋数


        }
        GameFrame.controller.swapPlayer();
        setChessintarr();
        int bl1 = 0, wh1 = 0;

        for (int d = 0; d < 8; d++) {
            for (int c = 0; c < 8; c++) {
                if (chessIntarr[d][c] == 1)
                    wh1++;
                else if (chessIntarr[d][c] == -1) {
                    bl1++;
                }
            }
        }
        GameFrame.statusPanel.setScoreText(bl1, wh1);
        repaint();
        return allcheck;
    }

    public ChessPiece getAIplayer() {
        return AIplayer;
    }

    public boolean AI2(boolean AI1, ChessPiece currentPlayer, boolean win) {//由于时间问题，代码段长
        Color enemycolor = ChessPiece.WHITE.getColor();//设定对方颜色
        if (currentPlayer.getColor() == Color.WHITE)
            enemycolor = ChessPiece.BLACK.getColor();
        boolean allcheck = false;
        AIplayer = currentPlayer;
        if (AI1 && !win) {
            setChessintarr();
            int[][] arr = new int[8][8];
            int[][] arr4 = new int[8][8];
            int[][] reminder = new int[8][8];
            Map<Integer, Integer> choice = new HashMap<>();
            List<Integer> xline = new ArrayList<>();
            List<Integer> yline = new ArrayList<>();
            for (int k = 0; k < 8; k++) {
                for (int l = 0; l < 8; l++) {
                    arr[k][l] = chessIntarr[k][l];
                    reminder[k][l] = 0;
                    if (currentPlayer.getColor() == Color.WHITE) {
                        arr[k][l] = -arr[k][l];
                    }
                    arr4[k][l] = arr[k][l];
                }
            }
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (arr[x][y] == -1) {
                        for (int s = 0; s < 8; s++) {
                            int p = dir[s][0];
                            int q = dir[s][1];
                            if ((x + p < 8) && (y + q < 8) && (x + p >= 0) && (y + q >= 0)) {
                                int tx = x + p;
                                int ty = y + q;
                                if (arr[tx][ty] == 1) {
                                    for (int c = tx, d = ty; (c + p < 8) && (c + p >= 0) && (d + q < 8) && (d + q >= 0); c += p, d += q) {
                                        if ((c + p >= 0) && (d + q >= 0) && (c + p < 8) && (d + q < 8) && (arr[c + p][d + q] != 1)) {
                                            if (arr[c + p][d + q] == 0) arr4[c + p][d + q] = 1;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            int h = 64;
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    if ((arr[j][k] == 0) && (arr4[j][k] == 1)) {
                        arr4[j][k] = 1;//找出的可下子
                    } else {
                        arr4[j][k] = 0;
                        h--;
                    }
                }
            }
            if (h > 0) {
                int a = 0;
                for (int j = 0; j < 8; j++) {
                    for (int k = 0; k < 8; k++) {
                        if (arr4[j][k] == 1) {

                            reminder[j][k] = 1;
                            xline.add(j);
                            yline.add(k);
                            //如果在判断输赢时不需要标蓝色提示
                            allcheck = true;
                        }
                        repaint();
                    }
                }
            }
            Random random = new Random();
            int rad = random.nextInt(xline.size());
            for (int a = 0; a < xline.size(); a++) {
                if (xline.get(a).equals(0) && yline.get(a).equals(0)) {
                    rad = a;
                    break;
                }
                if (xline.get(a).equals(7) && yline.get(a).equals(0)) {
                    rad = a;
                    break;
                }
                if (xline.get(a).equals(0) && yline.get(a).equals(7)) {
                    rad = a;
                    break;
                }
                if (xline.get(a).equals(7) && yline.get(a).equals(7)) {
                    rad = a;
                    break;
                }
            }
            chessGrids[xline.get(rad)][yline.get(rad)].setChessPiece(currentPlayer);
            int i, x1, y1, x2, y2;
            boolean sign = false;
            for (i = 0; i < 8; i++) {//对8个方向循环
                x1 = xline.get(rad) + dir[i][0];
                y1 = yline.get(rad) + dir[i][1];

                while (0 <= x1 && x1 < 8 && 0 <= y1 && y1 < 8 && chessGrids[x1][y1].getChessPiece() != null) {
                    if (chessGrids[x1][y1].getChessPiece().getColor() == enemycolor) {
                        sign = true;
                    } else {
                        if (sign) {
                            x1 -= dir[i][0];
                            y1 -= dir[i][1];
                            x2 = xline.get(rad) + dir[i][0];
                            y2 = yline.get(rad) + dir[i][1];
                            while (((xline.get(rad) <= x2 && x2 <= x1) || (x1 <= x2 && x2 <= xline.get(rad))) && ((yline.get(rad) <= y2 && y2 <= y1) || (y1 <= y2 && y2 <= yline.get(rad)))) {
                                GameFrame.controller.countScore(currentPlayer.getColor(), 1);
                                chessGrids[x2][y2].setChessPiece(currentPlayer);
                                GameFrame.controller.countScore(enemycolor, -1);//棋子的翻转
                                x2 += dir[i][0];
                                y2 += dir[i][1];
                            }
                            repaint();
                        }
                        break;
                    }
                    x1 += dir[i][0];
                    y1 += dir[i][1];
                }
            }
        }
        GameFrame.controller.swapPlayer();
        setChessintarr();
        int bl1 = 0, wh1 = 0;
        for (int d = 0; d < 8; d++) {
            for (int c = 0; c < 8; c++) {
                if (chessIntarr[d][c] == 1)
                    wh1++;
                else if (chessIntarr[d][c] == -1) {
                    bl1++;
                }
            }
        }
        GameFrame.statusPanel.setScoreText(bl1, wh1);
        return allcheck;
    }
}






