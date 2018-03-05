import biuoop.DrawSurface;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

/**
 * the screen displayed when all lives are lost.
 */
public class LoseScreen implements Animation {
    private boolean stop;
    private int score;

    /**
     *
     * @param score the score at the end of the game.
     * constructor.
     */
    public LoseScreen(int score) {
        this.stop = false;
        this.score = score;
    }

    @Override
    public void doOneFrame(DrawSurface d, double dt) {
        InputStream inputStream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream("background_Images/highscore_screen.png");
        try {
            d.drawImage(0, 0, ImageIO.read(inputStream));
        } catch (IOException e) {
            e.getMessage();
        }
        d.drawText(Ass7Game.SCREEN_WIDTH / 4, Ass7Game.SCREEN_HEIGHT / 2 + 100,
                "Game Over. your score is " + this.score, 32);
    }

    @Override
    public boolean shouldStop() {
        return this.stop;
    }
}
