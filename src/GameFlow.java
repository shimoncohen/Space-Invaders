import biuoop.GUI;
import biuoop.KeyboardSensor;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import biuoop.DialogManager;
import javax.imageio.ImageIO;
import java.io.File;

/**
 * incharge of running the levels one after the other.
 */
public class GameFlow {
    private Counter score;
    private Counter lives;
    private KeyboardSensor keyboardSensor;
    private AnimationRunner animationRunner;
    private GUI gui;
    private HighScoresTable highScoresTable;
    private File highScores;
    static final int LIVES = 3;

    /**
     *
     * @param ar animationrunner object.
     * @param ks ketboardsensor object.
     * @param gui1 a new GUI object.
     * @param table the games highscore table.
     * @param f the file to save the scores to.
     * @param s score counter.
     * @param l lives counter.
     * constructor.
     */
    public GameFlow(AnimationRunner ar, KeyboardSensor ks, GUI gui1, HighScoresTable table, File f, Counter s,
                    Counter l) {
        this.score = s;
        this.lives = l;
        this.lives.increase(LIVES);
        this.animationRunner = ar;
        this.keyboardSensor = ks;
        this.gui = gui1;
        this.highScoresTable = table;
        this.highScores = f;
    }

    /**
     *
     * @param levelToRun a level to run.
     * runs the level.
     */
    public void runLevel(LevelInformation levelToRun) {
        //go over the array of levels.
        GameLevel level = new GameLevel(levelToRun, this.keyboardSensor, this.animationRunner, this.lives, this.score);
        level.initialize();
        while (this.lives.getValue() != 0 && level.getBlockCounter().getValue() != 0) {
            level.playOneTurn();
        }
            //if the player has no more lives.
        if (this.lives.getValue() == 0) {
            saveScore();
            this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor, "space",
                    new LoseScreen(this.score.getValue())));
            InputStream inputStream = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream("background_Images/burning_earth_1.jpg");
            Image i = null;
            try {
                i = ImageIO.read(inputStream);
            } catch (IOException e) {
                e.getMessage();
            }
            this.animationRunner.run(new ImageAnimation(i));
            this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor, "space",
                    new HighScoresAnimation(this.highScoresTable, "space", this.keyboardSensor)));
        }
        try {
            this.highScoresTable.save(this.highScores);
        } catch (IOException e) {
            e.getMessage();
        }
        return;
    }

    /**
     * save the new highscore.
     */
    private void saveScore() {
        if (this.highScoresTable.getRank(this.score.getValue()) != 0) {
            DialogManager dialog = gui.getDialogManager();
            String name = dialog.showQuestionDialog("Name", "What is your name?", "");
            this.highScoresTable.add(new ScoreInfo(name, this.score.getValue()));
        }
    }
}