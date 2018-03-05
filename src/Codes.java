import biuoop.DrawSurface;
import java.awt.Color;

/**
 * an animation showing the game codes.
 */
public class Codes implements Animation {
    private boolean stop;

    @Override
    public boolean shouldStop() {
        return this.stop;
    }

    @Override
    public void doOneFrame(DrawSurface d, double dt) {
        d.drawText(260, 200, "Press 'w' to shoot from wings.", 20);
        d.drawText(260, 250, "Press 's' to shoot from sides.", 20);
        d.drawText(260, 300, "Press 'm' to shoot from middle.", 20);
        d.drawText(260, 350, "Press 'ba' to shoot a bomb.", 20);
        d.setColor(Color.red);
        d.drawText(260, 500, "Press space for main menu", 20);
    }
}
