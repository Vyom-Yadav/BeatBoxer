package StackOverflow;

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
            message = "new message";
            out.writeObject(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class RemoteReader implements Runnable {

        @Override
        public void run() {
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
}
