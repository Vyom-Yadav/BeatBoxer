package Trial;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;

public class ShrinkingRectangleWithMusic implements ControllerEventListener {
    JFrame frame;

    public static void main(String[] args) {
        ShrinkingRectangleWithMusic a = new ShrinkingRectangleWithMusic();
        a.go();
    }

    public void go() {
        try {
            frame = new JFrame();
            MyDrawPanel obj = new MyDrawPanel();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(obj);
            frame.setBounds(30, 30, 500, 500);
            frame.setVisible(true);
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            Sequence seq = new Sequence(Sequence.PPQ, 4);
            int[] eventsIWant = {127};
            sequencer.addControllerEventListener(obj, eventsIWant);
            Track track = seq.createTrack();

            ShortMessage changeInstrument = new ShortMessage();
            changeInstrument.setMessage(192, 1, 114, 0);
            MidiEvent event = new MidiEvent(changeInstrument, 0);
            track.add(event);
            for (int i = 5; i < 90; i++) {
                track.add(MiniMusicPlayer1.makeEvent(144, 1, i, 100, i));
                track.add(MiniMusicPlayer1.makeEvent(176, 1, 127, 0, i));
                track.add(MiniMusicPlayer1.makeEvent(128, 1, i, 100, i + 1));
            }
            sequencer.setSequence(seq);
            sequencer.setTempoInBPM(220);
            sequencer.start();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void controlChange(ShortMessage shortMessage) {

    }

    static class MyDrawPanel extends JPanel implements ControllerEventListener {
        boolean msg;
        int size = 100;

        @Override
        public void paintComponent(Graphics g) {
            if(msg && size>=0) {
                size--;
                Graphics2D g2d = (Graphics2D) g;
                g.setColor(Color.WHITE);
                g.fillRect(0,0,this.getWidth(), this.getHeight());
                g2d.setColor(Color.CYAN);
                g2d.fillRect(250, 250, size, size);
                msg = false;
            }

        }

        @Override
        public void controlChange(ShortMessage shortMessage) {
            msg = true;
            repaint();
        }
    }
}
