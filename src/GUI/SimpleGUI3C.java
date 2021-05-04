package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleGUI3C implements ActionListener {
    JFrame frame;

    public static void main(String[] args) {
        SimpleGUI3C gui = new SimpleGUI3C();
        gui.go();
    }

    public void go() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton button = new JButton("Change colors");
        button.addActionListener(this);

        MyDrawPanel drawPanel = new MyDrawPanel();

        frame.getContentPane().add(BorderLayout.SOUTH, button);
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.setSize(1000,1000);
        frame.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        frame.repaint();
    }
}
