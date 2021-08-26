package se.liu.chrwa634.pieces;

import se.liu.chrwa634.main.Board;
import se.liu.chrwa634.main.Move;

import java.awt.*;
import java.net.URL;
import java.util.List;

/**
 * This class represent the queen piece in chess. It holds methods
 * to get the diffrent squares this queen piece could move to.
 */
public class Queen extends Piece
{
    public Queen(Color color, URL imageURL) {
	super(color, PieceType.QUEEN, imageURL);
    }

    @Override public List<Move> getValidMoves(Board board, boolean removeCheckMove) {
	int file = square.getFile();
	int rank = square.getRank();

	List<Move> validMoves = getHorizontalMoves(file, rank, board);
	validMoves.addAll(getDiagonalMoves(file, rank, board));

	if (removeCheckMove) {
	    validMoves = board.removeInvalidMove(validMoves);
	}

	return validMoves;
    }
}
