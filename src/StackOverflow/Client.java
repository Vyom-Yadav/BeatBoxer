package StackOverflow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    ObjectInputStream in;
    ObjectOutputStream out;
    String message;

    public void go() {
        try {
            Socket clientSocket = new Socket("192.168.1.9", 4558);
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            Thread reader = new Thread(new RemoteReader());
            reader.setName("Reader");
            reader.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        buildGUI();

    }

    public void buildGUI() {
        JFrame frame = new JFrame();
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new sendItListener());

        frame.getContentPane().add(sendButton);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(500, 500);
    }

    class RemoteReader implements Runnable {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
            Object obj;
            try {
                while ((obj = in.readObject()) != null) {
                    message = (String) obj;
                    System.out.println(message + " from server");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) {
        new Client().go();
    }

    class sendItListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                message = "new message";
                out.writeObject(message);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
