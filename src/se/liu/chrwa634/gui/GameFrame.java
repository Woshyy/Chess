package se.liu.chrwa634.gui;

import javax.swing.*;
import java.awt.*;

/**
 * This is the class that represent the gameframe which holds all the different panels.
 * This class extends the JFrame class. Its main objectiv is to create a JFrame where
 * The diffent gamepanels could be shown.
 */
public class GameFrame
{
    private JFrame frame = new JFrame();
    private boolean isPaused = false;
    private GamePanel gamePanel;

    public static void main(String[] args) {
        new GameFrame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public GameFrame(String startPosition){
        gamePanel = new GamePanel(startPosition, this);
        frame.add(gamePanel);
        frame.setTitle("Chess");
        frame.setResizable(false);
        frame.setBackground(Color.BLACK);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void openGameOverGUI() {
        if (!getIsPaused()) {
            pause();
            new GameOverFrame(this);
        }
    }

    public void repaint() {
        frame.repaint();
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void pause() {
        isPaused = true;
    }

    public void unPause() {
        isPaused = false;
    }

    public boolean getIsPaused() {
        return isPaused;
    }
}
