import view.BackgroundImg;

import javax.swing.*;



public class Main {
    public static void main(String[] args) {
        //   GameFrame mainFrame = new GameFrame(800);
        // mainFrame.setVisible(true);
        SwingUtilities.invokeLater(BackgroundImg::new);
    }
}

