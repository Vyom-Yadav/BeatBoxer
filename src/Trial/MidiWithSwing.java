package Trial;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;

public class MidiWithSwing implements ControllerEventListener {
    JFrame frame;

    public static void main(String[] args) {
        MidiWithSwing mini = new MidiWithSwing();
        mini.go();
    }

    public void go() {
        try {
            frame = new JFrame();
            MyDrawPanel a = new MyDrawPanel();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(a);
            frame.setBounds(30,30,500,500);
            frame.setVisible(true);
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();

            int[] eventsIWant = {127};
            sequencer.addControllerEventListener(a, eventsIWant);

            Sequence seq = new Sequence(Sequence.PPQ, 4);
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

        @Override
        public void controlChange(ShortMessage shortMessage) {
            msg = true;
            repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            if (msg) {
                Graphics2D g2d = (Graphics2D) g;

                int r, b, gr;
                r = (int) (Math.random() * 250);
                b = (int) (Math.random() * 250);
                gr = (int) (Math.random() * 250);

                g2d.setColor(new Color(r,gr,b));

                int ht = (int) ((Math.random()*220)+10);
                int width = (int) ((Math.random()*220)+10);
                int x = (int) ((Math.random()*40)+10);
                int y = (int) ((Math.random()*40)+10);
                g2d.fillRect(x,y,ht,width);
                msg = false;
            }
        }
    }
}
