package se.liu.chrwa634.main;

import se.liu.chrwa634.pieces.Piece;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


/**
 * This class has the objective to scan the board for different scenarios. The class has the main objective
 * to check is a gameOver scenario is achieved. The class also remembers which squares that are currently is being considered as a move and
 * the square that was selected by the user. The class saves the castlingrights and the active player. The class saves
 * also the enpassant square.
 */
public class BoardScanner
{
    private Board board;

    private List<Square> squareSelected = new ArrayList<>();
    private Square selectedSquare;
    private List<Position> previousPosition = new ArrayList<>();
    private Square enPassantSquare;
    private EnumMap<CastlingRight, Boolean> castlingRightMap = new EnumMap<>(CastlingRight.class);
    private boolean isWhiteTurn;

    public BoardScanner(Board board) {
        this.isWhiteTurn = true;
        this.board = board;
	enPassantSquare = null;
	selectedSquare = new Square(-1, -1, Color.WHITE, this.board);
    }

    /**
     * This method returns if it is whites turn.
     * @return true if it is whites turn otherwise false.
     */
    public boolean getIsWhiteTurn() {
        return isWhiteTurn;
    }

    /**
     * This method set the field isWhiteTurn to the given value.
     * @param value The vaulue that will be set to the field isWhiteTurn.
     */
    public void setIsWhiteTurn(boolean value) {
        isWhiteTurn = value;
    }

    /**
     * This method retrieves the selected square.
     * @return Square which is currently being selected by the user.
     */
    public Square getSelectedSquare() {
        return selectedSquare;
    }

    /**
     * This method sets the selectedSquare field to a give square.
     * @param square The square that will be the new selectedSquare.
     */
    public void setSelectedSquare(Square square) {
        selectedSquare = square;
    }


    /**
     * This method checks if the given color king is currently in check.
     * @param color The kings color.
     * @return True if the king is in check.
     */
    public boolean isKingInCheck(Color color, Board board) {
        List<Piece> pieces = board.getPlayerPieces(color);
        // Check if any piece with that color is in check.
	for (Piece piece : pieces) {
	    if (piece.isInCheck()) {
	        return true;
	    }
	}
	return false;
    }

    /**
     * This method checks if the current position on the board has met a gameOver criteria.
     * @param board The board that will be checked.
     * @return A gameOverType corresponding to the gameover. If there was no criteria met return GameOverType.NULL.
     */

    public GameOverType getGameOver(Board board) {
        Color color = board.getIsWhiteTurn() ? Color.BLACK : Color.WHITE;

        // Check for stalemate or checkmate
        if (!hasValidMove(board)) {
            if (isKingInCheck(color, board)) {
                return GameOverType.CHECKMATE;
	    }
            else {
                return GameOverType.STALEMATE;
	    }
	}

        // Check for three fold repetition.
	else if (isThreeFoldRepetition(board)) {
	    return GameOverType.THREEFOLD_REPETITION;
	}
	// check for insufficientpieces.
	else if(isInsufficientPieces(board)) {
	    return GameOverType.INSUFFICIENT_PIECE;
	}

	// Check for fiftymove rule
	else if (isFiftyMoveDraw(board)) {
	    return GameOverType.FIFTY_MOVE_DRAW;
	}

	return GameOverType.NULL;
    }

    /**
     * Check if the current active player on a board has a validMove.
     * @param board The board which will be checked.
     * @return true if the player has a valid move otherwise false.
     */
    public boolean hasValidMove(Board board) {
	Color color = board.getIsWhiteTurn() ? Color.BLACK : Color.WHITE;
	List<Piece> playerPieces = board.getPlayerPieces(color);

	// check if any of the player piece has a valid move.
	for (Piece piece : playerPieces) {
	    List<Move> validMoves = piece.getValidMoves(board, true);
	    if (!validMoves.isEmpty()) {
		return true;
	    }
	}
	return false;
    }


    /**
     * This method checks if a square is attacked by the given color. Attack means that a piece of the specified color could
     * move to that square.
     * @param file The file of the square that will be checked.
     * @param rank The rank of the square that will be checked.
     * @param color The color of the piece that can move to the square.
     * @param board The board that will be checked.
     * @return
     */
    public boolean isSquareAttacked(int file, int rank, Color color, Board board){

	//check if any piece is attacking from different directions.
	for (int fileDirection = -1; fileDirection <= 1; fileDirection++) {
	    for (int rankDirection = -1; rankDirection <= 1; rankDirection++) {
		if (!(fileDirection == 0 && rankDirection == 0) && isLineAttacked(file, rank, fileDirection, rankDirection, color, board)) {
		    return true;
		}
	    }
	}

	//check for knights by checking all the squares that a knight could attack from.
	if (isSquareAttackedByKnights(file, rank, color, board)) {
	    return true;
	}
	return false;
    }


