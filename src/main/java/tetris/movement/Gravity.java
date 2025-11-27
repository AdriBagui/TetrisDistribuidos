package tetris.movement;

import tetris.MainFrame;
import tetris.tetrominoes.Tetromino;

public class Gravity {
    private static final double LOCK_DELAY_FRAMES = MainFrame.FPS/2; // aprox 500ms

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
        return delayUsed >= LOCK_DELAY_FRAMES;
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
        int framesPerCellSubstracted = 5;

        if (gravity > ((1./12.9)*60.0988)/MainFrame.FPS) {
            if (gravity > ((1./7.9)*60.0988)/MainFrame.FPS) {
                if (gravity > ((1./1.9)*60.0988)/MainFrame.FPS) {
                    framesPerCellSubstracted = 0;
                }
                else {
                    framesPerCellSubstracted = 1;
                }
            }
            else {
                framesPerCellSubstracted = 2;
            }
        }

        System.out.println((gravity*MainFrame.FPS/60.0988));

        gravity *= (gravity*MainFrame.FPS/60.0988) / ((gravity*MainFrame.FPS/60.0988) - framesPerCellSubstracted);

        System.out.println((gravity*MainFrame.FPS/60.0988));
    }

    private boolean isTouchingFloor() {
        Tetromino aux = fallingTetromino.createCopy();

        aux.moveDown();

        return collisionDetector.checkCollision(aux);
    }
}
