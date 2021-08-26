package se.liu.chrwa634.pieces;

import se.liu.chrwa634.main.Board;
import se.liu.chrwa634.main.Move;
import se.liu.chrwa634.main.Square;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

/**
 * This class represent the pawn piece in a chess board. It hold methods to get the different
 * squares in which this pawn piece could move to.
 */
public class Pawn extends Piece
{
    public Pawn(Color color, URL imageURL) {
        super(color, PieceType.PAWN, imageURL);
    }

    @Override public void move(Move move, Board board, boolean isRealMove) {
        super.move(move, board, isRealMove);
        int direction = color.equals(Color.WHITE) ? -1 : 1;

        Square startingSquare = move.getStartingSquare(board);
        Square destinationSquare = move.getDestinationSquare(board);

        int startingRank = startingSquare.getRank();
        int destinationFile = destinationSquare.getFile();

        if (isEnPassantMove(move, board)) {
            // Remove the piece after the enpassant.
            Square square = board.getSquare(destinationFile, startingRank);

            board.removePiece(square);
        }
        else if (isDoubleMove(move, board)) {
            Square enPassantSquare = board.getSquare(destinationFile, startingRank + direction);
            board.setEnPassantSquare(enPassantSquare);
        }

        if (isRealMove) {
            if (canPromote()) {
                promotePawn(board);
            }

            if (isDoubleMove(move, board)) {
                Square enPassantSquare = board.getSquare(startingSquare.getFile(), startingSquare.getRank() + direction);
                board.setEnPassantSquare(enPassantSquare);
            }

            board.resetPositionCounter();
            board.resetFiftyMoveCounter();
        }
    }

    /**
     * This method checks if a pawn could promote.
     * @return true if the pawn could promote.
     */
    private boolean canPromote() {
        int rank = square.getRank();
        int rankOne = Rank.ONE.getIndex();
        int rankEight = Rank.EIGHT.getIndex();

        return rank == rankOne || rank == rankEight;
    }

    /**
     * This method returns all the valid move of the pawn.
     * @param board The baord where the pawn is currently on.
     * @return A list of squares in which the pawn could move to.
     */
    @Override public List<Move> getValidMoves(Board board, boolean removeCheckMove) {
        int file = square.getFile();
        int rank = square.getRank();
        Direction direction = color.equals(Color.WHITE) ? Direction.UP : Direction.DOWN;

        List<Move> validMoves = getForwardMoves(file, rank, direction, board);
        validMoves.addAll(getDiagonalCapture(file, rank, direction, board));

        if (removeCheckMove) {
            validMoves = board.removeInvalidMove(validMoves);
        }

        return validMoves;
    }

    /**
     * This method ask the user to choose want they want to promote their pawn to and afterwards put that desired
     * piece into the board on the square that the pawn was.
     * @param board The board in which the promotion will be perfomed.
     */
    public void promotePawn(Board board) {
        Object[] pieces = {"Queen", "Rook", "Bishop", "Knight"};
        int i = JOptionPane.showOptionDialog(
                null, "What do you want to promote to?", "Promotion",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, pieces,
                "Queen");

        PieceFactory pf = new PieceFactory();
        PieceType[] pieceTypes = {PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT};

        Piece piece = pf.getPiece(pieceTypes[i], color);
        board.removePiece(square);
        board.putNewPiece(piece, square);
    }

    /**
     * This method checks if the given move is an en passant pawn move.
     * @param starting The starting square.
     * @param destination The destination square.
     * @return A boolean value that determines if the given move is an en passant move.
     */
    private boolean isEnPassantMove(Move move, Board board) {
        return !move.isCaptureMove() && isDiagonalMove(move, board);
    }

    /**
     * This method checks if move that was done is a diogonal move. This is done by checking if the difference
     * between the start and destination squares, if both the file and rank has the difference higher or equals to one
     * that it is a diagona move.
     * @param start The starting square.
     * @param destination The destination square.
     * @return A boolean that determines if the move is a diagonal move.
     */
    private boolean isDiagonalMove(Move move, Board board) {
        Square startingSquare = move.getStartingSquare(board);
        Square destinationSquare = move.getDestinationSquare(board);

        return abs(startingSquare.getFile() - destinationSquare.getFile()) >= 1 &&
               abs(startingSquare.getRank() - destinationSquare.getRank()) >= 1;
    }

    /**
     * This method checks if the move is a double move
     * @return true if the move is a double move.
     */
    private boolean isDoubleMove(Move move, Board board) {
        Square startingSquare = move.getStartingSquare(board);
        Square destinationSquare = move.getDestinationSquare(board);

        return abs(destinationSquare.getRank() - startingSquare.getRank()) > 1;
    }

    /**
     * This method check all the possible forward moves of the pawn. This is a help method for
     * the getValidMoves method.
     * @param file The file in which the pawn is located.
     * @param rank The rank in which the pawn is located.
     * @param verticalDirection The direction to check.
     * @param board The board in which the pawn is currently on.
     * @return A list of squares in which the pawn could move to.
     */
    private List<Move> getForwardMoves(int file, int rank, Direction verticalDirection, Board board) {

        List<Move> forwardValidMoves = new ArrayList<>();
        //square right infront
        int checkRank1 = rank + verticalDirection.getDirectionalValue();
        //square two moves infront
        int checkRank2 = rank + (verticalDirection.getDirectionalValue() * 2);

        if (isSquareValidMove(file, checkRank1, board, false)) {
            Square destinationSquare1 = board.getSquare(file, checkRank1);
            forwardValidMoves.add(new Move(square, destinationSquare1));
            if (isDoubleMovePossible() &&
                isSquareValidMove(file, checkRank2, board, false)) {
                Square destinationSquare2 = board.getSquare(file, checkRank2);
                forwardValidMoves.add(new Move(square, destinationSquare2));
            }
        }
        return forwardValidMoves;
    }

    /**
     * This methdo checks if the pawn could do a double move.
     * @return true if the pawn could perform a double move.
     */
    private boolean isDoubleMovePossible() {
        int rank = square.getRank();
        int secondRank = Rank.TWO.getIndex();
        int seventhRank = Rank.SEVEN.getIndex();

        return rank == secondRank || rank == seventhRank;
    }

    /**
     * This method check all the possible diagonal captures. This is a help method for
     * the getValidMoves method.
     * @param file The file in which the pawn is located.
     * @param rank The rank in which the pawn is located.
     * @param verticalDirection The direction to check.
     * @param board The board in which the pawn is currently on.
     * @return A list of squares in which the pawn could move to.
     */
    private List<Move> getDiagonalCapture(int file, int rank, Direction verticalDirection, Board board) {

        List<Move> validMoves = new ArrayList<>();
        List<Direction> directions = new ArrayList<>();
        Square enPassantSquare = board.getEnPassantSquare();
        directions.add(Direction.LEFT);
        directions.add(Direction.RIGHT);

        for (Direction horizontalDirection : directions) {

            int checkFile = file + horizontalDirection.getDirectionalValue();
            int checkRank = rank + verticalDirection.getDirectionalValue();

            if (board.isSquareInsideBoard(checkFile, checkRank)) {
                Square checkSquare = board.getSquare(checkFile, checkRank);
                if ((checkSquare.isOccupied() && isEnemyPiece(checkSquare)) ||
                    (board.doesEnPassantSquareExist() && enPassantSquare.equals(checkSquare))) {
                    validMoves.add(new Move(square, checkSquare));
                }
            }
        }
        return validMoves;
    }




}
