package se.liu.chrwa634.main;

import se.liu.chrwa634.pieces.Piece;
import se.liu.chrwa634.pieces.PieceFactory;
import se.liu.chrwa634.pieces.PieceType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class has the objective to return the boards FEN annotation and to load the position to a
 * board. The class has a piecefactory so it could create new pieces when loading the position to
 * a board.
 */
public class FenConverter
{
    private static final int CHAR_OFFSET = 97;
    private PieceFactory pieceFactory;


    public FenConverter() {
        pieceFactory = new PieceFactory();
    }


    /**
     * This method return the Square which was given as a chess notation. A normal
     * chess notation uses first an alphabetic chracter between A and H which denotes the rank
     * whilst a number denoting the file. The rank is obtained by subracting the char with a char-offset
     * which would get our value that we want. The file value is attained by subtracting the size of the
     * board with the value which was given.
     * @param move A two char string which represent a square in a chess board.
     * @param board The board which a square is going to be returned from.
     * @return The square which was represented by a chess notation.
     */
    private Square getSquareFromNotation(String move, Board board) {
	int file = (int)move.charAt(0) - CHAR_OFFSET;
	int rank = Board.getBoardSize() - Character.getNumericValue(move.charAt(1));
	return board.getSquare(file, rank);
    }

    /**
     * This method returns the castling notation of a give position. It does this by getting the different
     * colors castling rights through the object board. If none are true that it woud write a - representing
     * both players does not have castling-rights.
     * @param board The board to get the different castling rights.
     * @return A string which is the castling notation of the given position.
     */
    private String getCastlingNotation(Board board) {
	StringBuilder castlingNotation = new StringBuilder();

	for (CastlingRight castlingRight : CastlingRight.values()) {
	    if (board.getCastlingRight(castlingRight)) {
		castlingNotation.append(castlingRight.getFen());
	    }
	}
	if (castlingNotation.isEmpty()) {
	    castlingNotation.append('-');
	}

	return castlingNotation.toString();
    }

    /**
     * A help method to add a piece from a square into the fen String.
     * @param fen a stringbuilder currently use to build the FEN String.
     * @param square The square which is occupied by a piece.
     */
    private void addSquareToFen(StringBuilder fen, Square square) {
	Piece piece = square.getPiece();
	fen.append(piece.getFenChar());
    }

    public Piece getPiece(PieceType pieceType, Color color) {
        return pieceFactory.getPiece(pieceType, color);
    }
    /**
     * Returns the associated piece of the FEN notations. The color of the piece is
     * found by looking is the given character is capitalized. And afterwards
     * with a switch case the associted piece could be determined
     * @param character a charater which associate a chess piece
     * @return The chess piece which the given character was associating.
     */
    public Piece getFenPiece(char character) {
	Color color = Character.isUpperCase(character) ? Color.WHITE : Color.BLACK;
	PieceType pieceType = PieceType.getPieceTypeFromChar(character);
	return getPiece(pieceType, color);
    }

    /**
     * This function convert and returns a FEN string of the board. This is done by first taking a rank
     * from the board and afterwords going through every single square in that rank.
     * If the square is not currently occupied by a piece than increase the variable emptySquares.
     * The unoccupiedSquares variable will be used for counting the amount of unoccupied squares between pieces or
     * from the edge of the board to a piece.
     *
     * If we find a piece then add the number of emptySquares to the String if it is not 0 and afterwards add
     * the piece fen notation. At the end of a rank loop check, if the emptySquares is not 0 then add it to the string and
     * add a / which represents a new rank.
     *
     * after that we add the castlingRights to the fen strings by looking up the different castling rights through board.
     * after that we convert our StringBuilder to a string and return it.
     * @param board The board which will be converted as a FEN string.
     * There will not be any changes to the given board.
     * @return The FEN string of the board.
     */
    public String convertBoardToFen(Board board) {
	int unoccupiedSquares = 0;
	int boardSize = Board.getBoardSize();
	StringBuilder fen = new StringBuilder();

	for (int rank = 0; rank < boardSize; rank++) {
	    for (int file = 0; file < boardSize; file++) {
		if (board.isOccupied(file, rank)) {
		    if (unoccupiedSquares != 0) {
			fen.append(unoccupiedSquares);
			unoccupiedSquares = 0;
		    }
		    Square currentSquare = board.getSquare(file, rank);
		    addSquareToFen(fen, currentSquare);
		} else {
		    unoccupiedSquares += 1;
		}
	    }
	    if (unoccupiedSquares != 0) {
		fen.append(unoccupiedSquares);
		unoccupiedSquares = 0;
	    }
	    fen.append('/');
	}

	//Remove the last slash and add a space instead.
	int fenLength = fen.length();
	fen.setCharAt(fenLength - 1, ' ');

	fen.append(getActivePlayerNotation(board));

	//Adding castlingnotations
	fen.append(' ');
	fen.append(getCastlingNotation(board));
	fen.append(' ');

	fen.append(getEnPassantNotation(board));
	fen.append(' ');

	fen.append(getFiftyMoveCounterNotation(board));
	fen.append(' ');

	return fen.toString();
    }

    public String getEnPassantNotation(Board board) {
	List<Character> files = getFileList();
	Square enPassantSquare = board.getEnPassantSquare();

	if (!board.doesEnPassantSquareExist()) {
	    return "-";
	}

	int squareFile = enPassantSquare.getFile();
	int squareRank = enPassantSquare.getRank();

	String fenAnnotation = files.get(squareFile) + Integer.toString(squareRank);
	return fenAnnotation;
    }

