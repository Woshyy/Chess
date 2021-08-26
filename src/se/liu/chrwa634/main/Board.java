package se.liu.chrwa634.main;

import se.liu.chrwa634.pieces.File;
import se.liu.chrwa634.pieces.Piece;
import se.liu.chrwa634.pieces.Rank;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This is the class that represent the chess board. It holds all the different squares. It also has the objective to be the link
 * so that different object coudl work with each other.
 */
public class Board
{
    private static final int BOARD_SIZE = 8;

    private Square[][] boardSquares;
    private BoardCounter boardCounter;

    private BoardScanner detector;
    private FenConverter fenConverter;
    private GameOverType gameOverType;
    private List<List<Piece>> pieces;

    /**
     * This is the contrustor. It creates an array of empty squares. Afterwards using
     * the given FEN string, the method places pieces on the newly created squares.
     * @param startPosition A FEN string in which the position of the board will start.
     */
    public Board() {
        boardSquares = new Square[BOARD_SIZE][BOARD_SIZE];
        detector = new BoardScanner(this);
        fenConverter = new FenConverter();
        boardCounter = new BoardCounter();
        pieces = new ArrayList<>();
        pieces.add(new ArrayList<>());
        pieces.add(new ArrayList<>());
        gameOverType = GameOverType.NULL;

        for (int rank = 0; rank < BOARD_SIZE; rank++) {
            for (int file = 0; file < BOARD_SIZE; file ++) {
                boolean isBlackSquare = (rank + file) % 2 != 0;
                Color color = isBlackSquare ? Color.BLACK : Color.WHITE;
                boardSquares[rank][file] = new Square(file, rank, color, this);
            }
        }
    }


    /**
     * Return the size of a normal chessboard.
     * @return An integer representing the size of the board.
     */
    public static int getBoardSize() {
        return BOARD_SIZE;
    }

