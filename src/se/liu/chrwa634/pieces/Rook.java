package se.liu.chrwa634.pieces;

import se.liu.chrwa634.main.Board;
import se.liu.chrwa634.main.Move;

import java.awt.*;
import java.net.URL;
import java.util.List;

/**
 * This is a class represent a rook in chess. The class extends the piece abstract class.
 * To create a rook you need to give a Color either black or white.
 */
public class Rook extends Piece
{
    /**
     * The constructor takes in color as an argument for setting the color of the piece.
     * @param color The color of the piece. It is either black or white.
     */
    public Rook(Color color, URL imageURL) {
	super(color, PieceType.ROOK, imageURL);
    }

    @Override public void move(Move move, Board board, boolean isRealMove) {
        super.move(move, board, isRealMove);
        updateCastlingRights(move, board);
    }

    @Override public List<Move> getValidMoves(Board board, boolean removeCheckMove) {
        int file = square.getFile();
        int rank = square.getRank();

        List<Move> validMoves = getHorizontalMoves(file, rank, board);

        if (removeCheckMove) {
            validMoves = board.removeInvalidMove(validMoves);
        }
        return validMoves;
    }

    @Override public void updateCastlingRights(Move move, Board board) {
        super.updateCastlingRights(move, board);
        setCastlingRights(board, move.getStartingSquare(board));
    }
}
