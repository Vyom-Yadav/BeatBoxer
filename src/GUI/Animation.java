package GUI;

import javax.swing.*;
import java.awt.*;

public class Animation {
    JFrame frame;
    int move = 0;

    public static void main(String[] args) {
        Animation animation = new Animation();
        animation.go();
    }

    public void go() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DrawingPanel panel = new DrawingPanel();

        frame.getContentPane().add(panel);
        frame.setSize(1000,1000);
        frame.setVisible(true);
        while(move++<=1000) {
            panel.repaint();
            try {
                Thread.sleep(5);
            } catch (Exception ex) { }
        }
    }

    class DrawingPanel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillRect(0,0,this.getWidth(), this.getHeight());

            g.setColor(Color.BLUE);
            g.fillOval(move,move,100,100);
        }
    }
}
