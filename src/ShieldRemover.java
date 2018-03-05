/**
 * removes a shield from the game.
 */
public class ShieldRemover implements HitListener {
    private GameLevel game;

    /**
     *
     * @param game the current gamelevel.
     * constructor.
     */
    public ShieldRemover(GameLevel game) {
        this.game = game;
    }

    @Override
    /**
     *
     * @param beingHit a block being hit.
     * @param hitter the ball that hit the block.
     * Blocks that are hit and reach 0 hit-points are removed from the game.
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        beingHit.removeFromGame(this.game);
        beingHit.removeHitListener(this);
        hitter.removeFromGame(this.game);
    }
}