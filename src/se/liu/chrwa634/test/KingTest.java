package se.liu.chrwa634.test;

import se.liu.chrwa634.gui.GameFrame;

/**
 * Used for testing the king. This test different aspects of the king like castling and moving into
 * a check. There are also different posistion to test.
 */
public class KingTest
{
    public static void main(String[] args) {
        /*
        Test1 : "r1N1kn1r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w - - 0 1"
        Test2 : "r3k3/8/8/8/8/8/8/R3K2R w Kkq - 0 1"
        Test3 : "r3k2r/8/1N6/2b3B1/8/6n1/8/R3K2R w KQkq - 0 1"
         */
	new GameFrame("r3k2r/8/1N6/2b3B1/8/6n1/8/R3K2R w KQkq - 0 1");
    }
}
