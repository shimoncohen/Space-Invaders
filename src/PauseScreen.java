import biuoop.DrawSurface;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

/**
 * the screen displayed when the game is paused.
 */
public class PauseScreen implements Animation {
    private boolean stop;

    /**
     *
     * constructor.
     */
    public PauseScreen() {
        this.stop = false;
    }

    @Override
    public void doOneFrame(DrawSurface d, double dt) {
        InputStream inputStream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream("background_Images/pause_background.jpg");
        try {
            d.drawImage(0, 0, ImageIO.read(inputStream));
        } catch (IOException e) {
            e.getMessage();
        }
        d.drawText(160, 530, "paused -- press space to continue", 32);
    }

    @Override
    public boolean shouldStop() { return this.stop; }
}