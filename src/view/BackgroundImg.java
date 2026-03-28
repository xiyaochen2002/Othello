package view;

import java.awt.*;
import javax.swing.*;
import playersystem.Sound;
public class BackgroundImg extends JFrame  {
    JPanel jp_window;;//添加所有组件的面板
    private static final ImageIcon bgimg = new ImageIcon("res/Init.png");//背景图片
    public BackgroundImg() {
        jp_window = (JPanel) this.getContentPane();
        jp_window.setOpaque(false);//设置面板的透明度
        JLabel lable_img = new JLabel(bgimg);//把背景图片显示在一个标签里面
        lable_img.setBounds(-1, -1, bgimg.getIconWidth(), bgimg.getIconHeight());
        jp_window.setLayout(new FlowLayout(10,120,50));
        JButton StartGameBtn = new JButton("Start");
        StartGameBtn.setPreferredSize(new Dimension(90, 100));
        StartGameBtn.setBorderPainted(false);
        StartGameBtn.setIcon(new ImageIcon("res/start2.png"));
        add(StartGameBtn);
        StartGameBtn.setLocation(100, 500);//开始游戏
        StartGameBtn.addActionListener(e -> {
            GameFrame mainFrame = new GameFrame(1000);
            mainFrame.setVisible(true);
            this.setVisible(false);
        });
        JButton backGameBtn = new JButton("Exit");//退出游戏
        backGameBtn.setPreferredSize(new Dimension(90, 100));
        backGameBtn.setLocation(300 ,  500);
        backGameBtn.setBorderPainted(false);
        backGameBtn.setIcon(new ImageIcon("res/Exit.png"));
        add(backGameBtn);
        backGameBtn.addActionListener(e -> {
            System.exit(0);

        });
        JButton Description = new JButton("State");//说明按钮
        Description.setPreferredSize(new Dimension(90, 100));
        Description.setLocation(500,  500);
        Description.setBorderPainted(false);
        Description.setIcon(new ImageIcon("res/d.png"));
        add(Description);
        Description.addActionListener(e -> {
                    String msg ="\n\nAny disks of the opponent player's color  \n" +
                            "that are in between the disk just placed \n"+
                            "and another disk of the current player's color\n"+
                            "are turned over to the current player's color.\n\n";
                    JOptionPane.showMessageDialog(null, msg,"Description",JOptionPane.PLAIN_MESSAGE);
        });
        //用于实现窗口背景图片
        this.getLayeredPane().setLayout(null);
        //把背景图片添加到分层窗格的最底层作为背景
        this.getLayeredPane().add(lable_img, new Integer(Integer.MIN_VALUE));
        this.setSize(bgimg.getIconWidth(), bgimg.getIconHeight() + 40);//得到图片的宽和高
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        String[] options = {"Yes", "No"};
        int x = JOptionPane.showOptionDialog(null, "Sound?",
                "Sound",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        Sound sound= new Sound();
        if(x==0)
        {
            if(!sound.isplay())
                sound.loadSound("musics/qin.mid");//是否需要音乐
        }
        if(x==1)
        { if(sound.isplay())
            sound.mystop();
        }
    }

}