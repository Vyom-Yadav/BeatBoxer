package GUI;
import javax.swing.*;
import java.awt.event.*;

public class SimpleGUI1B implements ActionListener {
    JButton button1;

    public static void main(String[] args) {
        SimpleGUI1B gui = new SimpleGUI1B();
        gui.go();
    }

    public void go() {
        JFrame frame = new JFrame();

        button1 = new JButton("click me");

        button1.addActionListener(this); // Register your interest with the button

        frame.getContentPane().add(button1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent Event) {
        button1.setText("I have been clicked");
    }
}