    /**
     * This method checks if a line is attacked. A line means a horizontal or a diagonal direction from a square.
     * @param file The file of the square that will be checked.
     * @param rank The rank of the square that will be checked.
     * @param fileDirection The direction that the file will move every iteration.
     * @param rankDirection The direction that the rank will move every iteration.
     * @param board The board which will be checked on.
     * @return True if the squre is attacked from the specified direction.
     */
    public boolean isLineAttacked(int file, int rank, int fileDirection, int rankDirection, Color color, Board board){
	int checkFile = file + fileDirection;
	int checkRank = rank + rankDirection;
	Square square = board.getSquare(file, rank);

	while(true) {
	    // while we are still inside the board keep checking.
	    if (!isSquareInsideBoard(checkFile, checkRank)) {
	        // If we have reached outside the board then return false.
		return false;
	    }
	    // if the square is occupied...
	    Square checkSquare = board.getSquare(checkFile, checkRank);
	    if (isPieceAttackingSquare(checkSquare, square, color)) {
	        return true;
	    }
	    // If no stop condition were met keep looking
	    checkFile += fileDirection;
	    checkRank += rankDirection;
	}
    }

    /**
     * This method return true if the given piece color matches the given color.
     * @param piece The piece to check.
     * @param color The color to check.
     * @return true if the piece color and color are the same.
     */
    public boolean isFriendlyPiece(Piece piece, Color color) {
        return piece.getColor().equals(color);
    }

    /**
     * This method checks if a specific square is attacked by a knight.
     * @param file The file of the square that will be checked.
     * @param rank The rank of the square that will be checked.
     * @param fileDirection The file direction of the square relative to the square that is going to be checked.
     * @param rankDirection The rank direction of the square relative to the square that is going to be checked.
     * @param board The board in which we checked the squares.
     * @return True if the square is being attacked by a knight on the given direction.
     */
    public boolean isSquareAttackedByKnights(int file, int rank, Color color, Board board) {
	final int squareSearch = 2;
	final Square square = board.getSquare(file, rank);

	for (int fileDirection = -squareSearch; fileDirection <= squareSearch; fileDirection++) {
	    for (int rankDirection = -squareSearch; rankDirection <= squareSearch; rankDirection++) {
		if((fileDirection + rankDirection) % 2 != 0 && fileDirection != 0 && rankDirection != 0) {
		    int checkFile = file + fileDirection;
		    int checkRank = rank + rankDirection;

		    if (isSquareInsideBoard(checkFile, checkRank)) {
			Square checkSquare = board.getSquare(checkFile, checkRank);
			if (isPieceAttackingSquare(checkSquare, square, color)) {
			    return true;
			}
		    }
		}
	    }
	}
	return false;
    }

