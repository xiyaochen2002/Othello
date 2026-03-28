package view;
import controller.GameController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class GameFrame extends JFrame {
    public static GameController controller;
    public static ChessBoardPanel chessBoardPanel;
    public static StatusPanel statusPanel;
    private boolean isIA = false;
    boolean hard = true, easy = true;

    int delay = 2000;
    ActionListener taskPerformer = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            DoLoop();
        }
    };

    void DoLoop() {
        if (controller.AIMode) {
            System.out.println(123);
            if (controller.getCurrentPlayer() == chessBoardPanel.AIplayer) {
                chessBoardPanel.AI1(controller.AIMode, controller.getCurrentPlayer(), false);
            }
        }
    }

    JButton CheatBtn = new JButton(new ImageIcon("res/cheat_normal.png"));
    JButton HintBtn = new JButton(new ImageIcon("res/hint_normal.png"));
    JButton AIeasyBtn = new JButton(new ImageIcon("res/easy_normal.png"));
    JButton AIdiffiBtn = new JButton(new ImageIcon("res/hard_normal.png"));

    public GameFrame(int frameSize) {
        this.setTitle("2021F CS102A Project Reversi");
        this.setLayout(null);

        //获取窗口边框的长度，将这些值加到主窗口大小上，这能使窗口大小和预期相符
        Insets inset = this.getInsets();
        this.setSize(frameSize + inset.left + inset.right, frameSize + inset.top + inset.bottom);

        this.setLocationRelativeTo(null);


        chessBoardPanel = new ChessBoardPanel((int) (this.getWidth() * 0.8), (int) (this.getHeight() * 0.7));
        chessBoardPanel.setLocation((this.getWidth() - chessBoardPanel.getWidth()) / 2, (this.getHeight() - chessBoardPanel.getHeight()) / 3);

        statusPanel = new StatusPanel((int) (this.getWidth() * 0.8), (int) (this.getHeight() * 0.1));

        statusPanel.setLocation((this.getWidth() - chessBoardPanel.getWidth()) / 2, 0);

        controller = new GameController(chessBoardPanel, statusPanel);
        controller.setGamePanel(chessBoardPanel);

        this.add(chessBoardPanel);
        this.add(statusPanel);


        JButton restartBtn = new JButton();
        restartBtn.setSize(120, 50);
        restartBtn.setBorderPainted(false);
        restartBtn.setIcon(new ImageIcon("res/restart_normal.png"));
        restartBtn.setPressedIcon(new ImageIcon("res/restart_press.png"));
        restartBtn.setLocation((this.getWidth() - chessBoardPanel.getWidth()) / 2, (this.getHeight() + chessBoardPanel.getHeight()) / 2);
        add(restartBtn);
        restartBtn.addActionListener(e -> {//重新初始化界面
            this.remove(chessBoardPanel);
            chessBoardPanel = new ChessBoardPanel((int) (this.getWidth() * 0.8), (int) (this.getHeight() * 0.7));
            chessBoardPanel.setLocation((this.getWidth() - chessBoardPanel.getWidth()) / 2, (this.getHeight() - chessBoardPanel.getHeight()) / 3);
            controller = new GameController(chessBoardPanel, statusPanel);
            controller.setGamePanel(chessBoardPanel);
            controller.changeMode = controller.cheatMode = controller.hintMode = false;
            CheatBtn.setIcon(new ImageIcon("res/cheat_normal.png"));
            HintBtn.setIcon(new ImageIcon("res/hint_normal.png"));//这两个按钮都应该在正常状态
            this.add(chessBoardPanel);
            statusPanel.setScoreText(2, 2);
            statusPanel.setPlayerText("BLACK");
            chessBoardPanel.repaint();
        });

        JButton loadGameBtn = new JButton();
        loadGameBtn.setSize(120, 50);
        loadGameBtn.setBorderPainted(false);
        loadGameBtn.setIcon(new ImageIcon("res/load_normal.png"));
        loadGameBtn.setPressedIcon(new ImageIcon("res/load_press.png"));
        loadGameBtn.setLocation(restartBtn.getX() + restartBtn.getWidth() + 20, restartBtn.getY());
        add(loadGameBtn);
        loadGameBtn.addActionListener(e -> {
            System.out.println("clicked Load Btn");//加载存档
            String filePath = JOptionPane.showInputDialog(this, "input the path here");

            if (JOptionPane.YES_OPTION == 0) {
                if (Objects.equals(filePath, "")) {//如果没有输入
                    String msg = "Please input filename!";
                    JOptionPane.showMessageDialog(null, msg, "NULL Filename", JOptionPane.PLAIN_MESSAGE);
                } else if (!controller.isFilename(filePath))//如果输入格式不对
                    JOptionPane.showMessageDialog(null, "ERROR CODE : 104", "Re-input", JOptionPane.PLAIN_MESSAGE);
                else {
                    controller.readFileData(filePath);
                    if (controller.cheat)//如果加载出来是作弊模式就自动按下作弊键
                        CheatBtn.setIcon(new ImageIcon("res/cheat_press.png"));

                }
            }

        });

        JButton saveGameBtn = new JButton();
        saveGameBtn.setSize(120, 50);
        saveGameBtn.setBorderPainted(false);
        saveGameBtn.setIcon(new ImageIcon("res/save_normal.png"));
        saveGameBtn.setPressedIcon(new ImageIcon("res/save_press.png"));
        saveGameBtn.setLocation(loadGameBtn.getX() + restartBtn.getWidth() + 20, restartBtn.getY());
        add(saveGameBtn);
        saveGameBtn.addActionListener(e -> {
            if (!controller.changeMode && !controller.hintMode && !controller.cheatMode) {//hint和cheat不能同时进行，change表示是否普通模式
                System.out.println("clicked Save Btn");//改成存档
                String filePath = JOptionPane.showInputDialog(this, "input the path here");

                if (!controller.isFilename(filePath))
                    JOptionPane.showMessageDialog(null, "ERROR CODE : 104", "Re-input", JOptionPane.PLAIN_MESSAGE);
                else  if (JOptionPane.YES_OPTION == 0)
                    controller.writeDataToFile(filePath);
            } else if (controller.hintMode && controller.changeMode)//在特殊模式进行中不能直接存棋盘
                JOptionPane.showMessageDialog(null, "Please turn off Hint!", "Hint-ON", JOptionPane.PLAIN_MESSAGE);
            else if (controller.cheatMode && controller.changeMode)
                JOptionPane.showMessageDialog(null, "Please turn off Cheat!", "Cheat-ON", JOptionPane.PLAIN_MESSAGE);
        });


        JButton backGameBtn = new JButton();
        backGameBtn.setSize(120, 50);
        backGameBtn.setBorderPainted(false);
        backGameBtn.setIcon(new ImageIcon("res/back_normal.png"));//按下按钮的图片变化
        backGameBtn.setPressedIcon(new ImageIcon("res/back_press.png"));
        // 设置按钮被点击时的图片
        backGameBtn.setLocation(saveGameBtn.getX() + loadGameBtn.getWidth() + 20, restartBtn.getY());
        add(backGameBtn);
        backGameBtn.addActionListener(e -> {
            controller.backStep(controller.backstep);
            controller.backstep--;
        });

        CheatBtn.setSize(120, 50);
        CheatBtn.setBorderPainted(false);
        CheatBtn.setLocation(backGameBtn.getX() + restartBtn.getWidth() + 20, restartBtn.getY());
        add(CheatBtn);
        CheatBtn.addActionListener(e -> {
            //controller.cheatMode//改成存档
            if (!controller.hintMode && !controller.changeMode && !controller.cheatMode && !controller.AIMode) {//如果正常模式下
                controller.cheatMode = true;
                controller.changeMode = true;
                CheatBtn.setIcon(new ImageIcon("res/cheat_press.png"));
            } else if (controller.cheatMode) {
                controller.cheatMode = false;//如果已经在作弊模式下
                controller.changeMode = false;
                CheatBtn.setIcon(new ImageIcon("res/cheat_normal.png"));
            }
            if (controller.cheatMode && !controller.hintMode && controller.changeMode)//如果正常模式下
                CheatBtn.setIcon(new ImageIcon("res/cheat_press.png"));
        });


        HintBtn.setSize(120, 50);
        HintBtn.setBorderPainted(false);

        HintBtn.setLocation(20, restartBtn.getY());
        add(HintBtn);

        HintBtn.addActionListener(e -> {
            if (!controller.hintMode && !controller.changeMode && !controller.cheatMode && !controller.AIMode) {//当hint和cheat都没有开启时才可以
                controller.changeMode = true;
                controller.hintMode = true;//如果普通状态下按下HINT，就改变模式
                chessBoardPanel.HintClickGrid(GameFrame.controller.getCurrentPlayer(), controller.hintMode, false);
                HintBtn.setIcon(new ImageIcon("res/hint_press.png"));

            } else if (controller.hintMode) {
                controller.changeMode = false;//如果hint模式下
                controller.hintMode = false;
                chessBoardPanel.HintClickGrid(GameFrame.controller.getCurrentPlayer(), controller.hintMode, false);
                HintBtn.setIcon(new ImageIcon("res/hint_normal.png"));
            }


        });

        JButton AIBtn = new JButton(new ImageIcon("res/ai_normal.png"));//
        AIBtn.setSize(120, 50);
        AIBtn.setLocation(CheatBtn.getX() + restartBtn.getWidth() + 20, restartBtn.getY());
        add(AIBtn);
        AIBtn.addActionListener(e -> {
            if (!controller.hintMode && !controller.changeMode && !controller.cheatMode && !controller.AIMode) {
                controller.changeMode = true;
                controller.AIMode = true;
                isIA = true;
                AIeasyBtn.setVisible(isIA);
                AIdiffiBtn.setVisible(isIA);
                AIBtn.setIcon(new ImageIcon("res/ai_press.png"));
            } else if (controller.AIMode) {
                controller.changeMode = false;
                controller.AIMode = false;
                isIA = false;
                AIeasyBtn.setVisible(isIA);
                AIdiffiBtn.setVisible(isIA);
                AIBtn.setIcon(new ImageIcon("res/ai_normal.png"));
            }
        });

        AIeasyBtn.setSize(120, 50);
        AIeasyBtn.setLocation(AIBtn.getX(), AIBtn.getY() - 70);
        add(AIeasyBtn);
        AIeasyBtn.setVisible(false);
        AIeasyBtn.setBorderPainted(false);
        AIeasyBtn.addActionListener(e -> {
            if (easy && hard) {
                AIeasyBtn.setIcon(new ImageIcon("res/easy_press.png"));
                chessBoardPanel.AI1(controller.AIMode, controller.getCurrentPlayer(), false);
                int bl = 0, wh = 0;
                chessBoardPanel.setChessintarr();
                for (int d = 0; d < 8; d++) {
                    for (int c = 0; c < 8; c++) {
                        if (chessBoardPanel.chessIntarr[d][c] == 1)
                            wh++;
                        else if (chessBoardPanel.chessIntarr[d][c] == -1) {
                            bl++;
                        }
                    }
                }
                statusPanel.setScoreText(bl, wh);
                repaint();
                easy = false;
            } else if (!easy) {
                AIeasyBtn.setIcon(new ImageIcon("res/easy_normal.png"));
                easy = true;
            }
        });

        AIdiffiBtn.setSize(120, 50);
        AIdiffiBtn.setLocation(AIBtn.getX(), AIeasyBtn.getY() - 70);
        add(AIdiffiBtn);
        AIdiffiBtn.setVisible(false);
        AIdiffiBtn.setBorderPainted(false);
        AIdiffiBtn.addActionListener(e -> {
            if (easy && hard) {
                AIdiffiBtn.setIcon(new ImageIcon("res/hard_press.png"));
                chessBoardPanel.AI2(controller.AIMode, controller.getCurrentPlayer(), false);
                int bl = 0, wh = 0;
                chessBoardPanel.setChessintarr();
                for (int d = 0; d < 8; d++) {
                    for (int c = 0; c < 8; c++) {
                        if (chessBoardPanel.chessIntarr[d][c] == 1)
                            wh++;
                        else if (chessBoardPanel.chessIntarr[d][c] == -1) {
                            bl++;
                        }
                    }
                }
                statusPanel.setScoreText(bl, wh);
                repaint();
                hard = false;
            } else if (!hard) {
                AIdiffiBtn.setIcon(new ImageIcon("res/hard_normal.png"));
                hard = true;
            }
        });

        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        new Timer(delay, taskPerformer).start();
    }

}
