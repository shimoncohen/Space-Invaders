import java.awt.Color;

/**
 * a bomb.
 */
public class Bomb extends Ball {
    private SpriteCollection spriteCollection;
    private GameEnvironment gameEnvironment;
    private Point explosion;
    private BombAnimation bombAnimation;

    /**
     * @param center the center Point of the ball.
     * @param r      the radius of the ball.
     * @param velocity the balls velocity.
     * @param collection a sprite collection.
     * @param environment a game environment.
     * @param animation the bombs animation.
     * constructor.
     */
    public Bomb(Point center, int r, Velocity velocity, SpriteCollection collection, GameEnvironment environment,
                BombAnimation animation) {
        super(center, r, Color.red, environment, velocity);
        this.spriteCollection = collection;
        this.gameEnvironment = environment;
        this.bombAnimation = animation;
    }

    @Override
    public void removeFromGame(GameLevel g) {
        super.removeFromGame(g);
    }

    /**
     *
     * @return returns the bombs animation.
     */
    public BombAnimation getBombAnimation() {
        return this.bombAnimation;
    }
}