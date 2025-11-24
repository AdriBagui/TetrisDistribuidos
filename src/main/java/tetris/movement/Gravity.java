package tetris.movement;

import tetris.MainFrame;
import tetris.tetrominoes.Tetromino;

public class Gravity {
    private static final double LOCK_DELAY = MainFrame.FPS/2; // 500ms

    private boolean[][] grid;
    private Tetromino fallingTetromino;
    private double decimalY;
    private CollisionDetector collisionDetector;
    private double gravity; // cells per frame
    private double extraInputGravity;
    private boolean touchingFloor;
    private int delayUsed;

    public Gravity(boolean[][] grid, CollisionDetector collisionDetector, double gravity) {
        this.grid = grid;
        this.fallingTetromino = null;
        decimalY = 0;
        this.collisionDetector = collisionDetector;
        this.gravity = gravity;
        touchingFloor = false;
        delayUsed = 0;
    }

    public void update() {
        if (isTouchingFloor()) {
            decimalY = 0;
            delayUsed += 1;
            return;
        }

        decimalY += gravity;

        while (decimalY > 1 && !isTouchingFloor()) {
            fallingTetromino.moveDown();
            decimalY -= 1;
        }
    }

    public boolean locked() {
        return delayUsed >= LOCK_DELAY;
    }

    public void setFallingTetromino(Tetromino t) {
        fallingTetromino = t;
        decimalY = 0;
        resetLockDelay();
    }

    public void resetLockDelay() {
        delayUsed = 0;
        touchingFloor = false;
    }

    public void increaseGravity() {
        gravity += 0.025;
    }

    private boolean isTouchingFloor() {
        boolean isTouchingFloor;

        fallingTetromino.moveDown();
        isTouchingFloor = collisionDetector.checkCollision(fallingTetromino);
        fallingTetromino.moveUp();

        return isTouchingFloor;
    }
}
