package GUI;

import javax.swing.*;
import java.awt.*;

public class LayoutManagers_diff {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JButton button1 = new JButton("Hello");
        JButton button2 = new JButton("Hi");
        JButton button3 = new JButton("Wow");
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        panel.setBackground(Color.BLUE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Try without this too.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(BorderLayout.EAST,panel);
        frame.setBounds(0,0,500,500);
        frame.setVisible(true);
    }
}
