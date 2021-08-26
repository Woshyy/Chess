package se.liu.chrwa634.test;

import se.liu.chrwa634.gui.GameFrame;

/**
 * This class loads the board with different positions to test different endgame scenarios. The class can test
 * stalemate, insufficient piece, checkmate, 50-move-rule.
 */
public class EndGamePositionTest
{
    // ======
    //	test removeInvalidMove : rnbqk1nr/pppp1ppp/8/4p3/1b1PP3/8/PPP2PPP/RNBQKBNR w KQkq - 1 3
    //	test stalemate/checkmate/insufficient : k7/8/K7/8/8/8/8/1Q6 w - - 0 1
    //	Test harder to spot checkmate : k1r4Q/8/K1b5/8/8/8/8/1Q5B w - - 0 1
    //  Test insufficientPiece : 7K/P5n1/1n6/8/8/1N6/p5N1/7k w - - 0 1
    //  Test insufficientPiece same color bishop : 6rk/PP6/B7/1B6/B7/1B6/B7/K7 w - - 0 1
    //  Test 50-move rule : 7k/p4n2/8/8/8/1b6/P4N2/7K w - - 46 1
    //  Test threefoldrepetition : 7k/p4n2/1b6/8/8/1B6/P4N2/7K w - - 0 1
    // ======
    public static void main(String[] args) {
	new GameFrame("6rk/PP6/B7/1B6/B7/1B6/B7/K7 w - - 0 1");
    }
}