    /**
     * This method resets the board to the starting position of a chess game.
     */
    public void resetBoard() {
        gameOverType = GameOverType.NULL;
        clearPieces();
        resetPositionCounter();
        resetEnPassantSquare();
        resetFiftyMoveCounter();
        Position position = new Position("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        loadPosition(position);
    }

    /**
     * This method retrieves the gameOvertype
     * @return the gameOverType
     */
    public GameOverType getGameOverType() {
        return gameOverType;
    }

    /**
     * This method set the gameOverType to the give value.
     * @param gameOverType The value that the gameOverType will be set to.
     */
    public void setGameOverType(GameOverType gameOverType) {
        this.gameOverType = gameOverType;
    }

    /**
     * This method checks if the current game is over.
     * @return
     */
    public boolean isGameOver() {
        if (gameOverType == GameOverType.NULL) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * This method retrieves the value of isWhiteTurn.
     * @return The value of isWhiteTurn.
     */
    public boolean getIsWhiteTurn() {
        return detector.getIsWhiteTurn();
    }

    /**
     * This method sets the isWhiteTurn to the give value.
     * @param value The value that isWhiteTurn is going to be.
     */
    public void setIsWhiteTurn(boolean value) {
        detector.setIsWhiteTurn(value);
    }

    /**
     * This method change the isWhiteTurn boolean value to the opposite of its current value.
     */
    public void switchTurns() {
        setIsWhiteTurn(!getIsWhiteTurn());
    }



    /**
     * This method retrieves the en passant square. An en passant square is a square that a pawn could do
     * the move en passant.
     * @return The current en passant square
     */
    public Square getEnPassantSquare() {
        return detector.getEnPassantSquare();
    }

    /**
     * This method set the en passant square to the given square.
     * @param enPassantSquare The square that the en passant square will be.
     */
    public void setEnPassantSquare(Square enPassantSquare) {
        detector.setEnPassantSquare(enPassantSquare);
    }

    public boolean doesEnPassantSquareExist() {
        Square enPassantSquare = getEnPassantSquare();

        if (enPassantSquare == null) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * This method reset the en passant square to null.
     */
    public void resetEnPassantSquare() {
        detector.resetEnPassantSquare();
    }

    /**
     * This method places a piece to the specified square. If the squares is already occupied than the new piece
     * will take its place. The metods will remove the captured piece from the pieces list.
     * @param square The square where the piece will be placed.
     * @param piece The piece which will be placed.
     */
    public void placePiece(Piece piece, Square square) {
        if (square.isOccupied()) {
            removePiece(square);
        }
        square.placePiece(piece);
    }

    /**
     * This method places a new piece to the specified square. It adds the new piece to the player pieces list to.
     * @param piece The new piece that wants to be placed on the board.
     * @param square The square that the piece is going to be placed.
     */
    public void putNewPiece(Piece piece, Square square) {
        Color pieceColor = piece.getColor();
        List<Piece> playerPieces = getPlayerPieces(pieceColor);
        playerPieces.add(piece);
        placePiece(piece, square);
    }

    /**
     * This method represents lifting the piece from a square but not removing it from the board. This method
     * just remove the piece from the square it is standing. this method normaly is used when you want to move a piece
     * from a square to antoher.
     * @param square The square which has the piece that is going to be lifted of the square.
     */
    public void liftPiece(Square square) {
        square.removePiece();
    }

    /**
     * This method removes a piece from the square. This is done by calling the Square removePiece method. The method
     * also removes the piece from the pieces list.
     * @param square The square which will get its piece removed.
     */
    public void removePiece(Square square) {
        Piece piece = square.getPiece();
        Color pieceColor = piece.getColor();
        List<Piece> playerPieces = getPlayerPieces(pieceColor);
        playerPieces.remove(piece);
        square.removePiece();
        //All the previous positions cannot happen again so reset.
        resetPositionCounter();
    }


    /**
     * This method checks if a given a square is occupied.
     * @param file The squares file coordinate.
     * @param rank The squares rank coordinate.
     * @return True if the squares is occupied otherwise false.
     */
    public boolean isOccupied(int file, int rank) {
        return getSquare(file, rank).isOccupied();
    }

    /**
     * This method returns the square at a specific file and rank.
     * @param file The file of the square.
     * @param rank The rank of the square.
     * @return The specified square.
     */
    public Square getSquare(int file, int rank) {
        return boardSquares[rank][file];
    }

    public Square getSquare(File file, Rank rank) {
        return getSquare(file.getIndex(), rank.getIndex());
    }

    public Square getSquare(File file, int rank) {
        return getSquare(file.getIndex(), rank);
    }

    /**
     * This method creates a new position object that will be saved into a list that saves all the
     * previous position.
     */
    private void addPreviousPosition() {
        detector.addPreviousPosition();
    }

    /**
     * This method returns the previous position of the board.
     * @return The last position on the board.
     */
    public Position getPreviousPosition() {
        return detector.getPreviousPosition();
    }

    /**
     * This method add the position to the counter that counts how many time the position has occur
     * in the game.
     * @param position The position which will be added.
     */
    public void addToPositionCounter(Position position) {
        boardCounter.addToPositionCounter(position);
    }

    /**
     * This method resets the position counter by removing all of the previous recorded moves.
     */
    public void resetPositionCounter() {
        boardCounter.resetPositionCounter();
    }

    /**
     * This method returns a map with FEN strings as a key and its value as an interger depending
     * by the amount of time that the position has occured.
     * @return  A map with Strings as key and Integer as value.
     */
    public Map<String, Integer> getPositionCounter() {
        return boardCounter.getPositionCounter();
    }

    /**
     * This method sets the Fifty move counter to a given value.
     * @param value The value in which the counter will be set to.
     */
    public void setFiftyMoveCounter(int value) {
        boardCounter.setFiftyMoveCounter(value);
    }

    /**
     * This method counts up the fifty move counter.
     */
    public void increaseFiftyMoveCounter() {
        boardCounter.increaseFiftyMoveCounter();
    }

    /**
     * This method returns the value of the counter.
     * @return An integer representing the counter value.
     */
    public int getFiftyMoveCounter() {
        return boardCounter.getFiftyMoveCounter();
    }

    /**
     * This method sets the fifty-move counter back to zero.
     */
    public void resetFiftyMoveCounter() {
        boardCounter.resetFiftyMoveCounter();
    }

    /**
     * This method clear the selecten list and unselect all the squares in that list. This is done
     * by going through the list and iterating over all the square. With the square deselect function
     * we could easily deselect it. And at the end we just the the squareSelected list.
     *
     */
    public void clearSelected() {
        detector.clearSelected();
    }

    /**
     * This method select all the squares inside the given list and add them tho the squaresSelected list.
     * @param selectedSquares The list that will be added to squareSelected list.
     */
    public void addSelectedSquares(List<Move> selectedSquares) {
        detector.addSelectedSquare(selectedSquares);
    }

    /**
     * This method checks if a given square is in the squareSelected list.
     * @param square The square that will be checked.
     * @return True if the square is in the square selectedList.
     */
    public boolean isSquareSelected(Square square) {
        return detector.isSquareSelected(square);
    }

    /**
     * This method draws the board by going through all the square and calling its draw method. And
     * if the square is occupied than draw its piece.
     * @param graphics The ggraphics which will be drawn on.
     */
    public void draw(Graphics graphics){
        for (Square[] rank : boardSquares) {
            for (Square square : rank) {
                square.draw(graphics);
                if (square.isOccupied()) {
                    square.getPiece().draw(graphics);
                }
            }
        }
    }

    /**
     * This is method will perform the given move object on the board. If isRealMove is set to true than it would check
     * for game over scenarios.
     * @param starting The starting square.
     * @param destination The destination square.
     */
    public void movePiece(Move move, boolean isRealMove) {
        Square startingSquare = move.getStartingSquare(this);
        int startingSquareFile = startingSquare.getFile();
        int startingSquareRank = startingSquare.getRank();

        Piece piece = getSquare(startingSquareFile, startingSquareRank).getPiece();
        piece.move(move, this, isRealMove);
    }

    /**
     * This method select a square on the board. Select means that if that square has a piece than we should draw all
     * of its possible moves on the board. After the user selects a piece and next press a square that the piece could
     * move to then the piece would move to that square. If it was not one of those square that we remove all of those
     * possible move and wait for the user to select another piece.
     * @param rank The rank of the square which was selected.
     * @param file The file of the square which was selected.
     */
    public void select(int rank, int file) {
        //If the square that was seleced was the square that the user previously selected that do not do anything.
        Square square = getSquare(file, rank);
        if (!square.equals(getSelectedSquare())) {
            //If the square is not a square that is currently being selected than we deselect
            if (!isSquareSelected(square)) {
                clearSelected();
                removeSelectedSquare();
                //If the square is occupied that we check all the possible squares that the piece could move to and
                //and add them to the squareSelectedList.
                if (square.isOccupied()) {
                    Color pieceColor = square.getPiece().getColor();
                    if ((getIsWhiteTurn() && pieceColor.equals(Color.WHITE)) || (!getIsWhiteTurn() && pieceColor.equals(Color.BLACK))) {
                        addSelectedSquares(square.getPiece().getValidMoves(this, true));
                        setSelectedSquare(square);
                    }
                }
            } else {
                //If it is one of the selected square than perform the move and do all teh updtaes taht needs to be done
                //after a move is performed.
                Square selectedSquare = getSelectedSquare();
                Move move = new Move(selectedSquare, square);
                movePiece(move, true);
                increaseFiftyMoveCounter();
                addPreviousPosition();
                addToPositionCounter(getPreviousPosition());
                setGameOver();
                clearSelected();
                switchTurns();
            }
        }
    }

    /**
     * This method gets the selected Square.
     * @return The selected square
     */
    public Square getSelectedSquare() {
        return detector.getSelectedSquare();
    }

    /**
     * This method set the selectedSquare field to the given value.
     * @param square The square that will be the selectedSquare.
     */
    public void setSelectedSquare(Square square) {
        detector.setSelectedSquare(square);
    }

    /**
     * This method rremove the selected square by setting the selected
     */
    public void removeSelectedSquare() {
        setSelectedSquare(null);
    }

    /**
     * This method retrieves the castling rights of the given key.
     * @param castlingRight A key which represent a castling right.
     * @return A boolean value that represents if a specific castling right could be done.
     */
    public boolean getCastlingRight(CastlingRight castlingRight) {
        return detector.getCastlingRight(castlingRight);
    }

    /**
     * This method sets a castling right of a give key to a given value.
     * @param castlingRight The key which repsent a specific right to castle.
     * @param change The boolean value that will be placed.
     */
    public void setCastlingRight(CastlingRight castlingRight , boolean value) {
        detector.setCastlingRight(castlingRight, value);
    }

    /**
     * This method removes the invalid moves of a given list of moves.
     * @param movesToCheck  The list of moves to check.
     * @return A list of moves which are valid.
     */
    public List<Move> removeInvalidMove(List<Move> movesToCheck) {
        List<Move> validMoves = detector.removeInvalidMoves(movesToCheck);
        return validMoves;
    }

    /**
     * This method converts the current board to a fen string.
     * @return The fen string of the board.
     */
    public String convertBoardToFen() {
        return fenConverter.convertBoardToFen(this);
    }

    /**
     * This method load a given position on the board.
     * @param position The position that will be loaded on the board.
     */
    public void loadPosition(Position position) {
        fenConverter.loadPosition(position, this);
        addToPositionCounter(position);
    }

    public void setGameOver() {
        GameOverType gameOverType = detector.getGameOver(this);
        setGameOverType(gameOverType);
    }

    /**
     * This method checks if a given square coordinate is inside the board.
     * @param file The file of the square.
     * @param rank The rank of the square.
     * @return true if the given square on the coordintane is inside the board.
     */
    public boolean isSquareInsideBoard(int file, int rank) {
        return detector.isSquareInsideBoard(file, rank);
    }

    /**
     * This method checks if a given square coordinate is attacked by the opposite color.
     * @param file The file of the square.
     * @param rank The rank of the square.
     * @param color The opposite color.
     * @return True if the square is attacked by the opposite color.
     */
    public boolean isSquareAttacked(int file, int rank, Color color) {
        return detector.isSquareAttacked(file, rank, color, this);
    }

    /**
     * This method gets the a specific players pieces.
     * @param color The color of the player that want to be returned.
     * @return A list of player pieces.
     */
    public List<Piece> getPlayerPieces(Color color) {
        if (color.equals(Color.WHITE)) {
            return pieces.get(0);
        } else {
            return pieces.get(1);
        }
    }

    /**
     * This method remove of all the player pieces from the player list.
     */
    public void clearPieces() {
        for (List<Piece> playerPieces : pieces) {
            playerPieces.clear();
        }
    }
}

