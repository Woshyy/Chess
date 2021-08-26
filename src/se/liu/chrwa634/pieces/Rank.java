package se.liu.chrwa634.pieces;

/**
 * This enum is used to represent a rank in the chessboard. It is used for its index to get the correct square in the arrayList in the
 * Board object.
 */
public enum Rank
{
    ONE(7), TWO(6), THREE(5), FOUR(4), FIVE(3), SIX(2), SEVEN(1), EIGHT(0);

    private final int index;

    Rank(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
