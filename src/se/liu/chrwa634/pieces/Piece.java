package se.liu.chrwa634.pieces;

import se.liu.chrwa634.main.Board;
import se.liu.chrwa634.main.CastlingRight;
import se.liu.chrwa634.main.Move;
import se.liu.chrwa634.main.Square;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an abstract class that represents a piece on a chess board. This abstract class
 * has fields that every class that extends this abstract class should have like color.
 */
public abstract class Piece
{
    protected final Color color;
    protected Square square = null;
    protected ImageIcon img;

    protected  PieceType pieceType;

    protected Piece(Color color, PieceType pieceType, URL imageURL) {
        this.color = color;
        this.pieceType = pieceType;
        img = new ImageIcon(imageURL);
    }

    /**
     * This method returns all the possible moves that the piece could move to. If the removeCheckMove
     * is true than the method would remove every move that would lead to the king being in check.
     * @param board The board in which the piece will move.
     * @return A list of squares that the piece could move to.
     */
    public abstract List<Move> getValidMoves(Board board, boolean removeCheckMove);

    /**
     * This method gets the piece fen annotation.
     * @return A character that represents the piece in a FEN annotation.
     */
    public char getFenChar() {
        char fenChar = getPieceType().getFen();

        if (color.equals(Color.WHITE)) {
            return Character.toUpperCase(fenChar);
	} else {
            return fenChar;
	}
    }

    /**
     * This method returns the square that the piece is currently occupying.
     * @return The square.
     */
    public Square getSquare() {
        return square;
    }

    /**
     * This method set the square field to the given value.
     * @param square The square that the square field will be set to.
     */
    public void setSquare(Square square) {
	this.square = square;
    }

    /**
     * This method returns the PieceType of the piece.
     * @return The PieceType of the piece.
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * This method returns the color of a piece.
     * @return The color of the piece.
     */
    public Color getColor() {
	return color;
    }

    /**
     * This method moves the piece to another square by setting its square field
     * to a given square. If the isRealMove is true than updates are performed after the moving.
     * @param square The square in which the piece will be moved to.
     */
    public void move(Move move, Board board, boolean isRealMove) {
        Square destinationSquare = move.getDestinationSquare(board);
        Square startingSquare = move.getStartingSquare(board);

	board.liftPiece(startingSquare);
        board.placePiece(this, destinationSquare);


        if (isRealMove) {
	    board.resetEnPassantSquare();
	    updateCastlingRights(move, board);

	    if (move.isCaptureMove()) {
		board.resetPositionCounter();
		board.resetFiftyMoveCounter();
	    }
	}
    }

    /**
     * This method checks if a given square is occupied by a enemy piece.
     * @param square The square to check.
     * @return True if it is an enemy otherwise false.
     */
    public boolean isEnemyPiece(Square square) {
        Piece piece = square.getPiece();
        return !color.equals(piece.getColor());
    }

    /**
     * This method checks if a given square is a possible move for the piece. It checks whether if the square is occupied
     * or that the piece can capture and there is an enemy piece on the square. If one of those statements are true
     * that we can move to that square.
     * @param file The file of the square to check.
     * @param rank The rank of the saure to check.
     * @param board The board that the square is on.
     * @param canCapture If the piece is allowed to capture another piece on this square.
     * @return True if the piece could move to the square otherwise false.
     */
    public boolean isSquareValidMove(int file, int rank, Board board, boolean canCapture) {
	if (board.isSquareInsideBoard(file, rank)) {
	    Square checkSquare = board.getSquare(file, rank);
	    if (!checkSquare.isOccupied() || (canCapture && isEnemyPiece(checkSquare))) {
		return true;
	    }
	}
	return false;
    }

    /**
     * This method checks all the possible moves of a given direction. For exampel if fileDirection and rankDirection is one
     * than we check a square that is one unit from the starting square. The starting square is the square with the given coordinate
     * file and rank. In the exampel we would check every single move that is diognaly down to the right of the starting square.
     * If fileDirection and rankDirection is both 0 then this method just returns an emptyList.
     * This method is implemented in a way that it keeps going until it gets outside of the board or find another piece.
     * @param file The file coordinate of the starting square.
     * @param rank The rank coordinate of the starting square.
     * @param fileDirection The direction that it moves in the file every loop.
     * @param rankDirection The direction that it move in the rank every loop.
     * @param board The board that the method checks on.
     * @return Returns all the possible square to move to.
     */
    public List<Move> getPossibleDirectionalMoves(int file, int rank, int fileDirection, int rankDirection, Board board) {

        //If someone calls this method with the fileDirection and rankDirection is 0 then we just return an empty list
	//otherwise the method will be stuck on the while loop.
        if (fileDirection == 0 && rankDirection == 0) {
            return new ArrayList<>();
	}

	int checkFile = file + fileDirection;
	int checkRank = rank + rankDirection;
	List<Move> validMoves = new ArrayList<>();

	while(board.isSquareInsideBoard(checkFile, checkRank)) {
	    Square square = board.getSquare(checkFile, checkRank);
	    if (square.isOccupied()) {
		if (isEnemyPiece(square)) {
		    validMoves.add(new Move(this.square, square));
		}
		break;
	    } else {
		validMoves.add(new Move(this.square, square));
		checkFile += fileDirection;
		checkRank += rankDirection;
	    }
	}
	return validMoves;
    }

