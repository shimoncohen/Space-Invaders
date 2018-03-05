import biuoop.DrawSurface;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * A block consisting of a rectangle.
 */
public class DestroyableBlock extends Block implements HitNotifier {
    private Rectangle rec;
    private Color fill;
    private List<HitListener> hitListeners;

    /**
     * @param rec1 A rectangle object.
     * @param c A color for the rectangle.
     * constructor.
     */
    public DestroyableBlock(Rectangle rec1, Color c) {
        this.rec = rec1;
        this.fill = c;
        this.hitListeners = new ArrayList<>();
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
        List<HitListener> listeners = new ArrayList<HitListener>(this.hitListeners);
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
        return 0;
    }

    @Override
    public Rectangle getRec() {
        return this.rec;
    }

    @Override
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.fill);
        surface.fillRectangle((int) this.rec.getUpperLeft().getX(), (int) this.rec.getUpperLeft().getY(),
                (int) this.rec.getWidth(), (int) this.rec.getHeight());
    }

    @Override
    public Rectangle getCollisionRectangle() {
        return this.rec;
    }

    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        this.notifyHit(hitter);
        return currentVelocity;
    }
}