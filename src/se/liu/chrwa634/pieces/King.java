package se.liu.chrwa634.pieces;

import se.liu.chrwa634.main.Board;
import se.liu.chrwa634.main.CastlingRight;
import se.liu.chrwa634.main.Move;
import se.liu.chrwa634.main.Square;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

/**
 * This class represents the king in the chess board. This class has method to update castling rights
 * and gettting all the valid moves for the king.
 */
public class King extends Piece
{

    public King(Color color, URL imageURL) {
        super(color, PieceType.KING, imageURL);
    }

    @Override public void move(Move move, Board board, boolean isRealMove) {
        super.move(move, board, isRealMove);
        updateCastlingRights(board);

        Square startingSquare = move.getStartingSquare(board);
        Square destinationSquare = move.getDestinationSquare(board);


        if (isCastlingMove(startingSquare, destinationSquare)) {
            doCastlingMove(move, board);
        }

    }

    @Override public boolean isInCheck() {
        Color oppositeColor = color.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
        return square.isAttacked(oppositeColor);
    }

    @Override public boolean canDeliverMateAlone() {
        return false;
    }

    /**
     * This method return a list of squares in which the king could move to.
     * @param board The board where the king is currenly placed.
     * @return A list of squares in which the king could move to.
     */
    @Override public List<Move> getValidMoves(final Board board, boolean removeCheckMove) {
        int rank = square.getRank();
        int file = square.getFile();
        List<Move> validMoves = new ArrayList<>();

        for (int fileDirection = -1; fileDirection <= 1; fileDirection++) {
            for (int rankDirection = -1; rankDirection <= 1; rankDirection++) {
                if (!(fileDirection == 0 && rankDirection == 0)) {
                    if (isSquareValidMove(file + fileDirection, rank + rankDirection, board, true)) {
                        Square destinationSquare = board.getSquare(file + fileDirection, rank + rankDirection);
                        validMoves.add(new Move(square, destinationSquare));
                    }
                }
            }
        }

        if (removeCheckMove) {
            validMoves = board.removeInvalidMove(validMoves);
            validMoves.addAll(getCastlingMoves(board));
        }
        return validMoves;
    }

    @Override public void draw(final Graphics graphics) {
        Graphics2D g2D = (Graphics2D)graphics;
        int squareSize = Square.getSquareSize();
        int file = square.getFile() * squareSize;
        int rank = square.getRank() * squareSize;

        if (isInCheck()) {
            g2D.setColor(Color.RED);
            g2D.drawOval(file, rank, squareSize, squareSize);
            g2D.fillOval(file, rank, squareSize, squareSize);
        }
        super.draw(graphics);

    }

    /**
     * This method checks if a given move is a long castle.
     * @param move The move that will be checked.
     * @return true if it is a long castle.
     */
    private boolean isLongCastle(Move move) {
        int startingSquareFile = move.getStartingSquareFile();
        int destinationSquareFile = move.getDestinationSquareFile();

        return startingSquareFile > destinationSquareFile;
    }

    /**
     * This method is a help method to perform a castling move.
     * @param move A castling move that need to be performed.
     * @param board The board in which the move should be performed.
     */
    private void doCastlingMove(Move move, Board board) {
        Square destinationSquare = move.getDestinationSquare(board);
        int direction = isLongCastle(move) ? 1 : -1;

        //Get the right square which has the rook depending if it is long castle or short castle.
        Square rookSquare = isLongCastle(move) ? board.getSquare(File.A, destinationSquare.getRank()) :
                            board.getSquare(File.H, destinationSquare.getRank());

        //Get the destination square for the rook.
        Square rookDestinationSquare = board.getSquare(destinationSquare.getFile() + direction, destinationSquare.getRank());
        Piece rook = rookSquare.getPiece();

        //Move the rook to its destinationsquare.
        board.liftPiece(rookSquare);
        board.placePiece(rook, rookDestinationSquare);
        //All the previous positions could not be achieved any longer.
        board.resetPositionCounter();
    }

