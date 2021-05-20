package StackOverflow;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ObjectInputStream in_s;
    ObjectOutputStream out_s;
    String message_s;

    public void go() {
        try {
            while (true) {
                ServerSocket ser_Socket = new ServerSocket(4558);
                Socket clientSocket = ser_Socket.accept();
                out_s = new ObjectOutputStream(clientSocket.getOutputStream());

                Thread additionalThread = new Thread(new clientHandler(clientSocket));
                additionalThread.start();
                System.out.println("Got a connection");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class clientHandler implements Runnable {
        Socket clientSocket;

        public clientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                in_s = new ObjectInputStream(clientSocket.getInputStream());
                message_s = (String) in_s.readObject();
                out_s.writeObject(message_s);
                System.out.println(message_s);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Server().go();
    }

}
