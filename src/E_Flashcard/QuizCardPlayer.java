package E_Flashcard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class QuizCardPlayer {
    private JTextArea display;
    private ArrayList<QuizCard> cardList;
    private QuizCard currentCard;
    private int currentCardIndex;
    private JFrame frame;
    private JButton nextButton;
    private boolean isShowAnswer;

    public static void main(String[] args) {
        QuizCardPlayer reader = new QuizCardPlayer();
        reader.go();
    }

    public void go() {
        frame = new JFrame();
        JPanel mainPanel = new JPanel();
        Font bigFont = new Font(Font.MONOSPACED, Font.BOLD, 24);

        display = new JTextArea(10,20);
        display.setFont(bigFont);
        display.setLineWrap(true);
        display.setEditable(false);

        JScrollPane qScroller = new JScrollPane(display);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        nextButton = new JButton("Show Question");
        mainPanel.add(qScroller);
        mainPanel.add(nextButton);
        nextButton.addActionListener(new nextCardListener());

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Load card set");
        loadMenuItem.addActionListener(new openMenuListener());
        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(640,500);
        frame.setVisible(true);
    }

    class nextCardListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (isShowAnswer) {
                // show the answer because they have seen the question
                display.setText(currentCard.getAnswer());
                nextButton.setText("Next Card");
                isShowAnswer = false;
            } else {
                //show the next question
                if (currentCardIndex < cardList.size()) {
                    showNextCard();
                } else {
                    // there are no more cards
                    display.setText("That was the last card");
                    nextButton.setEnabled(false);
                }
            }

        }
    }

    class openMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showSaveDialog(frame);
            loadFile(fileOpen.getSelectedFile());

        }
    }

    private void loadFile (File file) {
        cardList = new ArrayList<QuizCard>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line=reader.readLine())!= null) {
                makeCard(line);
            }
            reader.close();
        } catch (IOException ex) {
            System.out.println("Couldn't read the card file");
            ex.printStackTrace();
        }
        showNextCard();
    }

    private void makeCard(String lineToParse) {
        String[] result = lineToParse.split("/");
        QuizCard card = new QuizCard(result[0], result[1]);
        cardList.add(card);
        System.out.println("made a card");
    }

    private void showNextCard() {
        currentCard = cardList.get(currentCardIndex);
        currentCardIndex++;
        display.setText(currentCard.getQuestion());
        nextButton.setText("Show Answer");
        isShowAnswer = true;
    }


}
