package se.liu.chrwa634.main;

import java.util.HashMap;
import java.util.Map;

/**
 * This class has counters which counts different aspects on the baord. One counter is for counting
 * The amount of times a position has occured on the board. Another counter counts the number of moves
 * in which there has not been any capture of movements of pawns. This counters are useful for determining
 * if a gameover criteria has been reached.
 */
public class BoardCounter
{
    private Map<String, Integer> positionCounter;
    private int fiftyMoveCounter;

    public BoardCounter() {
        positionCounter = new HashMap<>();
        setFiftyMoveCounter(0);
    }

    /**
     * This method set the fiftyMoveCounter to the given value.
     * @param fiftyMoveCounter The value which will be given to the counter
     */
    public void setFiftyMoveCounter(int fiftyMoveCounter) {
	this.fiftyMoveCounter = fiftyMoveCounter;
    }

    /**
     * This method increases the counter by one.
     */
    public void increaseFiftyMoveCounter() {
        fiftyMoveCounter++;
    }

    /**
     * This method returns the value of the fifty-move counter.
     * @return The value of the counter.
     */
    public int getFiftyMoveCounter() {
        return fiftyMoveCounter;
    }

    public void resetFiftyMoveCounter() {
        setFiftyMoveCounter(0);
    }

    /**
     * This method adds a position into the position counter.
     * @param position The position that will be added.
     */
    public void addToPositionCounter(Position position) {
	String fenPositionNotation = position.getPositionNotation();
	if (positionCounter.containsKey(fenPositionNotation)) {
	    int oldValue = positionCounter.get(fenPositionNotation);
	    oldValue ++;
	    positionCounter.replace(fenPositionNotation, oldValue);
	}
	else {
	    positionCounter.put(fenPositionNotation, 1);
	}
    }

    /**
     * This method reset the position counter. 
     */
    public void resetPositionCounter() {
        positionCounter.clear();
    }

    /**
     * This method returns the positionCounter.
     * @return The position counter. 
     */
    public Map<String, Integer> getPositionCounter() {
	return positionCounter;
    }
}
