import biuoop.GUI;
import biuoop.KeyboardSensor;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * a task that runs a game.
 */
public class RunGameTask implements Task {
    private AnimationRunner runner;
    private KeyboardSensor keyboardSensor;
    private GUI gui;
    private HighScoresTable highScoresTable;
    private File file;

    /**
     *
     * @param runner an animation runner.
     * @param highScoresTable the games highscore table.
     * @param ks a keyboardsensor.
     * @param gui a gui object.
     * @param f the highscore file.
     */
    public RunGameTask(AnimationRunner runner, HighScoresTable highScoresTable, KeyboardSensor ks, GUI gui, File f) {
        this.runner = runner;
        this.highScoresTable = highScoresTable;
        this.keyboardSensor = ks;
        this.gui = gui;
        this.highScoresTable = highScoresTable;
        this.file = f;
    }

    @Override
    public Void run() {
        InputStream inputStream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream("background_Images/please_save_us.jpeg");
        Image i = null;
        try {
            i = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.getMessage();
        }
        this.runner.run(new ImageAnimation(i));
        Counter score = new Counter();
        Counter lives = new Counter();
        GameFlow game = new GameFlow(this.runner, this.keyboardSensor, this.gui, this.highScoresTable, this.file,
                score, lives);
        Game currentGame = new Game(game, lives);
        this.runner.run(currentGame);
        return null;
    }
}
