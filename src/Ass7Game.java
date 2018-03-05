import biuoop.GUI;
import biuoop.Sleeper;
import biuoop.KeyboardSensor;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;

/**
 * Executing class.
 */
public class Ass7Game {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    /**
     *
     * @param args the arguments to the main.
     * the main function.
     */
    public static void main(String[] args) {
        GUI gui = new GUI("Arkanoid", SCREEN_WIDTH, SCREEN_HEIGHT);
        Sleeper sleep = new Sleeper();
        AnimationRunner animationRunner = new AnimationRunner(gui, sleep);
        KeyboardSensor ks = gui.getKeyboardSensor();
        File f = new File("highscores");
        HighScoresTable highScoresTable = HighScoresTable.loadFromFile(f);
        InputStream inputStream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream("background_Images/start_screen.jpg");
        Image i = null;
        try {
            i = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.getMessage();
        }
        animationRunner.run(new ImageAnimation(i));
        inputStream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream("background_Images/get_ready.jpg");
        try {
            i = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.getMessage();
        }
        MenuAnimation openingMenu = new MenuAnimation<String>("Main Menu", ks, animationRunner, i);
        Menu<Task> menu = openingMenu;
        while (true) {
            menu.addSelection("s", "Start Game", new RunGameTask(animationRunner, highScoresTable, ks, gui, f));
            menu.addSelection("c", "Game Codes", new RunTask(animationRunner, new KeyPressStoppableAnimation(ks,
                    "space", new Codes())));
            menu.addSelection("h", "Highscores", new RunTask(animationRunner,
                    new KeyPressStoppableAnimation(ks, "space", new HighScoresAnimation(highScoresTable, "s", ks))));
            menu.addSelection("q", "Quit", new CloseTask(gui, f, highScoresTable));
            animationRunner.run(menu);
            // wait for user selection
            Task<Void> task = menu.getStatus();
            task.run();
            openingMenu.resetStatus();
            openingMenu.reboot();
        }
    }
}