import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A paddle the hit the balls with.
 */
public class Paddle implements Sprite, Collidable, HitNotifier {
    private KeyboardSensor keyboard;
    private Rectangle rec;
    private Block block = new BorderBlock(this.rec, Color.lightGray);
    private int speed;
    private Color color;
    private List<HitListener> hitListeners;
    private Long lastShootingTime;
    private boolean sideShots;
    private boolean wingShots;
    private BombAnimation bomb;
    private Image paddleImage;

    /**
     *
     * @param rec1 the paddles rectangle.
     * @param sensor a KeyBoard sensor.
     * @param speed1 the paddles speed.
     * @param c the paddles color.
     * @param bombAnimation the paddles bomb animation.
     * constructor.
     */
    public Paddle(Rectangle rec1, KeyboardSensor sensor, int speed1, Color c, BombAnimation bombAnimation) {
        this.rec = rec1;
        this.keyboard = sensor;
        this.speed = speed1;
        this.hitListeners = new ArrayList<>();
        this.color = c;
        this.lastShootingTime = System.currentTimeMillis();
        this.bomb = bombAnimation;
        try {
            InputStream inputStream = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream("Spaceship_Images/spaceship.png");
            this.paddleImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    /**
     * @param dt amount of seconds past since last call.
     * moves the paddle left.
     */
    public void moveLeft(double dt) {
        if (this.rec.getUpperLeft().getX() >= 0) {
            this.rec.setPosition(new Point(this.rec.getUpperLeft().getX() - this.speed * dt,
                    this.rec.getUpperLeft().getY()));
        }
    }

    /**
     * @param dt amount of seconds past since last call.
     * moves the paddle right.
     */
    public void moveRight(double dt) {
        if (this.rec.getBottomRight().getX() <= Ass7Game.SCREEN_WIDTH) {
            this.rec.setPosition(new Point(this.rec.getUpperLeft().getX() + this.speed * dt,
                    this.rec.getUpperLeft().getY()));
        }
    }

    /**
     *
     * @param spriteCollection a sprite collection.
     * @param c the shots color.
     * @param gameEnvironment the game environment.
     * fire a shot.
     */
    public void shoot(SpriteCollection spriteCollection, Color c, GameEnvironment gameEnvironment) {
        Long millis = System.currentTimeMillis();
        if (millis - this.lastShootingTime > 350) {
            if (!this.sideShots && !this.wingShots) {
                Point center = new Point(this.getCenter().getX(), this.getCenter().getY() - 20);
                Ball shotLeft = new Ball(center, 3, c, gameEnvironment, new Velocity(0, -7 * 60));
                spriteCollection.addSprite(shotLeft);
            } else if (this.sideShots) {
                Ball shotLeft = new Ball(new Point(this.rec.getUpperLeft().getX() + 3,
                        this.rec.getUpperLeft().getY() - 5),
                        3, c, gameEnvironment, new Velocity(0, -7 * 60));
                Ball shotRight = new Ball(new Point(this.rec.getUpperLeft().getX() - 3 + this.rec.getWidth(),
                        this.rec.getUpperLeft().getY() - 5), 3, c, gameEnvironment, new Velocity(0, -7 * 60));
                spriteCollection.addSprite(shotLeft);
                spriteCollection.addSprite(shotRight);
            } else {
                Ball shotLeft = new Ball(new Point(this.rec.getUpperLeft().getX() - 5,
                        this.rec.getUpperLeft().getY() - 5),
                        3, c, gameEnvironment, new Velocity(-45, -7 * 60));
                Ball shotRight = new Ball(new Point(this.rec.getUpperLeft().getX() + 5 + this.rec.getWidth(),
                        this.rec.getUpperLeft().getY() - 5), 3, c, gameEnvironment, new Velocity(45, -7 * 60));
                spriteCollection.addSprite(shotLeft);
                spriteCollection.addSprite(shotRight);
            }
            this.lastShootingTime = millis;
        }
    }

    /**
     *
     * @param spriteCollection a sprite collection.
     * @param gameEnvironment the game environment.
     * @param bombAnimation the bombs animation.
     * launch a bomb.
     */
    public void bombAttack(SpriteCollection spriteCollection, GameEnvironment gameEnvironment,
                           BombAnimation bombAnimation) {
        Point center = new Point(this.getCenter().getX(), this.getCenter().getY() - 20);
        Bomb bomb1 = new Bomb(center, 10, new Velocity(0, -5), spriteCollection, gameEnvironment, bombAnimation);
        spriteCollection.addSprite(bomb1);
    }

    @Override
    /**
     * @param dt amount of seconds past since last call.
     * moves paddle according to keys pressed.
     */
    public void timePassed(double dt) {
        if (this.keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            this.moveRight(dt);
        } else if (this.keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            this.moveLeft(dt);
        }
        if (this.keyboard.isPressed("s")) {
            this.sideShots = true;
            this.wingShots = false;
        }
        if (this.keyboard.isPressed("m")) {
            this.sideShots = false;
            this.wingShots = false;
        }
        if (this.keyboard.isPressed("w")) {
            this.wingShots = true;
            this.sideShots = false;
        }
    }

    @Override
    /**
     *
     * @param d a DrawSurface object.
     * draws the paddle on the screen.
     */
    public void drawOn(DrawSurface d) {
        d.setColor(this.color);
        d.drawImage((int) this.rec.getUpperLeft().getX(), (int) this.rec.getUpperLeft().getY(), this.paddleImage);
    }

    @Override
    /**
     *
     * @return returns the paddles rectangle.
     * gets the paddles rectangle.
     */
    public Rectangle getCollisionRectangle() {
        return this.rec;
    }

    @Override
    /**
     *
     * @param collisionPoint a Point representing the collision location with the paddle.
     * @param currentVelocity the Velocity of the colliding object.
     * @return returns a new Velocity object according to where the paddle was hit.
     * changes the velocity of the ball that hit the block.
     */
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        this.notifyHit(hitter);
        return currentVelocity;
    }

    @Override
    /**
     *
     * @param g a game object.
     * Add this paddle to the game.
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
        g.addCollidable(this);
    }

    /**
     *
     * @param game the game to remove from.
     * removes the paddle from the cretain game.
     */
    public void removeFromGame(GameLevel game) {
        game.removeSprite(this);
        game.removeCollidable(this);
    }

    @Override
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }

    @Override
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    /**
     *
     * @param hitter the hitting ball.
     * notifys all listeners that a hit occurred.
     */
    private void notifyHit(Ball hitter) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listeners = new ArrayList<>(this.hitListeners);
        // Notify all listeners about a hit event:
        for (HitListener hl : listeners) {
            hl.hitEvent(this.block, hitter);
        }
    }

    /**
     *
     * @return returns the center point of the top of the paddle.
     */
    public Point getCenter() {
        double x = this.rec.getUpperLeft().getX() + this.rec.getWidth();
        double y = this.rec.getUpperLeft().getY();
        Line l = new Line(this.rec.getUpperLeft(), new Point(x, y));
        return l.middle();
    }
}