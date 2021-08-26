package se.liu.chrwa634.main;

/**
 * This enum represent the different gameOverType that has been added to the game.
 */
public enum GameOverType
{
    CHECKMATE("Checkmate"), THREEFOLD_REPETITION ("Threefold repetition"),
    STALEMATE("Stalemate"), FIFTY_MOVE_DRAW("Fifty-move rule"),
    INSUFFICIENT_PIECE("Insufficient piece"), NULL("");


    private final String loseTextComment;

    GameOverType(String loseTextComment) {
        this.loseTextComment = loseTextComment;
    }

    public String getLoseTextComment() {
        return loseTextComment;
    }
}
