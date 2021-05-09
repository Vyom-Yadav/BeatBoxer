package GUI;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class JLIST implements ListSelectionListener {
    JList<String> list;

    public static void main(String[] args) {
        JLIST a = new JLIST();
        a.go();
    }

    public void go() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        String[] list_Items = {"alpha", "beta", "gamma", "delta", "epsilon", "zeta", "eta", "theta"};
        list = new JList<>(list_Items);

        JScrollPane scroller = new JScrollPane(list);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scroller);
        list.setVisibleRowCount(4);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setVisible(true);
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
            if(!listSelectionEvent.getValueIsAdjusting()) { // Checks whether user is dragging or not.
                String selection = (String) list.getSelectedValue();
                System.out.println(selection);
            }


    }
}
