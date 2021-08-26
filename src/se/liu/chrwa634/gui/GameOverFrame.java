package se.liu.chrwa634.gui;

import se.liu.chrwa634.main.GameOverType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class takes care of the gameOverFrame. The class would handle the input from the user and
 * handle it accordingly.
 */
public class GameOverFrame implements ActionListener
{
    private JButton playAgainButton = new JButton("Play Again");
    private JButton quitButton = new JButton("Quit");

    private JFrame frame;
    private GameFrame gameFrame;

    public GameOverFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        this.frame = createFrame();
    }

    public JFrame createFrame() {
        GamePanel gamePanel = gameFrame.getGamePanel();
        GameOverType gameOverType = gamePanel.getGameOverType();

        JFrame frame = new JFrame("Game Over");
        frame.setSize(new Dimension(300, 300));
        frame.setLocationRelativeTo(gameFrame.getFrame());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        String loseText = gameOverType.getLoseTextComment();
        JLabel loseLabel = new JLabel();
        loseLabel.setText(loseText);
        loseLabel.setFont(new Font("MV Boli", Font.BOLD, 25));
        loseLabel.setHorizontalAlignment(JLabel.CENTER);
        loseLabel.setVerticalAlignment(JLabel.BOTTOM);
        frame.add(loseLabel, BorderLayout.NORTH);

        JLabel winLabel = new JLabel();
        if (gameOverType == GameOverType.CHECKMATE) {
            String winPlayer = gamePanel.isWhiteTurn() ? "Black" : "White";
            winLabel.setText(winPlayer + " won the game!");
        }
        else {
            winLabel.setText("The game is a draw");
        }
        winLabel.setVerticalAlignment(JLabel.CENTER);
        winLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(winLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        playAgainButton.addActionListener(this);
        buttonPanel.add(playAgainButton);
        quitButton.addActionListener(this);
        buttonPanel.add(quitButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        return frame;
    }

    @Override public void actionPerformed(final ActionEvent e) {
        if (e.getSource().equals(playAgainButton)) {
            GamePanel gamePanel = gameFrame.getGamePanel();
            gamePanel.resetBoard();
            frame.dispose();
            gameFrame.repaint();
            gameFrame.unPause();
        }
        else if (e.getSource().equals(quitButton)) {
            System.exit(0);
        }
    }
}
