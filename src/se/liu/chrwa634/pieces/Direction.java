package se.liu.chrwa634.pieces;

/**
 * This class represents a direction on the chess board. It is used to make the code more readably for the programmer.
 * For exampel instead of writing file - 1 it could instead be writing to file + Direction.LEFT.
 */
public enum Direction
{
    LEFT(-1), RIGHT(1), UP(-1), DOWN(1);

    private final int directionalValue;

    Direction(int value) {
        directionalValue = value;
    }

    public int getDirectionalValue() {
	return directionalValue;
    }
}
