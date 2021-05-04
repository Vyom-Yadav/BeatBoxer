package GUI;
import javax.swing.*;

public class Adding_Button_To_JFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JButton button = new JButton("click me");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // This line will make the code finish when clicked on cross button.
        frame.getContentPane().add(button);
        frame.setSize(1000,1000);
        frame.setVisible(true);
    }
}
