import biuoop.DrawSurface;

/**
 * the animation that runs the game.
 */
public class Game implements Animation {
    private GameFlow gf;
    private boolean stop;
    private Invaders invade;
    private int level;
    private Counter lives;

    /**
     *
     * @param game the game management.
     * @param l the players lives counter.
     * constructor.
     */
    public Game(GameFlow game, Counter l) {
        this.gf = game;
        this.stop = false;
        invade = null;
        this.level = 1;
        this.lives = l;
    }

    @Override
    public void doOneFrame(DrawSurface d, double dt) {
        //run the game.
        double speed = 1;
        this.invade = new Invaders(speed);
        while (true) {
            Level l = new Level("Battle number " + Integer.toString(this.level), "color(Color.black)", 360, 100,
                    this.invade);
            this.gf.runLevel(l);
            if (this.lives.getValue() == 0) {
                break;
            }
            speed *= 1.2;
            this.invade = new Invaders(speed);
            this.level += 1;
        }
        this.stop = true;
    }

    @Override
    public boolean shouldStop() {
        return this.stop;
    }
}
