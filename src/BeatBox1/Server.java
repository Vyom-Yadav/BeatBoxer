package BeatBox1;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {

    ArrayList<ObjectOutputStream> clientOutputStreams;

    public static void main(String[] args) {
        new Server().go();
    }

    class clientHandler implements Runnable {

        ObjectInputStream in;
        Socket clientSocket;

        public clientHandler(Socket socket) {
            try {
                clientSocket = socket;
                in = new ObjectInputStream(clientSocket.getInputStream());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            Object o2;
            Object o1;

            try {
                while((o1 = in.readObject()) != null) {
                    o2 = in.readObject();
                    System.out.println("Read two objects");
                    tellEveryone(o1, o2);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    public void go() {
        clientOutputStreams = new ArrayList<>();

        try {
            ServerSocket serverSock = new ServerSocket(4241);

            while (true) {
                Socket clientSocket = serverSock.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                clientOutputStreams.add(out);

                Thread readingThread = new Thread(new clientHandler(clientSocket));
                readingThread.start();

                System.out.println("Got a connection");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void tellEveryone(Object one, Object two) {
        Iterator<ObjectOutputStream> it = clientOutputStreams.iterator();
        while (it.hasNext()) {
            try {
                ObjectOutputStream out = it.next();
                out.writeObject(one);
                out.writeObject(two);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
