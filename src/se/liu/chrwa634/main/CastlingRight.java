package se.liu.chrwa634.main;

/**
 * This enum represents a castling right move. For example WHITE_SHORT means the casling right for the white pieces on the
 * kingside/short.
 */
public enum CastlingRight
{
    WHITE_LONG('Q'), WHITE_SHORT('K'),
    BLACK_LONG('q'), BLACK_SHORT('k');

    private final char fen;
    CastlingRight(char fen) {
    	this.fen = fen;
    }

    public char getFen() {
        return fen;
    }
}
