import biuoop.DrawSurface;
import java.awt.Color;

/**
 * an animation of a bomb exploding.
 */
public class BombAnimation implements Sprite {
    private boolean stop;
    private SpriteCollection collection;
    private GameEnvironment environment;
    private Ball[] explosion;
    private int count;

    /**
     *
     * @param spriteCollection a sprite collection.
     * @param gameEnvironment the game environment.
     * constructor.
     */
    public BombAnimation(SpriteCollection spriteCollection, GameEnvironment gameEnvironment) {
        this.collection = spriteCollection;
        this.environment = gameEnvironment;
        this.explosion = new Ball[4];
        this.stop = true;
    }

    /**
     *
     * @param p stops the bomb animation.
     */
    public void setStop(Point p) {
        this.stop = false;
        this.explosion[0] = new Ball(new Point(p.getX(), p.getY()), 7, Color.red,
                this.environment, new Velocity(0, 10));
        this.explosion[1] = new Ball(new Point(p.getX(), p.getY()), 7, Color.red,
                this.environment, new Velocity(0, -10));
        this.explosion[2] = new Ball(new Point(p.getX(), p.getY()), 7, Color.red,
                this.environment, new Velocity(10, 0));
        this.explosion[3] = new Ball(new Point(p.getX(), p.getY()), 7, Color.red,
                this.environment, new Velocity(-10, 0));
        this.collection.addSprite(this.explosion[0]);
        this.collection.addSprite(this.explosion[1]);
        this.collection.addSprite(this.explosion[2]);
        this.collection.addSprite(this.explosion[3]);
    }

    /**
     *
     * @return returns if the animation should stop.
     */
    public boolean shouldStop() {
        return this.stop;
    }

    @Override
    public void timePassed(double dt) {
        if (!this.stop) {
            this.count++;
            for (int i = 0; i < this.explosion.length; i++) {
                this.explosion[i].moveOneStep(dt);
            }
            if (this.count == 10) {
                this.collection.removeSprite(this.explosion[0]);
                this.collection.removeSprite(this.explosion[1]);
                this.collection.removeSprite(this.explosion[2]);
                this.collection.removeSprite(this.explosion[3]);
                this.stop = true;
                this.count = 0;
            }
        }
    }

    @Override
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }

    @Override
    public void drawOn(DrawSurface d) {
        if (!this.stop) {
            for (int i = 0; i < this.explosion.length; i++) {
                this.explosion[i].drawOn(d);
            }
        }
    }
}