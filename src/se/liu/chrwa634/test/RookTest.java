package se.liu.chrwa634.test;

import se.liu.chrwa634.gui.GameFrame;

/**
 * Only used for testing the rook piece. Even though it can do castling with the king, it is already
 * tested in the kingunittest so this test just puts two rooks on the baord. which could
 * be moved.
 */
public class RookTest
{
    public static void main(String[] args) {
	new GameFrame("7k/R2r4/8/8/8/8/R2r4/7K w - - 0 1");
    }
}
