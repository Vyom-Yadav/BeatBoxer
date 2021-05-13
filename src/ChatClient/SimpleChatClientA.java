package ChatClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleChatClientA {
    JTextField outgoing;
    PrintWriter writer;
    Socket socket;

    public static void main(String[] args) {
        SimpleChatClientA obj = new SimpleChatClientA();
        obj.go();
    }

    public void go() {
        JFrame frame = new JFrame("Ludicrously simple chat client");
        JPanel panel = new JPanel();
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new sendButtonListener());
        panel.add(outgoing);
        panel.add(sendButton);
        frame.getContentPane().add(panel);
        setUpNetwork();
        frame.setSize(500,500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setUpNetwork() {
        try {
            socket = new Socket("127.0.0.1", 5000);
            writer = new PrintWriter(socket.getOutputStream());
            System.out.println("Network Established");
        } catch (IOException ex) {
            System.out.println("Unable to establish connection");
            ex.printStackTrace();
        }
    }

    class sendButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                writer.println(outgoing.getText());
                writer.flush();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

}