    /**
     * This method checks if a given square pieceSquare has a piece with the right color that could move to
     * the square checkSquare.
     * @param pieceSquare The square that could have a valid piece.
     * @param checkSquare The square which the piece should be able to move to.
     * @param color The color that the piece should have.
     * @return True if valid piece exist in the pieceSquare.
     */
    public boolean isPieceAttackingSquare(Square pieceSquare, Square checkSquare, Color color) {
        if (pieceSquare.isOccupied()) {
	    Piece piece = pieceSquare.getPiece();
	    if (isFriendlyPiece(piece, color)) {
		List<Move> validMoves = piece.getValidMoves(board, false);
		for (Move validMove : validMoves) {
		    Square destinationSquare = validMove.getDestinationSquare(board);
		    if (destinationSquare.equals(checkSquare)) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    /**
     * This method checks if a given square coordinate is inside the board.
     * @param file The file coordinate to check.
     * @param rank The rank coordinate to check.
     * @return True if it is inside the board otherwise false.
     */
    public boolean isSquareInsideBoard(int file, int rank) {
	int boardSize = Board.getBoardSize() - 1;
	int startSize = 0;
	return (file >= startSize && file <= boardSize) && (rank >= startSize && rank <= boardSize);
    }

    /**
     * This method removes all the invalid moves in a given list of moves. Invalid move means move that leads
     * to the king being in check.
     * @param movesToCheck The list of moves to check.
     * @return The list of move that are valid.
     */
    public List<Move> removeInvalidMoves(List<Move> movesToCheck) {
        String position = board.convertBoardToFen();
        List<Move> validMoves = new ArrayList<>();
        //Go through all the moves...
	for (Move move : movesToCheck) {
	    Piece pieceToMove = move.getStartingSquare(board).getPiece();
	    Color color = pieceToMove.getColor();

	    // Create a seperate board with the current position loaded and perform the move on the board.
	    Board imaginaryBoard = new Board();
	    imaginaryBoard.loadPosition(new Position(position));
	    // it is not a realmove so set that to false.
	    imaginaryBoard.movePiece(move, false);
	    // If the king is not in check after performing the move than it is a vliad move.
	    if (!isKingInCheck(color, imaginaryBoard)) {
	        validMoves.add(move);
	    }
	}
	return validMoves;
    }

    /**
     * This method checks if both of the player has sufficient pieces to checkmate their opponent.
     * @param board The board that will be checked.
     * @return true if both of the player does not have sufficient pieces to checkmate the opponent.
     */
    private boolean isInsufficientPieces(Board board) {
        List<Piece> whitePieces = board.getPlayerPieces(Color.WHITE);
        List<Piece> blackPieces = board.getPlayerPieces(Color.BLACK);

	if (isInsufficientMaterial(whitePieces) && isInsufficientMaterial(blackPieces)) {
	    return true;
	}
	else {
	    return false;
	}
    }

    /**
     * This method checks if a the list of pieces can checkmate.
     * @param pieces The list of pieces to check.
     * @return false if the list of pieces can checkmate.
     */
    public boolean isInsufficientMaterial(List<Piece> pieces) {
	final int sufficientAmountOfPieces = 3;

        if (pieces.size() >= sufficientAmountOfPieces) {
            boolean blackBishop = false;
            boolean whiteBishop = false;
            int nonBishopPiece = 0;
            final int maxAmountOfNonBishopPiece = 2;
            final char bishopChar = 'b';
	    for (Piece piece : pieces) {
	        char lowerFenChar = Character.toLowerCase(piece.getFenChar());
	        if (lowerFenChar == bishopChar) {
	                Square pieceSquare = piece.getSquare();
	                Color color = pieceSquare.getColor();

	                if (color.equals(Color.WHITE)) {
	                    whiteBishop = true;
			} else {
	                    blackBishop = true;
			}
		    }
		else {
		    nonBishopPiece += 1;
		}

		if ((whiteBishop && blackBishop) || nonBishopPiece >= maxAmountOfNonBishopPiece) {
		    return false;
		}
	    }
	}
        else {
	    for (Piece piece : pieces) {
		if (piece.canDeliverMateAlone()) {
		    return false;
		}
	    }
	}
        return true;
    }

    /**
     * This method checks if a position has been repeated three times.
     * @param board The board that will be checked.
     * @return true if a position has been repeated three times.
     */
    public boolean isThreeFoldRepetition(Board board) {
        final int maxRepeatedPosition = 3;

	Map<String, Integer> positionCounter = board.getPositionCounter();
	for (int amountPositionRepeated : positionCounter.values()) {
	    if (amountPositionRepeated >= maxRepeatedPosition) {
	        return true;
	    }
	}
	return false;
    }

    /**
     * This method checks if there has not been any captures or movements of pawn in 50 moves.
     * @param board The board that will be checked.
     * @return True if there have not been any capture of pawn movements for 50 moves.
     */
    public boolean isFiftyMoveDraw(Board board) {
        final int fiftyCounterMax = 50;

        if (board.getFiftyMoveCounter() >= fiftyCounterMax) {
            return true;
	}
        else {
            return false;
	}
    }

    /**
     * This method clears the selectedSquares list.
     */
    public void clearSelected() {
	squareSelected.clear();
    }

    /**
     * This method add a list of selected squares into the selectedSquare list.
     * @param moves
     */
    public void addSelectedSquare(List<Move> moves) {
	for (Move move : moves) {
	    squareSelected.add(move.getDestinationSquare(board));
	}
    }

    /**
     * This method checks if a given square is in the selected list.
     * @param square The square to check.
     * @return True if the suqare is in the selected square list.
     */
    public boolean isSquareSelected(Square square) {
        return squareSelected.contains(square);
    }

    /**
     * This method adds the previous position into the previousPosition list.
     */
    public void addPreviousPosition() {
        previousPosition.add(new Position(board.convertBoardToFen()));
    }

    /**
     * This method retrieves the previous position.
     * @return The previous posiition.
     */
    public Position getPreviousPosition() {
	int lastIndex = previousPosition.size() - 1;
	return previousPosition.get(lastIndex);
    }

    /**
     * This method retrieves the en passant square.
     * @return the en passant square.
     */
    public Square getEnPassantSquare() {
        return enPassantSquare;
    }

    /**
     * This method set the en passant square to the give square.
     * @param square The square that will be set as the en passant square.
     */
    public void setEnPassantSquare(Square square) {
        enPassantSquare = square;
    }
    /**
     * This method reset the en passant square by giving it a value of a square that does not exist on the board.
     */
    public void resetEnPassantSquare() {
        enPassantSquare = null;
    }

    /**
     * This method sets a castling right to a specific value.
     * @param castlingRight The castling right that will be set.
     * @param value The value that it will be set to.
     */
    public void setCastlingRight(CastlingRight castlingRight, boolean value) {
        castlingRightMap.put(castlingRight, value);
    }

    /**
     * This method retrieves a castling right value.
     * @param castlingRight The castling right value that will be retrieved.
     * @return a boolean value that represent if you could castle a specific way.
     */
    public boolean getCastlingRight(CastlingRight castlingRight) {
        return castlingRightMap.get(castlingRight);
    }
}
