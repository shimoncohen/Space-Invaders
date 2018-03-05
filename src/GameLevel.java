import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import java.awt.Color;

/**
 * A game being played.
 */
public class GameLevel implements Animation {
    private LevelInformation levInfo;
    private SpriteCollection sprites;
    private SpriteCollection shots;
    private SpriteCollection bombs;
    private GameEnvironment environment;
    private Counter blockCount;
    private Counter ballCount;
    private Counter score;
    private Counter lives;
    private KeyboardSensor keyboard;
    private Paddle pad;
    private Color ballsAndPaddleColor;
    private boolean aPressed;
    private int bombTime;
    private BombAnimation bombAnimation;
    static final int REC_HEIGHT = 30;
    static final int BORDER_HEIGHT_OR_WIDTH = 25;
    static final int PADDLE_HEIGHT = 50;
    static final int SHIELD_Y_POS = 500;
    private AnimationRunner runner;
    private boolean running;

    /**
     *
     * @param animationRunner a new animation runner.
     * @param levelInfo a new level info.
     * @param lives a new counter for the players lives.
     * @param score a new counter for the players score.
     * @param ks a keyboardsensor.
     * constructor.
     */
    public GameLevel(LevelInformation levelInfo, KeyboardSensor ks, AnimationRunner animationRunner,
                     Counter lives, Counter score) {
        this.levInfo = levelInfo;
        this.sprites = new SpriteCollection();
        this.shots = new SpriteCollection();
        this.bombs = new SpriteCollection();
        this.sprites.addSprite(this.levInfo.getBackground());
        this.environment = new GameEnvironment();
        this.levInfo.blocks().addToGame(this);
        this.blockCount = new Counter();
        this.blockCount.increase(levelInfo.numberOfBlocksToRemove());
        this.ballCount = new BallCounter();
        this.ballCount.increase(this.levInfo.numberOfBalls());
        this.lives = lives;
        this.score = score;
        this.keyboard = ks;
        this.runner = animationRunner;
        this.ballsAndPaddleColor = Color.yellow;
        this.bombAnimation = new BombAnimation(this.bombs, this.environment);
        this.sprites.addSprite(this.bombAnimation);
    }

    /**
     *
     * @return returns the block counter.
     */
    public Counter getBlockCounter() {
        return this.blockCount;
    }

    @Override
    public boolean shouldStop() {
        if (!this.running) {
            return true;
        }
        return false;
    }

    @Override
    /**
     *
     * @param d a DrawSurface onject.
     * @param dt amount of seconds past since last call.
     * the games logic.
     */
    public void doOneFrame(DrawSurface d, double dt) {
        this.levInfo.blocks().timePassed(dt);
        this.sprites.drawAllOn(d);
        this.shots.drawAllOn(d);
        this.bombs.drawAllOn(d);
        this.sprites.notifyAllTimePassed(dt);
        this.shots.notifyAllTimePassed(dt);
        this.bombs.notifyAllTimePassed(dt);
        //if no blocks are left.
        if (this.levInfo.blocks().successfulInvasion(SHIELD_Y_POS)) {
            this.ballCount.decrease(1);
        }
        if (this.ballCount.getValue() == 0) { //if no balls are left.
            this.pad.removeFromGame(this);
            this.pad = new Paddle(new Rectangle(new Point(Ass7Game.SCREEN_WIDTH / 2 - this.levInfo.paddleWidth() / 2,
                    Ass7Game.SCREEN_HEIGHT - PADDLE_HEIGHT), this.levInfo.paddleWidth(),
                    PADDLE_HEIGHT), this.keyboard, this.levInfo.paddleSpeed(), this.ballsAndPaddleColor,
                    this.bombAnimation);
            BallRemover br = new BallRemover(this, this.ballCount);
            this.pad.addHitListener(br);
            this.pad.addToGame(this);
            this.lives.decrease(1);
            this.ballCount.increase(1);
            this.levInfo.blocks().resetPosition();
            this.shots = new SpriteCollection();
            this.bombs = new SpriteCollection();
            this.running = false;
        }
        if (this.blockCount.getValue() == 0) {
            this.running = false;
        }
        if (this.keyboard.isPressed("p")) { //if the key p is pressed.
            this.runner.run(new KeyPressStoppableAnimation(this.keyboard, "space", new PauseScreen()));
        }
        if (this.keyboard.isPressed(KeyboardSensor.SPACE_KEY)) {
            this.pad.shoot(this.shots, this.ballsAndPaddleColor, this.environment);
        }
        this.levInfo.blocks().attack(this.shots, this.ballsAndPaddleColor, this.environment);
        if (this.keyboard.isPressed("a")) {
            this.aPressed = true;
            this.bombTime = 0;
        }
        if (this.keyboard.isPressed("b")) {
            if (this.aPressed) {
                this.aPressed = false;
                if (this.bombs.getCollection().size() == 0) {
                    this.pad.bombAttack(this.bombs, this.environment, this.bombAnimation);
                }
            }
        }
        if (this.bombTime / 1000 >= 3) {
            this.aPressed = false;
            this.bombTime = 0;
        }
        this.bombTime += System.currentTimeMillis();
    }

