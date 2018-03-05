/**
 * a game level.
 */
public class Level implements LevelInformation {
    private int paddleSpeed;
    private int paddleWidth;
    private Invaders invaders;
    private String background;
    private String name;

    /**
     *
     * @param levelName the levels name.
     * @param background the levels background.
     * @param pSpeed the paddle speed.
     * @param pWidth the paddles width.
     * @param invade the levels blocks.
     * constructor.
     */
    public Level(String levelName, String background, double pSpeed, double pWidth, Invaders invade) {
        this.paddleSpeed = (int) pSpeed;
        this.paddleWidth = (int) pWidth;
        this.invaders = invade;
        this.background = background;
        this.name = levelName;
    }

    @Override
    public Sprite getBackground() {
        return new ImageSprite("background_Images/level_background3.jpeg", 0, 0);
    }

    @Override
    public int paddleSpeed() {
        return this.paddleSpeed;
    }

    @Override
    public int numberOfBlocksToRemove() {
        return this.invaders.size();
    }

    @Override
    public int paddleWidth() {
        return this.paddleWidth;
    }

    @Override
    public Invaders blocks() {
        return this.invaders;
    }

    @Override
    public String levelName() {
        return this.name;
    }

    @Override
    public int numberOfBalls() {
        return 1;
    }
}
