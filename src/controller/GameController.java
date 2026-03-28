package controller;

import view.ChessBoardPanel;
import model.ChessPiece;
import view.*;


import java.awt.*;
import java.io.*;

import javax.swing.*;
import java.util.Objects;
//import java.util.Stack;

public class GameController implements Serializable, Stack {
    static int[][] move = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    private ChessBoardPanel gamePanel;
    private StatusPanel statusPanel;
    private ChessPiece currentPlayer;
    private int blackScore;
    private int whiteScore;
    public boolean cheatMode = false, hintMode = false, changeMode = false, AIMode = false;

    private int top = -1;
    private int maxsize = 0;
    private int[][][] stackArray = new int[maxsize][8][8];
    public boolean hint = false, cheat = false;
    public int backstep = top;
    int[][] ischange = new int[8][8];

    public GameController(ChessBoardPanel gamePanel, StatusPanel statusPanel) {
        this.gamePanel = gamePanel;
        this.statusPanel = statusPanel;
        this.currentPlayer = ChessPiece.BLACK;
        blackScore = 2;
        whiteScore = 2;
        gamePanel.setChessintarr();
    }

    public int gettop() {
        return top;
    }

    public void swapPlayer() {

        currentPlayer = (currentPlayer == ChessPiece.BLACK) ? ChessPiece.WHITE : ChessPiece.BLACK;
        statusPanel.setPlayerText(currentPlayer.name());
        statusPanel.setScoreText(blackScore, whiteScore);
    }

