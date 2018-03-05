import biuoop.DrawSurface;
import java.awt.Color;
import java.util.LinkedList;
import java.util.Random;

/**
 * a bunch of invaders.
 */
public class Invaders implements Sprite {
    private LinkedList<Invader> invaders;
    private double originalSpeed;
    private double invadersSpeed;
    private Invader[][] invaderMatrix;
    private Long lastShootingTime;
    static final int STARTING_X = 50;
    static final int STARTING_Y = 60;
    static final int ROWS = 5;
    static final int COLUMNS = 10;
    static final int INVADER_WIDTH = 40;
    static final int INVADER_HEIGHT = 30;

    /**
     *
     * @param speed the invaders starting speed.
     * constructor.
     */
    public Invaders(double speed) {
        this.invaders = new LinkedList<>();
        this.invaderMatrix = new Invader[ROWS][COLUMNS];
        int x = STARTING_X, y = STARTING_Y;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                Rectangle rec = new Rectangle(new Point(x, y), INVADER_WIDTH, INVADER_HEIGHT);
                this.invaderMatrix[i][j] = new Invader(this, rec, new Point(i, j));
                this.invaders.add(this.invaderMatrix[i][j]);
                x += INVADER_WIDTH + 10;
            }
            x = STARTING_X;
            y += INVADER_HEIGHT + 10;
        }
        this.originalSpeed = speed;
        this.invadersSpeed = speed;
        this.lastShootingTime = System.currentTimeMillis();
    }

    /**
     *
     * @return returns the list of invaders.
     */
    public LinkedList<Invader> getInvaders() {
        return this.invaders;
    }

    @Override
    public void timePassed(double dt) {
        moveOneStep();
    }

    @Override
    public void addToGame(GameLevel g) {
        for (int i = 0; i < this.invaders.size(); i++) {
            this.invaders.get(i).addToGame(g);
        }
    }

    @Override
    public void drawOn(DrawSurface d) {
        LinkedList<Block> temp = new LinkedList<>();
        temp.addAll(this.invaders);
        for (int i = 0; i < temp.size(); i++) {
            temp.get(i).drawOn(d);
        }
    }

    /**
     * move all invaders one step.
     */
    private void moveOneStep() {
        LinkedList<Invader> tempList = new LinkedList<>();
        tempList.addAll(this.invaders);
        boolean edge = false;
        if (invadersOnEdges()) {
            this.invadersSpeed *= -1.1;
            edge = true;
        }
        for (int i = 0; i < tempList.size(); i++) {
            tempList.get(i).moveX(this.invadersSpeed);
            if (edge) {
                tempList.get(i).moveY(INVADER_HEIGHT);
            }
        }
    }

    /**
     *
     * @return returns true if invaders are on the edge of the screen, false otherwise.
     */
    private boolean invadersOnEdges() {
        if (this.invadersSpeed > 0) {
            for (int i = 0; i < COLUMNS; i++) {
                for (int j = ROWS - 1; j >= 0; j--) {
                    if (invaderMatrix[j][i] != null) {
                        if (this.invaderMatrix[j][i].getRec().getBottomRight().getX() >= Ass7Game.SCREEN_WIDTH) {
                            return true;
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < COLUMNS; i++) {
                for (int j = 0; j < ROWS; j++) {
                    if (invaderMatrix[j][i] != null) {
                        if (this.invaderMatrix[j][i].getRec().getUpperLeft().getX() <= 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     * @return returns the number of invaders.
     */
    public int size() {
        return this.invaders.size();
    }

    /**
     *
     * @param invader an invader to remove.
     * remove dead invader.
     */
    public void removeInvader(Invader invader) {
        Invader[][] tempMatrix = this.invaderMatrix;
        LinkedList<Invader> tempList = new LinkedList<>();
        tempList.addAll(this.invaders);
        int index = tempList.indexOf(invader);
        if (index >= 0) {
            Point p = tempList.get(index).getInvaderInMatrix();
            tempMatrix[(int) p.getX()][(int) p.getY()] = null;
        }
        tempList.remove(invader);
//        this.invaderMatrix = tempMatrix;
        this.invaders = tempList;
    }

    /**
     *
     * @param spriteCollection the games sprite collection.
     * @param c the shots color.
     * @param gameEnvironment the gameenvironment.
     * orders an invader to attack.
     */
    public void attack(SpriteCollection spriteCollection, Color c, GameEnvironment gameEnvironment) {
        Long millis = System.currentTimeMillis();
        if (millis - this.lastShootingTime > 500) {
            Random r = new Random();
            int shootingInvader = r.nextInt(COLUMNS);
            if (this.invaders.size() == 0) {
                return;
            }
            while (findAttackingInvader(shootingInvader) == null) {
                shootingInvader = r.nextInt(COLUMNS);
            }
            Point temp = findAttackingInvader(shootingInvader);
            this.invaderMatrix[(int) temp.getX()][(int) temp.getY()].shoot(spriteCollection, c, gameEnvironment);
            this.lastShootingTime = millis;
        }
    }

    /**
     *
     * @param column the attacking invaders column.
     * @return returns the attacking invaders position in invaders matrix.
     */
    private Point findAttackingInvader(int column) {
        Invader[][] temp = this.invaderMatrix;
        for (int i = ROWS - 1; i >= 0; i--) {
            if (temp[i][column] != null) {
                return new Point(i, column);
            }
        }
        return null;
    }

    /**
     * resets the invaders position to original position.
     */
    public void resetPosition() {
        Invader[][] temp = this.invaderMatrix;
        int x = STARTING_X, y = STARTING_Y;
        int leftColumn = -1;
        for (int i = 0; i < COLUMNS; i++) {
            if (leftColumn > -1) {
                break;
            }
            for (int j = 0; j < ROWS; j++) {
                if (invaderMatrix[j][i] != null) {
                    leftColumn = i;
                    break;
                }
            }
        }
        for (int i = 0; i < ROWS; i++) {
            for (int j = leftColumn; j < COLUMNS; j++) {
                if (temp[i][j] != null) {
                    temp[i][j].setRec(new Rectangle(new Point(x, y), INVADER_WIDTH, INVADER_HEIGHT));
                }
                x += INVADER_WIDTH + 10;
            }
            x = STARTING_X;
            y += INVADER_HEIGHT + 10;
        }
        this.invadersSpeed = this.originalSpeed;
        //this.invaderMatrix = temp;
    }

    /**
     *
     * @param y the shields y coordinates.
     * @return returns true if invaders passed the shields, false otherwise.
     */
    public boolean successfulInvasion(int y) {
        Invader[][] temp = this.invaderMatrix;
        for (int i = ROWS - 1; i >= 0; i--) {
            for (int j = 0; j < COLUMNS; j++) {
                if (temp[i][j] != null) {
                    if (temp[i][j].getRec().getUpperLeft().getY()
                            + temp[i][j].getRec().getHeight() >= y) {
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
    }
}
