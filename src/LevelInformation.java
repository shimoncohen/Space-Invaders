/**
 * all of the levels information.
 */
public interface LevelInformation {

    /**
     *
     * @return returns the levels number of balls.
     */
    int numberOfBalls();

    /**
     *
     * @return returns the levels paddle speed.
     */
    int paddleSpeed();

    /**
     *
     * @return returns the levels paddles width.
     */
    int paddleWidth();

    /**
     *
     * @return returns the levels name.
     */
    String levelName();

    /**
     *
     * @return Returns a sprite with the background of the level.
     */
    Sprite getBackground();

    /**
     *
     * @return returns a list of the blocks that make up this level.
     */
    Invaders blocks();

    /**
     *
     * @return returns the number of blocks in the level that should be removed to pass the level.
     */
    int numberOfBlocksToRemove();
}