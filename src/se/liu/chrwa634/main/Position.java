package se.liu.chrwa634.main;

/**
 * This class is a way of saving a specific position in the class.
 * The class is saving a position using the FEN chess notation as a string.
 * FEN is a chess notation used to save a specific position in chess. you can read
 * more about FEN here: https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
 */
public class Position
{
    private String fen;

    /**
     * A constructor which could be used if you want to directly send in a FEN string.
     * @param fen The fen string which would be saved.
     */
    public Position(String fen) {
        this.fen = fen;
    }

    /**
     * This method returns the FEN of the positon.
     * @return The FEN of the position.
     */
    public String getFen() {
        return fen;
    }

    /**
     * This method return the position notation of the position.
     * @return The position notation as a string.
     */
    public String getPositionNotation() {
        String[] splitString = fen.split(" ");
        return splitString[0];
    }
}
