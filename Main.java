

import javax.swing.*;

class Main extends JFrame {
    public static void main(String[] args) {
        Game frame = new Game();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}