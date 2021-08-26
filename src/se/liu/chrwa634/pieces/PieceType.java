package se.liu.chrwa634.pieces;

/**
 * This enum represents a specific type of piece. The enums also saves the piece fen character to be used in a method
 * to retrieve its fen character.
 */
public enum PieceType
{
    PAWN('p'), KNIGHT('n'), QUEEN('q'), KING('k'), BISHOP('b'), ROOK('r');

    private final char fen;
    PieceType(char fen) {
        this.fen = fen;
    }

    public char getFen() {
        return fen;
    }

    /**
     * This method retrieves the right pieceType according to the given character.
     * @param character The character annotation of a given piece type.
     * @return A PieceType according to the given character annotation.
     */
    public static PieceType getPieceTypeFromChar(char character) {
        char characterUpper = Character.toUpperCase(character);

        switch (characterUpper) {
            case 'P':
                return PieceType.PAWN;
            case 'N':
                return PieceType.KNIGHT;
            case 'B':
                return PieceType.BISHOP;
            case 'R':
                return PieceType.ROOK;
            case 'Q':
                return PieceType.QUEEN;
            default:
                return PieceType.KING;
        }
    }
}