    private char getActivePlayerNotation(Board board) {
	if (board.getIsWhiteTurn()) {
	    return 'w';
	} else {
	    return 'b';
	}
    }

    private String getFiftyMoveCounterNotation(Board board) {
        return Integer.toString(board.getFiftyMoveCounter());
    }

    private List<Character> getFileList() {
	List<Character> files = new ArrayList<>();
	files.add('a');
	files.add('b');
	files.add('c');
	files.add('d');
	files.add('e');
	files.add('f');
	files.add('g');
	files.add('h');
	return files;
    }


    /**
     * This method loads a position into the board by using a position object.
     * @param position The position that wants to be loaded.
     */
    public void loadPosition(Position position, Board board) {
	StringBuilder fen = new StringBuilder(position.getFen());

	clearBoard(board);
	loadPiecesToBoard(board, fen);
	loadActivePlayer(board, fen);
	loadCastlingRights(board, fen);
	loadEnPassantSquare(board, fen);
	loadFiftyMoveCounter(board, fen);
    }

    private void clearBoard(Board board) {
        int boardEndIndex = Board.getBoardSize();
	for (int rank = 0; rank < boardEndIndex; rank++) {
	    for (int file = 0; file < boardEndIndex; file++) {
	        Square square = board.getSquare(file, rank);
	        if (square.isOccupied()) {
		    board.removePiece(square);
		}
	    }
	}
    }
    /**
     * This is a help method for the load position method that loads the pieces to the board of the given stringbuilder fen.
     * It works by iterating over the char until it gets to a space. If the character is space than it has loaded
     * all of the pieces into the board.
     * @param fen A stringbuilder of a given fen string.
     */
    private void loadPiecesToBoard(Board board, StringBuilder fen) {
	int file = 0;
	int rank = 0;
	String pieceCharacters = "PpNnBbRrQqKk";

	while(true) {
	    char currentChar = getNextChar(fen);
	    if (currentChar == '/') {
		file = 0;
		rank  += 1;
	    } else if (pieceCharacters.indexOf(currentChar) != -1) {
	        Piece piece = getFenPiece(currentChar);
	        Square square = board.getSquare(file, rank);
		board.putNewPiece(piece, square);

		file += 1;
	    } else if (currentChar == ' ') {
		//stop adding more characters
		break;
	    } else {
		//hop the amount of squares because they are supposed to be empty
		file += Character.getNumericValue(currentChar);
	    }
	}
    }

    /**
     * This method loads the isWhiteTurn flag by looking if the given fen char was a w or b.
     * @param fen The fen string to load from.
     */
    private void loadActivePlayer(Board board, StringBuilder fen) {
	char currentChar = getNextChar(fen);
	if (currentChar == 'w') {
	    board.setIsWhiteTurn(true);
	}
	else {
	    board.setIsWhiteTurn(false);
	}
	//removing the space after the w
	fen.deleteCharAt(0);
    }

    /**
     * This method load the castling rights flag by looking at the given fen char. This is done by first
     * setting all of the flags to false and afterwards setting the ones that should to true.
     * @param fen The fen string to load from.
     */
    private void loadCastlingRights(Board board, StringBuilder fen) {
	//Set all the castlingrights to false.
	for (CastlingRight castlingRight : CastlingRight.values()) {
	    board.setCastlingRight(castlingRight, false);
	}

	while(true) {
	    char currentChar = getNextChar(fen);

	    if (currentChar == '-') {
		fen.deleteCharAt(0);
		break;
	    } else if (currentChar == ' ') {
		break;
	    } else {
		CastlingRight castlingRight;
		switch (currentChar) {
		    case 'K':
			castlingRight = CastlingRight.WHITE_SHORT;
			break;
		    case 'Q':
			castlingRight = CastlingRight.WHITE_LONG;
			break;
		    case 'k':
			castlingRight = CastlingRight.BLACK_SHORT;
			break;
		    default:
			castlingRight = CastlingRight.BLACK_LONG;
			break;
		}
		board.setCastlingRight(castlingRight, true);
	    }
	}
    }

    /**
     * This method load the en passant square from a given fen.
     * @param fen The fen in which to load from.
     */
    private void loadEnPassantSquare(Board board, StringBuilder fen) {
	char currentChar = getNextChar(fen);
	if (currentChar == '-') {
	    board.resetEnPassantSquare();
	}
	else {
	    String moveCheck = String.valueOf(currentChar) + getNextChar(fen);
	    board.setEnPassantSquare(getSquareFromNotation(moveCheck, board));
	}
    }

    /**
     * This method loads the fifty move counter from a given fen.
     * @param board The board in which to load the counter
     * @param fen The fen to load.
     */
    private void loadFiftyMoveCounter(Board board, StringBuilder fen){
	fen.deleteCharAt(0);
        char nextChar = fen.charAt(1);
        String number;
        if (nextChar != ' ') {
	    final int getTwoNumbers = 2;
	    number = fen.substring(0, getTwoNumbers);
            fen.deleteCharAt(0);
            fen.deleteCharAt(1);
	} else {
	    final int getOneNumber = 1;
	    number = fen.substring(0, getOneNumber);
            fen.deleteCharAt(0);
	}
	board.setFiftyMoveCounter(Integer.valueOf(number));
    }

    /**
     * This is a method that returns the next char of a given stringbuilder objekt.
     * @param fen the fen
     * @return the next character in a fen.
     */
    private char getNextChar(StringBuilder fen) {
        char nextChar = fen.charAt(0);
        fen.deleteCharAt(0);
        return nextChar;
    }
}