    /**
     * This method updates castling rights by setting them to false.
     * @param board The baord where the castling rights will be updated.
     */
    private void updateCastlingRights(Board board) {
        CastlingRight longCastle = color.equals(Color.WHITE) ? CastlingRight.WHITE_LONG : CastlingRight.BLACK_LONG;
        CastlingRight shortCastle = color.equals(Color.WHITE) ? CastlingRight.WHITE_SHORT : CastlingRight.BLACK_SHORT;

        board.setCastlingRight(longCastle, false);
        board.setCastlingRight(shortCastle, false);
    }

    /**
     * This method checks if a move is a castling move. This is done by firstly checking if the moved piece is a king.
     * And then checking if the file difference between the starting and the destination square is larger than 1.
     * If both of these statements are true that the move must be a castling move.
     * @param starting The starting square.
     * @param destination The destination square.
     * @return True if the give move is a castling move otherwise false.
     */
    private boolean isCastlingMove(Square starting, Square destination) {
        final int twoSquareMove = 2;
        return abs(starting.getFile() - destination.getFile()) >= twoSquareMove;
    }

    /**
     * This method checks castling moves that the king could do.
     * @param board The board in which the method check the castling rights on.
     * @return A list of squares in which the king could move to.
     */
    private List<Move> getCastlingMoves(Board board) {
        int rank = square.getRank();
        int file = square.getFile();
        Color oppositeColor = getOppositeColor();

        Square e1 = board.getSquare(File.E, Rank.ONE);
        Square e8 = board.getSquare(File.E, Rank.EIGHT);

        List<Move> validMoves = new ArrayList<>();
        //check if the king is on its starting square
        if (square.equals(e1) || square.equals(e8)) {
            CastlingRight shortCastling = color.equals(Color.WHITE) ? CastlingRight.WHITE_SHORT : CastlingRight.BLACK_SHORT;
            if (board.getCastlingRight(shortCastling) && !isInCheck()) {
                // Check if the G and F files are not occupied or attacked...
                Square fileF = board.getSquare(File.F, rank);
                Square fileG = board.getSquare(File.G, rank);
                Square fileH = board.getSquare(File.H, rank);
                if (!fileF.isOccupied() && !fileG.isOccupied() && fileH.isOccupied()) {
                    int directionRightValue = Direction.RIGHT.getDirectionalValue();
                    if (!board.isSquareAttacked(file + directionRightValue, rank, oppositeColor)
                        && !board.isSquareAttacked(file + (2 * directionRightValue), rank, oppositeColor)) {
                        // If they are not attacked or occupied than it is possible to castle short.
                        validMoves.add(new Move(square, fileG));
                    }
                }
            }

            CastlingRight longCastling = color.equals(Color.WHITE) ? CastlingRight.WHITE_LONG: CastlingRight.BLACK_LONG;
            if (board.getCastlingRight(longCastling) && !isInCheck()) {
                // Check if the C and D files are not occupied or attacked...
                Square filleD = board.getSquare(File.D, rank);
                Square fileC = board.getSquare(File.C, rank);
                Square fileA = board.getSquare(File.A, rank);
                if (!filleD.isOccupied() && !fileC.isOccupied() && fileA.isOccupied()) {
                    int directionLeftValue = Direction.LEFT.getDirectionalValue();
                    if (!board.isSquareAttacked(file + directionLeftValue, rank, oppositeColor)
                        && !board.isSquareAttacked(file + (2 * directionLeftValue), rank, oppositeColor)) {
                        // If they are not attacked or occupied than it is possible to castle long.
                        validMoves.add(new Move(square, fileC));

                    }
                }
            }
        }
        return validMoves;
    }

    /**
     * This method returns the opposite color of this piece.
     * @return The opposite color.
     */
    private Color getOppositeColor() {
        return color.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
    }
}
