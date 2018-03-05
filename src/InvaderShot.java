/**
 * an invader shot.
 */
public class InvaderShot extends Ball {

    /**
     * @param center the center Point of the ball.
     * @param color  the color of the ball.
     * @param r      the radius of the ball.
     * @param ge     A GameEnvironment object.
     * @param velocity the balls velocity.
     * constructor.
     */
    public InvaderShot(Point center, int r, java.awt.Color color, GameEnvironment ge, Velocity velocity) {
        super(center, r, color, ge, velocity);
    }
}
