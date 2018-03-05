import biuoop.DrawSurface;
import java.awt.Image;

/**
 * a image animation.
 */
public class ImageAnimation implements Animation {
    private Image image;
    private int count;
    private boolean stop;

    /**
     *
     * @param i the image to draw.
     * constructor.
     */
    public ImageAnimation(Image i) {
        this.image = i;
    }

    @Override
    public void doOneFrame(DrawSurface d, double dt) {
        d.drawImage(0, 0, this.image);
        count++;
        if (this.count == 200) {
            this.stop = true;
        }
    }

    @Override
    public boolean shouldStop() {
        return this.stop;
    }
}