    /**
     *
     * @param c A Collidable object to add to GameEnvironment.
     * adds a collidable to the game.
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     *
     * @param s1 A Sprite object to add to SpriteCollection.
     * adds a sprite to the game.
     */
    public void addSprite(Sprite s1) {
        if (s1.getClass() == Ball.class) {
            this.shots.addSprite(s1);
        } else {
            this.sprites.addSprite(s1);
        }
    }

    /**
     *
     * @param c a collidable object.
     * removes a collidable from the game.
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);
    }

    /**
     *
     * @param s a sprite object.
     * removes a sprite from the game.
     */
    public void removeSprite(Sprite s) {
        if (this.sprites.getCollection().contains(s)) {
            this.sprites.removeSprite(s);
        } else if (this.shots.getCollection().contains(s)) {
            this.shots.removeSprite(s);
        } else if (this.bombs.getCollection().contains(s)) {
            this.bombs.removeSprite(s);
        }
    }

    /**
     *
     * Initialize a new game: create the Blocks and Ball (and Paddle).
     * and add them to the game.
     */
    public void initialize() {
        //add the information block on top of the game.
        Rectangle rec = new Rectangle(new Point(0, 0), Ass7Game.SCREEN_WIDTH, REC_HEIGHT);
        BorderBlock info = new BorderBlock(rec, Color.white);
        this.sprites.addSprite(info);
        //create all of the listeners.
        BallRemover br = new BallRemover(this, this.ballCount);
        BlockRemover remover = new BlockRemover(this, this.blockCount);
        ShotRemover sr = new ShotRemover(this);
        ShieldRemover shieldRemover = new ShieldRemover(this);
        ScoreTrackingListener trackS = new ScoreTrackingListener(this.score);
        //create the paddle.
        this.pad = new Paddle(new Rectangle(new Point(Ass7Game.SCREEN_WIDTH / 2 - this.levInfo.paddleWidth() / 2,
                Ass7Game.SCREEN_HEIGHT - PADDLE_HEIGHT), this.levInfo.paddleWidth(),
                PADDLE_HEIGHT), this.keyboard, this.levInfo.paddleSpeed(), this.ballsAndPaddleColor,
                this.bombAnimation);
        this.pad.addHitListener(br);
        this.pad.addToGame(this);
        // top border block.
        BorderBlock b = new BorderBlock(new Rectangle(new Point(0, 0 + REC_HEIGHT), Ass7Game.SCREEN_WIDTH,
                BORDER_HEIGHT_OR_WIDTH), Color.gray);
        b.addHitListener(sr);
        b.addToGame(this);
        // bottom border block.
        // bottom border block
        Color removerC = this.ballsAndPaddleColor;
        b = new BorderBlock(new Rectangle(new Point(BORDER_HEIGHT_OR_WIDTH, Ass7Game.SCREEN_HEIGHT),
                Ass7Game.SCREEN_WIDTH - 2 * BORDER_HEIGHT_OR_WIDTH, BORDER_HEIGHT_OR_WIDTH), removerC);
        b.addHitListener(sr);
        b.addToGame(this);
        //shields up!.
        Shield shield1 = new Shield(new Point(100, SHIELD_Y_POS));
        Shield shield2 = new Shield(new Point(350, SHIELD_Y_POS));
        Shield shield3 = new Shield(new Point(600, SHIELD_Y_POS));
        shield1.addHitListener(sr);
        shield1.addHitListener(shieldRemover);
        shield1.addToGame(this);
        shield2.addHitListener(sr);
        shield2.addHitListener(shieldRemover);
        shield2.addToGame(this);
        shield3.addHitListener(sr);
        shield3.addHitListener(shieldRemover);
        shield3.addToGame(this);
        Invaders l = this.levInfo.blocks();
        //add listeners to blocks, and blocks to the game.
        for (Invader invader : l.getInvaders()) {
            invader.addHitListener(trackS);
            invader.addHitListener(remover);
        }
        //create all the indicators.
        ScoreIndicator sci = new ScoreIndicator(this.score);
        LivesIndicator li = new LivesIndicator(this.lives);
        LevelNameIndicator lni = new LevelNameIndicator(this.levInfo.levelName());
        li.addToGame(this);
        sci.addToGame(this);
        lni.addToGame(this);
    }

    /**
     * Run the game -- start the animation loop.
     */
    public void playOneTurn() {
        this.runner.run(new CountdownAnimation(2, 3, this.sprites));
        this.running = true;
        this.runner.run(this);
    }
}