package components;

import model.*;
import view.BackgroundImg;
import view.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ChessGridComponent extends BasicComponent {
    public static int chessSize;
    public static int gridSize;
    public static Color gridColor = new Color(255, 150, 50);

    private ChessPiece chessPiece;
    public int row;
    public int col;

    public ChessGridComponent(int row, int col) {
        this.setSize(gridSize, gridSize);

        this.row = row;
        this.col = col;
    }

    @Override
    public void onMouseClicked() {
        System.out.printf("%s clicked (%d, %d)\n", GameFrame.controller.getCurrentPlayer(), row, col);
        //todo: complete mouse click method

        if (GameFrame.controller.canClick(row, col)) {

            if (this.chessPiece == null) {
                GameFrame.controller.backstep = GameFrame.controller.gettop();//每一次下棋都要更新悔棋的内容
                GameFrame.controller.countScore(GameFrame.controller.getCurrentPlayer().getColor(), 1);

                this.chessPiece = GameFrame.controller.getCurrentPlayer();
                GameFrame.controller.swapPlayer();

                GameFrame.controller.setStack(getRow(), getCol(), GameFrame.controller.getCurrentPlayer());//记录每一步
                GameFrame.controller.push(GameFrame.controller.setStack(getRow(), getCol(), GameFrame.controller.getCurrentPlayer()));//入栈
                GameFrame.chessBoardPanel.setChessintarr();
                int bl1 = 0, wh1 = 0;

                for (int d = 0; d < 8; d++) {
                    for (int c = 0; c < 8; c++) {
                        if (GameFrame.chessBoardPanel.chessIntarr[d][c] == 1)
                            wh1++;
                        else if (GameFrame.chessBoardPanel.chessIntarr[d][c] == -1) {
                            bl1++;
                        }
                    }
                }
                GameFrame.statusPanel.setScoreText(bl1, wh1);
            }

        }
        repaint();
        if (!Objects.equals(GameFrame.controller.Iswin(), "NO WINS")) {//判断输赢
            String[] options = {"End"};

            JOptionPane.showOptionDialog(null, GameFrame.controller.Iswin(),
                    "Game Over!",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            System.exit(0);


            GameFrame mainFrame = new GameFrame(1000);
            mainFrame.setVisible(true);
        }
        if (GameFrame.controller.IsswapPlayer(GameFrame.controller.getCurrentPlayer())) {//判断平局
            String MSG = "The other role has no place to set.\n" + "Please set chess again.";
            String[] options = {"I see"};
            JOptionPane.showOptionDialog(null, MSG, "Play Again", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            GameFrame.controller.swapPlayer();
            repaint();

        }

    }


    public ChessPiece getChessPiece() {
        return chessPiece;
    }

    public void setChessPiece(ChessPiece chessPiece) {
        this.chessPiece = chessPiece;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void drawPiece(Graphics g) {
        g.setColor(gridColor);
        g.fillRect(1, 1, this.getWidth() - 2, this.getHeight() - 2);
        if (this.chessPiece != null) {
            g.setColor(chessPiece.getColor());
            g.fillOval((gridSize - chessSize) / 2, (gridSize - chessSize) / 2, chessSize, chessSize);
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.printComponents(g);
        drawPiece(g);
    }


}
