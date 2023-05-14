import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame {
    int numOfButtons, score = 0, clickedPhotos, current, oddClicks, numClicks = 0;
    String photoArray[] = { "img/aizawa.png", "img/bakugo.jpg", "img/denkiKiri.jpg", "img/midoriya.jpg", "img/todoroki.jpg",
            "img/uraraka.jpg" };
    JButton[] cardButtons;
    ImageIcon cardBack = new ImageIcon("img/cardBack.jpg");
    ImageIcon[] pictures;
    ImageIcon temp;
    Timer timer;
    JFrame gameFrame;
    JLabel matches, matchNum, movesLabel, movesNum;
    JPanel cardPanel;

    public Main() {
        //make jframe
        gameFrame = new JFrame("Matching Game: My Hero Academia");

        //dialog pop up at beginning of game
        JOptionPane.showMessageDialog(gameFrame,
                "Welcome to My Hero Matching Game! \nHere is how to play: \nYour objective is to find the two matching \ncharacters in the cards displayed with the least \namount of moves possible. Good Luck!");

        //set jframe items
        gameFrame.setBackground(Color.BLACK);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setBounds(10, 10, 550, 600);
        gameFrame.setLayout(new GridLayout(1, 6));
        gameFrame.setVisible(true);

        //make game panel with layout  
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout(0, 0));

        //create card panell and set items
        cardPanel = new JPanel();
        cardPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        cardPanel.setLayout(new GridLayout(0, 4, 0, 0));
        cardPanel.setBackground(Color.BLACK);

        //card method to make cards, timer, and allow cards to be randomized
        addCard();
        gamePanel.add(cardPanel, BorderLayout.CENTER);

        //create score panel for the bottom of the jframe and set items
        JPanel scorePanel = new JPanel();
        scorePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        scorePanel.setLayout(new GridLayout(3, 2, 0, 0));
        scorePanel.setBackground(Color.GRAY);

        //add labels to the score panel to see matches and moves made
        matches = new JLabel("Number of matches: ");
        matchNum = new JLabel(" " + score);
        movesLabel = new JLabel("Number of moves made: ");
        movesNum = new JLabel(" " + numClicks);
        scorePanel.add(matches);
        scorePanel.add(matchNum);
        scorePanel.add(movesLabel);
        scorePanel.add(movesNum);
        scorePanel.validate();
        scorePanel.repaint();
        gamePanel.add(scorePanel, BorderLayout.SOUTH);

        //add all to game frame
        gameFrame.add(gamePanel);

    }

    //adds the cards to be able to be used
    public void addCard() {
        numOfButtons = photoArray.length * 2;
        cardButtons = new JButton[numOfButtons];
        pictures = new ImageIcon[numOfButtons];

        //for loop to go through pictures array adding photos to each card
        for (int i = 0, j = 0; i < photoArray.length; i++) {
            pictures[j] = new ImageIcon(photoArray[i]);
            j = makeCards(j);

            pictures[j] = pictures[j - 1];
            j = makeCards(j);
        }

        //allows the cards to switch positions each time program is ran
        Random rnd = new Random();
        for (int i = 0; i < numOfButtons; i++) {
            int j = rnd.nextInt(numOfButtons);
            temp = pictures[i];
            pictures[i] = pictures[j];
            pictures[j] = temp;
        }

        //timer for photo longevity
        timer = new Timer(1000, new TimerListener());
    }

    //makes action listeners so that cards are able to work and show on panel
    private int makeCards(int j) {
        cardButtons[j] = new JButton("");
        cardButtons[j].addActionListener((ActionListener) new ImgListener());
        cardButtons[j].setIcon(cardBack);
        cardButtons[j].setBackground(Color.BLACK);
        cardPanel.add(cardButtons[j++]);
        return j;
    }

    //action listener for after cards are clicked
    private class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            cardButtons[current].setIcon(cardBack);
            cardButtons[oddClicks].setIcon(cardBack);
            timer.stop();
        }
    }

    //will count clicked photos 
    private class ImgListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (timer.isRunning()) {
                return;
            }
            clickedPhotos++;
            movesNum.setText(Integer.toString(clickedPhotos));
            for (int i = 0; i < numOfButtons; i++) {
                if (e.getSource() == cardButtons[i]) {
                    cardButtons[i].setIcon(pictures[i]);
                    current = i;
                }
            }

            if (clickedPhotos % 2 == 0) {
                if (current == oddClicks) {
                    numClicks--;
                    return;
                }
                //if current picture index doesn't equal odd clicked index, the timer will start
                if (pictures[current] != pictures[oddClicks]) {
                    timer.start();
                } else {
                    //score counter
                    score++;
                    matchNum.setText(Integer.toString(score));
                    //if score is 6, they win!
                    if (score == 6) {
                        JOptionPane.showMessageDialog(gameFrame, "You won!!! You passed with " + clickedPhotos
                                + " moves. \n(Lowest moves possible: 12 moves)");
                    }
                }
            } else {
                oddClicks = current;
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }

}
