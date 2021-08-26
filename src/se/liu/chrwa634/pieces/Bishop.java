package se.liu.chrwa634.pieces;

import se.liu.chrwa634.main.Board;
import se.liu.chrwa634.main.Move;

import java.awt.*;
import java.net.URL;
import java.util.List;

/**
 * This class represent a bishop piece on the chess board. This class has method to retrieve the valid moves
 * of the bishop. This class extends the piece class because a bishop is a piece on the chess board. =
 */
public class Bishop extends Piece
{
    public Bishop(Color color, URL imageURL) {
	super(color, PieceType.BISHOP, imageURL);
    }

    @Override public List<Move> getValidMoves(Board board, boolean removeCheckMove) {
	int file = square.getFile();
	int rank = square.getRank();

	List<Move> validMoves = getDiagonalMoves(file, rank, board);
	if (removeCheckMove) {
	    validMoves = board.removeInvalidMove(validMoves);
	}
	return validMoves;
    }

    @Override public boolean canDeliverMateAlone() {
	return false;
    }
}
