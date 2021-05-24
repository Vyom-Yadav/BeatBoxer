package BeatBox1;

import javax.sound.midi.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class BeatBoxTrial1 {
    JPanel mainPanel;
    ArrayList<JCheckBox> checkBoxList;
    Sequencer sequencer;
    Sequence sequence;
    Sequence mySequence = null;
    Track track;
    JFrame theFrame;
    JList<String> incomingList;
    JTextField userMessage;
    int nextNum;
    Font newFont = new Font(Font.MONOSPACED, Font.BOLD, 15);
    Dimension dim = new Dimension(150, 100);
    boolean[] checkboxState;
    String userName;
    Vector<String> listVector = new Vector<>();
    ObjectInputStream in;
    ObjectOutputStream out;
    HashMap<String, boolean[]> otherSeqsMap = new HashMap<>();
    String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat", "Acoustics Snare", "Crash Cymbal",
            "Hand Clap", "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga", "Cowbell", "Vibraslap",
            "Low-mis Tom", "High Agogo", "Open High Conga"};

    int[] instruments = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63};
    int[] noOfBeats = new int[16];
    SoundPattern pattern;
    boolean clearPattern;

    public static void main(String[] args) {
        new BeatBoxTrial1().startUp(args[0]);
    }

    public static MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a, tick);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return event;
    }

    public void startUp(String name) {
        userName = name;

        try {
            Socket sock = new Socket("192.168.43.63", 4241);
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());
            Thread remote = new Thread(new RemoteReader());
            remote.setName("Additional Thread");
            remote.start();
        } catch (Exception ex) {
            System.out.println("Couldn't connect, play on your own");
            ex.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName());
        setUpMidi();
        BuildGUI();
    }

    class RemoteReader implements Runnable {
        boolean[] checkboxState = null;
        String nameToShow = null;
        Object obj = null;

        @Override
        public void run() {
            try {
                while ((obj = in.readObject()) != null) {
                    System.out.println("Got an object from the server");
                    System.out.println(obj.getClass());
                    nameToShow = (String) obj;
                    checkboxState = (boolean[]) in.readObject();
                    otherSeqsMap.put(nameToShow, checkboxState);
                    listVector.add(nameToShow);
                    incomingList.setListData(listVector);
                    System.out.println(Thread.currentThread().getName());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void BuildGUI() {
        theFrame = new JFrame("Cyber Beat Box");
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        pattern = new SoundPattern();
        background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        checkBoxList = new ArrayList<>();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        JButton start = new JButton("Start");
        start.addActionListener(new MyStartListener());
        start.setPreferredSize(dim);
        start.getPreferredSize();
        buttonBox.add(start);
        start.setFont(newFont);

        JButton stop = new JButton("Stop");
        stop.addActionListener(new MyStopListener());
        stop.setPreferredSize(dim);
        stop.getPreferredSize();
        buttonBox.add(stop);
        stop.setFont(newFont);

        JButton upTempo = new JButton("Tempo Up");
        upTempo.addActionListener(new MyUpTempoListener());
        upTempo.setPreferredSize(dim);
        upTempo.getPreferredSize();
        buttonBox.add(upTempo);
        upTempo.setFont(newFont);

        JButton downTempo = new JButton("Tempo Down");
        downTempo.addActionListener(new MyDownTempoListener());
        downTempo.setPreferredSize(dim);
        downTempo.getPreferredSize();
        buttonBox.add(downTempo);
        downTempo.setFont(newFont);

        JButton selectAll = new JButton("Select All");
        selectAll.addActionListener(new MySelectAllListener());
        selectAll.setPreferredSize(dim);
        selectAll.getPreferredSize();
        buttonBox.add(selectAll);
        selectAll.setFont(newFont);

        JButton deSelectAll = new JButton("Unselect All");
        deSelectAll.addActionListener(new MyDeSelectAllListener());
        deSelectAll.setPreferredSize(dim);
        deSelectAll.getPreferredSize();
        buttonBox.add(deSelectAll);
        deSelectAll.setFont(newFont);

        JButton open = new JButton("Open/de-serialize");
        open.addActionListener(new openListener());
        open.setPreferredSize(dim);
        open.getPreferredSize();
        buttonBox.add(open);
        open.setFont(newFont);

        JButton save = new JButton("Save/serialize");
        save.addActionListener(new saveListener());
        save.setPreferredSize(dim);
        save.getPreferredSize();
        buttonBox.add(save);
        save.setFont(newFont);

        JButton sendIt = new JButton("Send It");
        sendIt.addActionListener(new MySendItListener());
        sendIt.setPreferredSize(dim);
        sendIt.getPreferredSize();
        buttonBox.add(sendIt);
        sendIt.setFont(newFont);

        userMessage = new JTextField();
        buttonBox.add(userMessage);

        incomingList = new JList<>();
        incomingList.addListSelectionListener(new incomingListSelectionListener());
        incomingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane theList = new JScrollPane(incomingList);
        buttonBox.add(theList);
        incomingList.setListData(listVector); // no data to start with

        Box nameBox = new Box(BoxLayout.Y_AXIS);
        for (int i = 0; i < 16; i++) {
            Label a = new Label(instrumentNames[i]);
            a.setFont(newFont);
            nameBox.add(a);
        }
        background.add(BorderLayout.EAST, buttonBox);
        background.add(BorderLayout.WEST, nameBox);

        theFrame.getContentPane().add(background);

        GridLayout grid = new GridLayout(16, 16);
        grid.setVgap(1);
        grid.setHgap(2);
        mainPanel = new JPanel(grid);
        pattern.setPreferredSize(new Dimension(1000, 160));
        pattern.getPreferredSize();
        background.add(BorderLayout.CENTER, mainPanel);
        background.add(BorderLayout.SOUTH, pattern);

        for (int i = 0; i < 256; i++) {
            JCheckBox c = new JCheckBox();
            c.setSelected(false);
            checkBoxList.add(c);
            mainPanel.add(c);
        }

        theFrame.setPreferredSize(new Dimension(1000, 1000));
        theFrame.getPreferredSize();
        theFrame.pack();
        theFrame.setVisible(true);

    }

    public void setUpMidi() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(120);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildTrackAndStart() {
        ArrayList<Integer> trackList;

        sequence.deleteTrack(track);
        track = sequence.createTrack();

        for (int i = 0; i < 16; i++) {
            trackList = new ArrayList<>();

            int key = instruments[i];

            for (int j = 0; j < 16; j++) {
                JCheckBox jc = checkBoxList.get(j + (16 * i));
                if (jc.isSelected()) {
                    trackList.add(key);
                    noOfBeats[i]++;
                } else {
                    trackList.add(null);
                }

                makeTracks(trackList);
                track.add(makeEvent(176, 1, 127, 0, 16)); // Change Instrument
            }

            track.add(makeEvent(192, 9, 1, 0, 15)); // If 15th beat is not there in some case.
            try {
                sequencer.setSequence(sequence);
                sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
                sequencer.start();
                sequencer.setTempoInBPM(120);
                pattern.repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void makeTracks(ArrayList<Integer> list) {
        Iterator<Integer> it = list.iterator();
        for (int i = 0; i < 16; i++) {
            if (it.hasNext()) { // As we add null also.
                Integer num = it.next();
                if (num != null) {
                    int numKey = num;
                    track.add(makeEvent(144, 9, numKey, 100, i));
                    track.add(makeEvent(128, 9, numKey, 100, i + 1));
                }
            }
        }
    }

    private void saveFile(File file) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(checkboxState);
            outputStream.close();

        } catch (IOException ex) {
            System.out.println("Couldn't save file");
            ex.printStackTrace();
        }
    }

    private void openFile(File file) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
            checkboxState = (boolean[]) inputStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Couldn't open file");
            ex.printStackTrace();
        }
    }

    public void changeSequence(boolean[] checkboxState) {
        for (int i = 0; i < 256; i++) {
            JCheckBox check = checkBoxList.get(i);
            check.setSelected(checkboxState[i]);
        }
    }

    class MyStartListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            buildTrackAndStart();
            clearPattern = false;
        }
    }

    class MyStopListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            sequencer.stop();
            for (int i = 0; i < 16; i++) {
                noOfBeats[i] = 0;
            }
            clearPattern = true;
            pattern.repaint();
        }
    }

    class MyUpTempoListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float) (tempoFactor * 1.03));
        }
    }

    class MyDownTempoListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor(tempoFactor * 97);
        }
    }

    class MySelectAllListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            for (int i = 0; i < 256; i++) {
                checkBoxList.get(i).setSelected(true);
            }
        }
    }

    class MyDeSelectAllListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            for (int i = 0; i < 256; i++) {
                checkBoxList.get(i).setSelected(false);
            }
        }
    }

    class saveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            checkboxState = new boolean[256];
            for (int i = 0; i < 256; i++) {
                checkboxState[i] = checkBoxList.get(i).isSelected();
            }
            JFileChooser fileSave = new JFileChooser();
            fileSave.showSaveDialog(theFrame);
            saveFile(fileSave.getSelectedFile());
        }
    }

    class openListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(theFrame);
            openFile(fileOpen.getSelectedFile());
            for (int i = 0; i < 256; i++) {
                checkBoxList.get(i).setSelected(checkboxState[i]);
            }
            sequencer.stop();
            buildTrackAndStart();
        }
    }

    class incomingListSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            if (!listSelectionEvent.getValueIsAdjusting()) {
                String selected = incomingList.getSelectedValue();
                if (selected != null) {
                    // now go to map and change the sequence.
                    boolean[] selectedState = otherSeqsMap.get(selected);
                    changeSequence(selectedState);
                    sequencer.stop();
                    clearPattern = false;
                    buildTrackAndStart();
                }
            }
        }
    }

    class MySendItListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean[] checkboxState = new boolean[256];
            for (int i = 0; i < 256; i++) {
                JCheckBox check = checkBoxList.get(i);
                if (check.isSelected()) {
                    checkboxState[i] = true;
                }
            }
            String messageToSend;
            try {
                messageToSend = userName + nextNum++ + ": " + userMessage.getText();
                out.writeObject(messageToSend);
                out.writeObject(checkboxState);
            } catch (Exception ex) {
                System.out.println("Sorry dude. Could not send it to server");
            }
            userMessage.setText("");
        }
    }

    class MyPlayMineListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (mySequence != null) {
                sequence = mySequence; // restore to my original.
            }
        }
    }

    class SoundPattern extends JPanel {

        @Override
        public void paintComponent(Graphics g) {
            if (clearPattern) {
                g.clearRect(0, 0, this.getWidth(), this.getHeight());
            } else {

                int width = theFrame.getWidth() / 16;
                int red, blue, green;

                for (int i = 0; i < 16; i++) {
                    red = (int) (Math.random() * 255);
                    blue = (int) (Math.random() * 255);
                    green = (int) (Math.random() * 255);

                    Color randomColor = new Color(red, blue, green);
                    g.setColor(randomColor);
                    g.fillRect(i * width, (16 - noOfBeats[i]) * 10, width, noOfBeats[i] * 10);
                }
            }
        }
    }

}
