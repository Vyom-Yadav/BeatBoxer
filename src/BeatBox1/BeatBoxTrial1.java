package BeatBox1;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BeatBoxTrial1 {
    JPanel mainPanel;
    ArrayList<JCheckBox> checkBoxList;
    Sequencer sequencer;
    Sequence sequence;
    Track track;
    JFrame theFrame;
    Font newFont = new Font(Font.MONOSPACED, Font.BOLD, 15);
    Dimension dim = new Dimension(150, 100);

    String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat", "Acoustics Snare", "Crash Cymbal",
            "Hand Clap", "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga", "Cowbell", "Vibraslap",
            "Low-mis Tom", "High Agogo", "Open High Conga"};

    int[] instruments = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63};
    int[] noOfBeats = new int[16];
    SoundPattern pattern;
    boolean clearPattern;

    public static void main(String[] args) {
        new BeatBoxTrial1().BuildGUI();
    }

    public void BuildGUI() {
        theFrame = new JFrame("Cyber Beat Box");
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        pattern = new SoundPattern();
        background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        checkBoxList = new ArrayList<JCheckBox>();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        JButton start = new JButton("Start");
        start.addActionListener(new MyStartListener());
        start.addActionListener(pattern);
        start.setPreferredSize(dim);
        buttonBox.add(start);
        start.setFont(newFont);

        JButton stop = new JButton("Stop");
        stop.addActionListener(new MyStopListener());
        stop.setPreferredSize(dim);
        buttonBox.add(stop);
        stop.setFont(newFont);

        JButton upTempo = new JButton("Tempo Up");
        upTempo.addActionListener(new MyUpTempoListener());
        upTempo.setPreferredSize(dim);
        buttonBox.add(upTempo);
        upTempo.setFont(newFont);

        JButton downTempo = new JButton("Tempo Down");
        downTempo.addActionListener(new MyDownTempoListener());
        downTempo.setPreferredSize(dim);
        buttonBox.add(downTempo);
        downTempo.setFont(newFont);

        JButton selectAll = new JButton("Select All");
        selectAll.addActionListener(new MySelectAllListener());
        selectAll.setPreferredSize(dim);
        buttonBox.add(selectAll);
        selectAll.setFont(newFont);

        JButton deSelectAll = new JButton("Unselect All");
        deSelectAll.addActionListener(new MyDeSelectAllListener());
        deSelectAll.setPreferredSize(dim);
        buttonBox.add(deSelectAll);
        deSelectAll.setFont(newFont);

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
        pattern.setPreferredSize(new Dimension(1000,160));
        pattern.getPreferredSize();
        background.add(BorderLayout.CENTER, mainPanel);
        background.add(BorderLayout.SOUTH, pattern);

        for (int i = 0; i < 256; i++) {
            JCheckBox c = new JCheckBox();
            c.setSelected(false);
            checkBoxList.add(c);
            mainPanel.add(c);
        }

        setUpMidi();

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
        int[] trackList = null;

        sequence.deleteTrack(track);
        track = sequence.createTrack();

        for (int i = 0; i < 16; i++) {
            trackList = new int[16];

            int key = instruments[i];

            for (int j = 0; j < 16; j++) {
                JCheckBox jc = (JCheckBox) checkBoxList.get(j + (16 * i));
                if (jc.isSelected()) {
                    trackList[j] = key;
                    noOfBeats[i]++;
                } else {
                    trackList[j] = 0;
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void makeTracks(int[] list) {
        for (int i = 0; i < 16; i++) {
            int key = list[i];

            if (key != 0) {
                track.add(makeEvent(144, 9, key, 100, i));
                track.add(makeEvent(128, 9, key, 100, i + 1));
            }
        }
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

    class MyStartListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            buildTrackAndStart();
            clearPattern = false;
        }
    }

    class MyStopListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            sequencer.stop();
            for (int i=0; i<16; i++) {
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
            sequencer.setTempoFactor((float) (tempoFactor * 97));
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

    class SoundPattern extends JPanel implements ActionListener {

        @Override
        public void paintComponent(Graphics g) {
            if (clearPattern) {
                g.clearRect(0,0, this.getWidth(), this.getHeight());
            } else {

                int width = theFrame.getWidth() / 16;
                int red, blue, green;

                for (int i = 0; i < 16; i++) {
                    red = (int) (Math.random() * 255);
                    blue = (int) (Math.random() * 255);
                    green = (int) (Math.random() * 255);

                    Color randomColor = new Color(red, blue, green);
                    g.setColor(randomColor);
                    g.fillRect(i * width, (16-noOfBeats[i])*10, width, noOfBeats[i] * 10);
                }
            }
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    }

}
