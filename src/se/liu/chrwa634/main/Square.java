package se.liu.chrwa634.main;

import se.liu.chrwa634.pieces.Piece;

import java.awt.*;


/**
 * This class represents a square on a chessboard. The square must have a location
 * that are represented as a file and rank on the chessboard. The Square could also
 * contain a Piece object and this represents that a piece is currently occupying the
 * square.
 *
 * The square also requires a color(which is either black or white that is essential
 * for drawing the object. The Square class has also a flag, isSelected, in which
 * if it is true then we also draw a marker. The marker represent that a piece could
 * be moved into this square. The square has two static constants SQUARE_SIZE
 * and SQUARE_SELECT_SIZE. SQUARE_SIZE is lenght of a side of the drawn square.
 * SQUARE_SELECT_SIZE is the radius of the marker.
 *
 * @author Chrwa634
 * @version 1.0
 */
public class Square
{
    private final static int SQUARE_SIZE = 80;
    private final static int SQUARE_SELECT_SIZE = 20;
    private final static Color[] GUI_COLORS = {new Color(0xFFD9D9), new Color(0x8E2CA0)};
    private Color color;
    private Piece piece = null;
    private int file;
    private int rank;
    private Board board;

    public Square(int file, int rank, Color color, Board board) {
        this.board = board;
        this.color = color;
        this.file = file;
        this.rank = rank;
    }

    /**
     * This method places a piece on this square. If there is already
     * a piece occupying this square then that piece would get replaced.
     * This method also updates the piece that is has moved to another square.
     *
     * @param piece The piece which will be placed on the square.
     */
    public void placePiece(Piece piece) {
        this.piece = piece;
        piece.setSquare(this);
    }

    /**
     * This method simply remove the piece from this square by setting piece
     * to null.
     */
    public void removePiece() {
        this.piece = null;
    }

    /**
     * This method returns the piece which is currently occupying this square.
     * @return The piece which is occupying the square.
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * This method returns the color of the square.
     * @return The color of the square.
     */
    public Color getColor() {
        return color;
    }

    /**
     * This method return the file of the square.
     * @return The file of the square.
     */
    public int getFile() {
        return file;
    }

    /**
     * This method returns the rank of the square.
     * @return The rank of the square.
     */
    public int getRank() {
        return rank;
    }

    /**
     * This method checks if there is currently a piece occupying this square.
     * The method does this by checking if piece is null.
     * @return a boolean value according to if the square is occupied.
     */
    public boolean isOccupied() {
        if (piece == null) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * This method gets the SQUARE_SIZE constant of the square class
     * @return The SQUARE_SIZE of the square class.
     */
    public static int getSquareSize() {
        return SQUARE_SIZE;
    }

    /**
     * This method draws the square. If isSelected equals to true then we draw a marker.
     * If isSelected equals to true and there is a piece occupying this square then we
     * draw a pink rectangle, which represents that the move would capture the piece
     * that is currently occupying the square.
     * @param graphics The graphics object which would be drawn on.
     */
    public void draw(Graphics graphics) {
        int xDraw = getFile() * SQUARE_SIZE;
        int yDraw = getRank() * SQUARE_SIZE;
        graphics.setColor(getDrawingColor());
        graphics.fillRect(xDraw, yDraw, SQUARE_SIZE, SQUARE_SIZE);
        if (board.isSquareSelected(this)) {
            if (isOccupied()) {
                graphics.setColor(Color.PINK);
                graphics.fillRect( xDraw,yDraw, SQUARE_SIZE, SQUARE_SIZE);
            } else {
                //an offset to put the circle in the middle of the square.
                int circleOffset = (SQUARE_SIZE - SQUARE_SELECT_SIZE) / 2;
                graphics.setColor(Color.CYAN);
                graphics.fillOval(xDraw + circleOffset, yDraw + circleOffset,
                                  SQUARE_SELECT_SIZE, SQUARE_SELECT_SIZE);
            }
        }
    }

    /**
     * This method checks if this square is attacked by a specific piece color.
     * @param color The color to check
     * @return true if the square is being attacked by a piece of the specified color.
     */
    public boolean isAttacked(Color color) {
        return board.isSquareAttacked(file, rank, color);
    }

    /**
     * This method returns the drawing color of the square on the panel.
     * @return The drawing color of the square.
     */
    private Color getDrawingColor() {
        if (color.equals(Color.WHITE)) {
            return GUI_COLORS[0];
        } else {
            return GUI_COLORS[1];
        }
    }
}
