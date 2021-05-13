package Trial;

public class ANastySchedulerCode implements Runnable {

    @Override
    public void run() {
        go();
    }

    public void go() {
        printIt();
    }
    public void printIt() {
        System.out.println("In the new thread.");
    }
}

class AnotherClass {
    public static void main(String[] args) {
        Runnable threadJob = new ANastySchedulerCode();
        Thread myThread = new Thread(threadJob);

        myThread.start();
        System.out.println("In the main method"); // Not certain which will be printed first.
    }
}