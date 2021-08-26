package se.liu.chrwa634.pieces;

/**
 * This enum is used to represent a file in the chessboard. It is used for its index to get the correct square in the arrayList in the
 * Board object.
 */
public enum File
{
    A(0), B(1), C(2), D(3), E(4), F(5), G(6), H(7);

    private final int index;

    File(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
