package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextArea implements ActionListener {
    JTextArea text;
    public static void main(String[] args) {
        TextArea a = new TextArea();
        a.go();
    }

    public void go() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JButton button = new JButton("Click me");
        button.addActionListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        text = new JTextArea(20,30);
        text.setLineWrap(true);
        panel.setBackground(Color.BLACK);
        frame.setContentPane(panel);

        JScrollPane scroller = new JScrollPane(text);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scroller);

        frame.getContentPane().add(BorderLayout.CENTER, scroller);
        frame.getContentPane().add(BorderLayout.SOUTH, button);

        frame.setSize(500,500);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        text.append("Button Clicked");
    }
}
