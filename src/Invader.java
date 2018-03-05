import biuoop.DrawSurface;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

/**
 * A block consisting of a rectangle.
 */
public class Invader extends Block implements HitNotifier {
    private Invaders invadingGroup;
    private Rectangle rec;
    private Image image;
    private int hits;
    private List<HitListener> hitListeners;
    private Point invaderInMatrix;

    /**
     *
     * @param group the invaders group.
     * @param rec1 A rectangle object.
     * @param p invader position in invader matrix.
     * constructor.
     */
    public Invader(Invaders group, Rectangle rec1, Point p) {
        this.invadingGroup = group;
        this.rec = rec1;
        try {
            InputStream inputStream = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream("Invader_Images/invader2.png");
            this.image = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.getMessage();
        }
        this.hitListeners = new ArrayList<>();
        this.invaderInMatrix = p;
        this.hits = 1;
    }

    @Override
    public void removeFromGame(GameLevel game) {
        this.invadingGroup.removeInvader(this);
        game.removeCollidable(this);
        game.removeSprite(this);
    }

    @Override
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    @Override
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
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
            hl.hitEvent(this, hitter);
        }
    }

    /**
     *
     * @return returns the list of listeners.
     */
    public List<HitListener> getHitListeners() {
        return hitListeners;
    }

    @Override
    public int getHitPoints() {
        return this.hits;
    }

    @Override
    public Rectangle getRec() {
        return this.rec;
    }

    @Override
    public void drawOn(DrawSurface surface) {
        surface.drawImage((int) this.rec.getUpperLeft().getX(),
                (int) this.rec.getUpperLeft().getY(), this.image);
    }

    @Override
    public Rectangle getCollisionRectangle() {
        return this.rec;
    }

    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        this.hits = 0;
        this.notifyHit(hitter);
        if (hitter.getClass() != InvaderShot.class) {
            this.invadingGroup.removeInvader(this);
        }
        return currentVelocity;
    }

    /**
     *
     * @return returns invader position in invader matrix.
     */
    public Point getInvaderInMatrix() {
        return this.invaderInMatrix;
    }

    /**
     *
     * @param movement the number of pixles to move the invader on x coordinants.
     */
    public void moveX(double movement) {
        this.rec.setPosition(new Point(this.rec.getUpperLeft().getX() + movement, this.rec.getUpperLeft().getY()));
    }

    /**
     *
     * @param movement the number of pixles to move the invader on y coordinants.
     */
    public void moveY(double movement) {
        this.rec.setPosition(new Point(this.rec.getUpperLeft().getX(), this.rec.getUpperLeft().getY() + movement));
    }

    /**
     *
     * @param spriteCollection a sprite collection.
     * @param c the shots color.
     * @param gameEnvironment the game environment.
     */
    public void shoot(SpriteCollection spriteCollection, Color c, GameEnvironment gameEnvironment) {
        Point center = new Point(this.rec.getUpperLeft().getX() + this.rec.getWidth() / 2,
                this.rec.getBottomRight().getY() + 1);
        InvaderShot shot = new InvaderShot(center, 3, c, gameEnvironment, new Velocity(0, 4 * 60));
        spriteCollection.addSprite(shot);
    }

    /**
     *
     * @param rectangle the new rectangle.
     */
    public void setRec(Rectangle rectangle) {
        this.rec = rectangle;
    }
}