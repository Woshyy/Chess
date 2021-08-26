package se.liu.chrwa634.gui;

import se.liu.chrwa634.main.Board;
import se.liu.chrwa634.main.GameOverType;
import se.liu.chrwa634.main.Position;
import se.liu.chrwa634.main.Square;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class is used to display the game which is currently being played. This class extends the JPanel class.
 */
public class GamePanel extends JPanel
{
    private static final int BOARD_SIZE = 640;
    private static final Dimension SCREEN_SIZE = new Dimension(BOARD_SIZE, BOARD_SIZE);

    private Board board;
    private GameFrame gameFrame;

    /**
     * This is the constructor that initiates the board according to the given fen position. It also adds
     * mouselisteners.
     * @param startPosition The start position of the board.
     */
    public GamePanel(String startPosition, GameFrame gameFrame) {
        this.setFocusable(true);
        this.setPreferredSize(SCREEN_SIZE);
        this.gameFrame = gameFrame;
        board = new Board();
        board.loadPosition(new Position(startPosition));
        addMouseListener(new MouseAdapter()
        {
            @Override public void mouseClicked(final MouseEvent e) {
                if (!isPaused()) {
                    int squareSize = Square.getSquareSize();
                    int file = e.getX()/squareSize;
                    int rank = e.getY()/squareSize;
                    board.select(rank, file);
                    updateBoard();
                    if (board.isGameOver()) {
                        gameFrame.openGameOverGUI();
                    }
                }
            }
        });
    }

    /**
     * This method redraws the board when it is called.
     */
    public void updateBoard() {
        repaint();
    }

    /**
     * This function will set the board to the start position of a normal chess game.
     */
    public void resetBoard() {
       board.resetBoard();
    }

    /**
     * This method draws the board.
     * @param graphics the graphics where we draw the board.
     */
    public void paint(Graphics graphics) {
        Image image = createImage(getWidth(), getHeight());
        Graphics imageGraphics = image.getGraphics();
        draw(imageGraphics);
        graphics.drawImage(image, 0, 0, this);
    }

    /**
     * This method calls the board draw method.
     * @param graphics the graphics where it will be drawn.
     */
    public void draw(Graphics graphics) {
        board.draw(graphics);
    }

    public boolean isWhiteTurn() {
        return board.getIsWhiteTurn();
    }

    public GameOverType getGameOverType() {
        return board.getGameOverType();
    }

    public boolean isPaused() {
        return gameFrame.getIsPaused();
    }

}