    /**
     * This method return all the valid moves for all of the horizontal moves. This is done by calling
     * the getPossibleDirectionalMoves method with different fileDirection and rankDirection values
     * that represent different directions.
     * @param rank The starting squares rank.
     * @param file The starting squares file.
     * @param board The board which will be checked on.
     * @return A list of squares that the piece could move to.
     */
    public List<Move> getHorizontalMoves(int rank, int file, Board board) {
	//down
	List<Move> validMoves = getPossibleDirectionalMoves(rank, file, 1, 0,board);
	//up
	validMoves.addAll(getPossibleDirectionalMoves(rank, file,  -1, 0,board));
	//right
	validMoves.addAll(getPossibleDirectionalMoves(rank, file, 0, 1,board));
	//left
	validMoves.addAll(getPossibleDirectionalMoves(rank, file, 0, -1,board));
	return validMoves;
    }

    /**
     * This method return all the alid mvoe for all of the diagonal moves. This is done by calling
     * the getPossibleDirectionalMoves method with different fileDirection and rankDirection values that
     * represent different direcctions.
     * @param rank The starting squares rank.
     * @param file The starting squares file.
     * @param board The board which will be checked on.
     * @return A list of squares that the piec oudl move to.
     */
    public List<Move> getDiagonalMoves(int rank, int file, Board board) {
        //down right
	List<Move> validMoves = getPossibleDirectionalMoves(rank, file, 1, 1,board);
	//up right
	validMoves.addAll(getPossibleDirectionalMoves(rank, file, 1, -1,board));
	//down left
	validMoves.addAll(getPossibleDirectionalMoves(rank, file, -1, 1,board));
	//up left
	validMoves.addAll(getPossibleDirectionalMoves(rank, file, -1, -1,board));
	return validMoves;
    }

    /**
     * This method draws the piece to a graphics object.
     * @param graphics The graphics which the piece will be drawn to.
     */
    public void draw(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;

	int squareSize = Square.getSquareSize();
	int file = square.getFile() * squareSize;
	int rank = square.getRank() * squareSize;

	Image pieceImage = img.getImage();
	img = new ImageIcon(pieceImage.getScaledInstance(squareSize, squareSize, Image.SCALE_SMOOTH));

	g2d.drawImage(img.getImage(), file, rank, null);
    }

    /**
     * This method updates the castling rights after a move is done.
     * @param move The move that was done.
     * @param board The board that the move was performed.
     */
    public void updateCastlingRights(Move move, Board board) {
	Square destinationSquare = move.getDestinationSquare(board);
	setCastlingRights(board, destinationSquare);
    }

    /**
     * This method set the castlings rights to false if a piece moved to a1, a8, f1, f8. Depending on which square
     * it moves to that a different castling right would be updated.
     * @param board The board which the update should be done.
     * @param square The square that the peice moved to.
     */
    protected void setCastlingRights(Board board, Square square) {
	int file = square.getFile();
	int rank = square.getRank();

	int fileA = File.A.getIndex();
	int fileH = File.H.getIndex();
	int rankOne = Rank.ONE.getIndex();
	int rankEight = Rank.EIGHT.getIndex();

	if (rank == rankOne) {
	    if (file == fileA) {
		board.setCastlingRight(CastlingRight.WHITE_LONG, false);
	    }
	    else if (file == fileH) {
		board.setCastlingRight(CastlingRight.WHITE_SHORT, false);
	    }
	}
	else if (rank == rankEight) {
	    if (file == fileA) {
		board.setCastlingRight(CastlingRight.BLACK_LONG, false);
	    }
	    else if (file == fileH) {
		board.setCastlingRight(CastlingRight.BLACK_SHORT, false);
	    }
	}
    }

    /**
     * This metod returns if the piece is in check.
     * @return If the piece is in check.
     */
    public boolean isInCheck() {
        return false;
    }

    /**
     * This method returns if the piece could checkmate alone with its king.
     * @return true if the piece could deliver checkmate alone with its king.
     */
    public boolean canDeliverMateAlone() {
        return true;
    }
}

