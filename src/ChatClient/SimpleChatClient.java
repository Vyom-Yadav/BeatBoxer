package ChatClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleChatClient {
    JTextArea incoming;
    JTextField outgoing;
    BufferedReader reader;
    PrintWriter writer;
    Socket socket;

    public static void main(String[] args) {
        SimpleChatClient obj = new SimpleChatClient();
        obj.go();
    }

    public void go() {
        JFrame frame = new JFrame("Ludicrously simple chat client");
        JPanel panel = new JPanel();
        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane scroller = new JScrollPane(incoming);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new sendButtonListener());
        panel.add(scroller);
        panel.add(outgoing);
        panel.add(sendButton);
        frame.getContentPane().add(panel);
        setUpNetwork();

        Thread readerThread = new Thread(new incomingReader());
        readerThread.start();

        frame.setSize(500, 500);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setUpNetwork() {
        try {
            socket = new Socket("192.168.1.9", 5000);
            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(streamReader);
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


    class incomingReader implements Runnable {

        @Override
        public void run() {
            String message;
            try {
                while (((message = reader.readLine()) != null)) {
                    System.out.println("read " + message);
                    incoming.append(message + '\n');
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
