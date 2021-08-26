package se.liu.chrwa634.pieces;

import se.liu.chrwa634.main.Board;
import se.liu.chrwa634.main.Move;
import se.liu.chrwa634.main.Square;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This method represent a knight piece on the chess board. The class method that returns all the possible move
 * that this knight piece could do.
 */
public class Knight extends Piece
{
    public Knight(Color color, URL imageURL) {
	super(color, PieceType.KNIGHT, imageURL);
    }

    @Override public List<Move> getValidMoves(Board board, boolean removeCheckMove) {
        final int searchRadius = 2;
        int rank = square.getRank();
        int file = square.getFile();
        List<Move> validMoves = new ArrayList<>();

        for (int fileDirection = -searchRadius; fileDirection <= searchRadius; fileDirection++) {
            for (int rankDirection = -searchRadius; rankDirection <= searchRadius; rankDirection++) {
                int checkFile = file + fileDirection;
                int checkRank = rank + rankDirection;
                if ( ((fileDirection + rankDirection) % 2 != 0) && fileDirection != 0 &&
                     rankDirection != 0) {
                    if (isSquareValidMove(checkFile, checkRank, board, true)) {
                        Square destinationSquare = board.getSquare(checkFile, checkRank);
                        validMoves.add(new Move(square, destinationSquare));
                    }
                }
            }
        }

        if (removeCheckMove) {
            validMoves = board.removeInvalidMove(validMoves);
        }
        return validMoves;
    }

    @Override public boolean canDeliverMateAlone() {
        return false;
    }

}
