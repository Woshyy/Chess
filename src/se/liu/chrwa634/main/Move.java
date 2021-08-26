package se.liu.chrwa634.main;

/**
 * This class represents a move. It holds information about the starting square, the destination square,
 * the moved piece and if it is a capture move.
 */
public class Move
{
    private int startingSquareFile;
    private int startingSquareRank;
    private int destinationSquareFile;
    private int destinationSquareRank;
    private boolean captureMove;

    public Move(Square startingSquare, Square destinationSquare) {
        startingSquareFile = startingSquare.getFile();
        startingSquareRank = startingSquare.getRank();
        destinationSquareFile = destinationSquare.getFile();
        destinationSquareRank = destinationSquare.getRank();

        setCaptureMove(destinationSquare);
    }

    /**
     * This method returns if the given move is a captureMove.
     * @return true if it is a capture move.
     */
    public boolean isCaptureMove() {
        return captureMove;
    }

    /**
     * This method returns the starting square of the move on a given board.
     * @param board The board in which to get the starting square.
     * @return
     */
    public Square getStartingSquare(Board board) {
	return board.getSquare(startingSquareFile, startingSquareRank);
    }

    /**
     * This method retrieves the destination square of the move on a given board.
     * @param board The board in which to get the starting square.
     * @return
     */
    public Square getDestinationSquare(Board board) {
        return board.getSquare(destinationSquareFile, destinationSquareRank);
    }

    /**
     * This method retrieves the file coordinate of the starting square.
     * @return The file coordinate of the starting square.
     */
    public int getStartingSquareFile() {
        return startingSquareFile;
    }

    /**
     * This method retrieves the file coordinate of the destination square.
     * @return The rank coordinate of the destination square.
     */
    public int getDestinationSquareFile() {
        return destinationSquareFile;
    }

    /**
     * This method set if the move is a capture move.
     * @param square The destination Square.
     */
    private void setCaptureMove(Square square) {
        if (square.isOccupied()) {
            captureMove = true;
        }
        else {
            captureMove = false;
        }
    }
}
