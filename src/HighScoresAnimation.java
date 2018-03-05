import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;

/**
 * Displays a highscore table.
 */
public class HighScoresAnimation implements Animation {
    private HighScoresTable highScoresTable;
    private String stopKey;
    private KeyboardSensor keyboardSensor;
    private boolean stop;

    /**
     *
     * @param scores the highscore table to display.
     * @param endKey the key to exit the animation.
     * @param ks a keyboard sensor.
     * constructor.
     */
    public HighScoresAnimation(HighScoresTable scores, String endKey, KeyboardSensor ks) {
        this.highScoresTable = scores;
        this.stopKey = endKey;
        this.keyboardSensor = ks;
        this.stop = false;
    }

    @Override
    public void doOneFrame(DrawSurface d, double dt) {
        InputStream inputStream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream("background_Images/medal_honor.jpg");
        try {
            d.drawImage(0, 0, ImageIO.read(inputStream));
        } catch (IOException e) {
            e.getMessage();
        }
        //d.drawText(100, 100, "High Scores", 50);
        int x = 80, y = 200;
        int count = 0;
        for (int i = 0; i < this.highScoresTable.getHighScores().size(); i++) {
            d.drawText(x, y, this.highScoresTable.getHighScores().get(i).getName(), 20);
            d.drawText(x + 100, y, Integer.toString(this.highScoresTable.getHighScores().get(i).getScore()), 20);
            if (count % 2 == 1) {
                y += 20;
                x = 80;
            } else {
                x += 460;
            }
            count++;
        }
        if (this.keyboardSensor.isPressed(this.stopKey)) {
            this.stop = true;
        }
        d.setColor(Color.red);
        d.drawText(265, 600, "Press space for main menu", 20);
    }

    @Override
    public boolean shouldStop() {
        return stop;
    }
}