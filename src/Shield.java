import biuoop.DrawSurface;
import java.awt.Color;
import java.util.LinkedList;

/**
 * a shield made of blocks.
 */
public class Shield implements Sprite, HitNotifier {
    private LinkedList<Block> shieldBlocks;
    static final int SHIELD_BLOCK_SIZE = 5;

    /**
     *
     * @param startingPoint the shields starting point.
     * constructor.
     */
    public Shield(Point startingPoint) {
        this.shieldBlocks = new LinkedList<>();
        createShield(startingPoint);
    }

    /**
     *
     * @param startingPoint the shields starting point.
     * create the shield.
     */
    private void createShield(Point startingPoint) {
        double x = startingPoint.getX(), y = startingPoint.getY();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 20; j++) {
                Rectangle r = new Rectangle(new Point(x, y), SHIELD_BLOCK_SIZE, SHIELD_BLOCK_SIZE);
                this.shieldBlocks.add(new DestroyableBlock(r, Color.ORANGE));
                x += SHIELD_BLOCK_SIZE;
            }
            x = startingPoint.getX();
            y += SHIELD_BLOCK_SIZE;
        }
    }

    @Override
    public void drawOn(DrawSurface d) {
        for (int i = 0; i < this.shieldBlocks.size(); i++) {
            this.shieldBlocks.get(i).drawOn(d);
        }
    }

    @Override
    public void addToGame(GameLevel g) {
        for (int i = 0; i < this.shieldBlocks.size(); i++) {
            this.shieldBlocks.get(i).addToGame(g);
        }
    }

    @Override
    public void timePassed(double dt) {
    }

    @Override
    public void addHitListener(HitListener hl) {
        for (int i = 0; i < this.shieldBlocks.size(); i++) {
            this.shieldBlocks.get(i).addHitListener(hl);
        }
    }

    @Override
    public void removeHitListener(HitListener hl) {
        for (int i = 0; i < this.shieldBlocks.size(); i++) {
            this.shieldBlocks.get(i).removeHitListener(hl);
        }
    }
}