    public int[][] initialInt() {
        int[][] arr = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i == 3 && j == 3) || (i == 4 && j == 4)) {
                    arr[i][j] = -1;
                } else if ((i == 3 && j == 4) || (i == 4 && j == 3))
                    arr[i][j] = 1;
                else arr[i][j] = 0;
            }
        }
        return arr;

    }

    public void backStep(int k) {//K是撤回的步数
        if (k == -1) {//初始棋盘不在步数范围内，如果退到最开始就直接讨论。
            this.currentPlayer = ChessPiece.BLACK;
            blackScore = 2;
            whiteScore = 2;

            statusPanel.setScoreText(2, 2);
            statusPanel.setPlayerText("BLACK");
            gamePanel.setChessGrids(initialInt());
            gamePanel.repaint();

        } else if (k >= 0) {
            int[][] set = stackArray[k];
            int[][] setpanel = new int[8][8];
            int bla = 0, whi = 0;
            for (int t = 0; t < 8; t++) {

                System.arraycopy(set[t], 0, setpanel[t], 0, 8);
            }

            if (set[8][2] == 1) {
                statusPanel.setPlayerText("WHITE");
                setCurrentPlayer(ChessPiece.WHITE);
            } else if (set[8][2] == -1) {
                statusPanel.setPlayerText("BLACK");
                setCurrentPlayer(ChessPiece.BLACK);
            }
            statusPanel.setScoreText((set[8][5]), set[8][4]);//黑白得分
            gamePanel.setChessGrids(setpanel);//画棋盘
            gamePanel.repaint();
        }
    }


    public void countScore(Color color, int i) {
        //todo: modify the countScore method

        if (color == ChessPiece.BLACK.getColor()) {
            blackScore += i;
        } else {
            whiteScore += i;//翻转后黑白数目变化可能不只是1
        }
    }

    public ChessPiece getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(ChessPiece player) {
        this.currentPlayer = player;
    }

    public ChessBoardPanel getGamePanel() {
        return gamePanel;
    }


    public void setGamePanel(ChessBoardPanel gamePanel) {
        this.gamePanel = gamePanel;
    }


    public void readFileData(String fileName) {
        try {
            File myfile = new File("res/" + fileName);
            if (!myfile.exists()) {
                JOptionPane.showMessageDialog(null, "ERROR CODE : 106", "ERROR", JOptionPane.PLAIN_MESSAGE);
            } else {
                FileReader fileReader = new FileReader(myfile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String S;
                String[] split;
                push(setStack(0, 0, getCurrentPlayer()));
                int[][] setpanel = peek();//取出栈顶元素
                for (int t = 0; t < 8; t++) {
                    S = bufferedReader.readLine();
                    for (int z = 0; z < 8; z++) {
                        split = S.split(" ");
                        if (split.length != 8) {//判断棋盘合法性
                            JOptionPane.showMessageDialog(null, "ERROR CODE : 101", "ERROR", JOptionPane.PLAIN_MESSAGE);
                            System.exit(0);
                        } else if (Integer.parseInt(split[z]) != 0 && Integer.parseInt(split[z]) != 1 && Integer.parseInt(split[z]) != -1) {
                            JOptionPane.showMessageDialog(null, "ERROR CODE : 102", "ERROR", JOptionPane.PLAIN_MESSAGE);
                            System.exit(0);//判断是否棋子被更改
                        }
                        setpanel[t][z] = Integer.parseInt(split[z]);
                    }
                }
                if (ischange(fileName))
                    JOptionPane.showMessageDialog(null, "File Changed : 106", "ERROR", JOptionPane.PLAIN_MESSAGE);
                S = bufferedReader.readLine();
                split = S.split(" ");
                if (split.length != 8)
                    JOptionPane.showMessageDialog(null, "ERROR CODE : 103", "ERROR", JOptionPane.PLAIN_MESSAGE);
                else if (Integer.parseInt(split[2]) == 1) {
                    statusPanel.setPlayerText("WHITE");
                    setCurrentPlayer(ChessPiece.WHITE);
                } else if (Integer.parseInt(split[2]) == -1) {
                    statusPanel.setPlayerText("BLACK");
                    setCurrentPlayer(ChessPiece.BLACK);
                } else {
                    JOptionPane.showMessageDialog(null, "ERROR CODE : 103", "ERROR", JOptionPane.PLAIN_MESSAGE);
                    System.exit(0);//判断角色是否更改成不合法
                }
                if (Integer.parseInt(split[3]) == 1) {
                    cheatMode = true;
                    changeMode = true;
                    hintMode = false;
                    cheat = true;//判断是否在作弊模式下棋
                } else if (Integer.parseInt(split[3]) != 0)
                    JOptionPane.showMessageDialog(null, "ERROR CODE : 106", "ERROR", JOptionPane.PLAIN_MESSAGE);
//作弊模式对应数字更改不合法
                statusPanel.setScoreText((Integer.parseInt(split[5])), Integer.parseInt(split[4]));
                gamePanel.setChessGrids(setpanel);
                gamePanel.repaint();
                fileReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeDataToFile(String fileName) {
        int x = 0;
        try {
            File myfile = new File("res/" + fileName);
            if (!myfile.exists()) {
                myfile.createNewFile();
            } else if (myfile.exists() && !Objects.equals(fileName, "") && fileName != null) {//如果没有输入的话弹出重新输入
                String[] options = {"Yes", "No"};
                x = JOptionPane.showOptionDialog(null, "Overwrite?",
                        "File exists",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            }//如果文件已经存在就弹窗是否覆盖
            if (x == 0 && !Objects.equals(fileName, "")) {
                FileWriter fileWriter = new FileWriter(myfile, false);
                gamePanel.setChessintarr();
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                for (int i = top; i >= 0; i--) {
                    for (int j = 0; j < 9; j++) {
                        for (int k = 0; k < 8; k++) {
                            bufferedWriter.write(String.valueOf(stackArray[i][j][k]) + " ");
                        }
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.newLine();
                }
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean ischange(String filename) {
        boolean is = false;
        for (int a = 0; a < 8; a++) {
            for (int b = 0; b < 8; b++) {
                ischange[a][b] = initialInt()[a][b];
            }
        }
        try {
            File myfile = new File("res/" + filename);
            FileReader fileReader;

            fileReader = new FileReader(myfile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int lines = 0;

            String S = "";
            for (; ; ) {
                lines++;
                S = bufferedReader.readLine();

                if (S == null) {
                    S = bufferedReader.readLine();
                    if (S == null){
                        S = bufferedReader.readLine();//如果连续三行空就是结束了，
                        if (S == null)
                        break;}
                }
            }

            String[] strings = new String[lines];
            fileReader.close();
            File myfile3 = new File("res/" + filename);
            FileReader fileReader3;

            fileReader3 = new FileReader(myfile3);
            BufferedReader bufferedReader3 = new BufferedReader(fileReader3);
            for (int i = 0; i < lines; i++) {
                String S1 = bufferedReader3.readLine();
                if (S1 != null&& !S1.equals("")) {

                    String[] str = S1.split(" ");
                    if (str.length != 8) {
                        System.out.println(S1);
                        System.out.println(str.length);
                        JOptionPane.showMessageDialog(null, "File Changed : 104", "ERROR", JOptionPane.PLAIN_MESSAGE);
                        break;
                    }
                }
            }
            fileReader3.close();//判断是否文件被增减字符
            File myfile2 = new File("res/" + filename);
            FileReader fileReader2;

            fileReader2 = new FileReader(myfile2);
            BufferedReader bufferedReader2 = new BufferedReader(fileReader2);
            int m = 0;
            for (int i = 1; i < lines; i++) {
                S = bufferedReader2.readLine();
                if (i % 10 == 9 && S != null&& !S.equals("")) {
                    strings[m] = S;
                    m++;
                }
            }
            System.out.println(lines);
            for (int i = strings.length - 1; i >= 0; i--) {
                if (strings[i] != null) {
                    System.out.print(strings[i] + " ");
                    String[] spl = strings[i].split(" ");
                    ischange = isChange(Integer.parseInt(spl[0]), Integer.parseInt(spl[1]), -Integer.parseInt(spl[2]), Integer.parseInt(spl[3]) == 1, ischange);
                }
            }
            fileReader2.close();
            File myfile1 = new File("res/" + filename);
            FileReader fileReader1;
            fileReader1 = new FileReader(myfile1);
            BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
            for (int i = 0; i < 8; i++) {
                S = bufferedReader1.readLine();
                String[] spl1 = S.split(" ");
                for (int v = 0; v < 8; v++)
                    if (ischange[i][v] != Integer.parseInt(spl1[v])) {
                        is = true;
                        break;

                    }
            }
            fileReader1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(ischange[i][j]);
            }
            System.out.print("\n");
        } System.out.print("\n");
        return is;

    }

    public int[][] isChange(int x, int y, int COlor, boolean isCheatMode, int[][] map) {//下每一步以检验是否被改过，并且检验是否作弊模式
        int i, x1, y1, x2, y2;
        boolean sign;
        for (i = 0; i < 8; i++) {
            sign = isCheatMode;
            x1 = x + move[i][0];
            y1 = y + move[i][1];
            while (0 <= x1 && x1 < 8 && 0 <= y1 && y1 < 8 && map[x1][y1] != 0) {
                if (map[x1][y1] == -COlor)
                    sign = true;
                else {
                    if (sign) {
                        x1 -= move[i][0];
                        y1 -= move[i][1];
                        x2 = x + move[i][0];
                        y2 = y + move[i][1];
                        while (((x <= x2 && x2 <= x1) || (x1 <= x2 && x2 <= x)) && ((y <= y2 && y2 <= y1) || (y1 <= y2 && y2 <= y))) {
                            map[x2][y2] = COlor;
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
        map[x][y] = COlor;
        return map;
    }

    public boolean isFilename(String filename) {//判断文件名是否合法
        boolean is = true;
        if (Objects.equals(filename, "")) return false;
        else if (filename == null) return false;
        else if (!filename.contains(".")) return false;
        String[] split = filename.split("\\.");
        if (!Objects.equals(split[split.length - 1], "txt")) {//文件后缀应该是txt
            is = false;
        }
        return is;
    }

    public String Iswin() {
        return gamePanel.Iswin();
    }

    public boolean IsswapPlayer(ChessPiece currentPlayer) {//用于在一方无子可下但是没分输赢时换角色下
        return gamePanel.IsswapPlayer(currentPlayer);
    }

    public boolean canClick(int row, int col) {
        return gamePanel.canClickGrid(row, col, currentPlayer, cheatMode);
    }

    public int[][] setStack(int row, int col, ChessPiece currentPlayer) {//设置栈点数组，数字化记录
        int black = 0, white = 0;
        gamePanel.setChessintarr();
        int[][] temp = new int[9][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(gamePanel.chessIntarr[i], 0, temp[i], 0, 8);
        }//记下棋盘
        temp[8][0] = row;
        temp[8][1] = col;//记下下的位置
        if (currentPlayer.getColor() == Color.BLACK)
            temp[8][2] = -1;
        else if (currentPlayer.getColor() == Color.WHITE)
            temp[8][2] = 1;//记下下的人
        if (cheatMode)
            temp[8][3] = 1;
        else temp[8][3] = 0;
        for (int i = 0; i < ChessBoardPanel.getCHESS_COUNT(); i++) {
            for (int j = 0; j < ChessBoardPanel.getCHESS_COUNT(); j++) {
                if (gamePanel.chessIntarr[i][j] == 1) {//1表示白色，-1表示黑色
                    white++;
                } else if (gamePanel.chessIntarr[i][j] == -1) {
                    black++;
                }
            }
        }
        temp[8][4] = white;
        temp[8][5] = black;
        return temp;
    }

    @Override
    public boolean isEmpty() {
        return top == -1;
    }

    @Override
    public void push(int[][] data) {//入栈
        if (isFull()) {
            maxsize = maxsize + 10;
            int[][][] newStackArray = new int[maxsize][9][8];
            System.arraycopy(stackArray, 0, newStackArray, 0, stackArray.length);
            stackArray = newStackArray;
        }
        top++;
        stackArray[top] = data;
    }

    @Override
    public int[][] peek() {//查元素

        return stackArray[top];

    }


    @Override
    public boolean isFull() {
        return top == maxsize - 1;
    }
}
