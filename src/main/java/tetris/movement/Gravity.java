package tetris.movement;

import tetris.tetrominoes.Tetromino;

import static tetris.Config.*;

public class Gravity {
    private Tetromino fallingTetromino;
    private double decimalY;
    private CollisionDetector collisionDetector;
    private double gravity; // cells per frame
    private double appliedGravity;
    private double lockDelayFrames; // aprox 500ms
    private double appliedLockDelayFrames;
    private int delayUsed;

    public Gravity(CollisionDetector collisionDetector, double gravity) {
        this.fallingTetromino = null;
        decimalY = 0;
        this.collisionDetector = collisionDetector;
        this.gravity = gravity;
        this.appliedGravity = gravity;
        this.lockDelayFrames = FPS/2;
        this.appliedLockDelayFrames = lockDelayFrames;
        delayUsed = 0;
    }

    public void update() {
        if (isTouchingFloor()) {
            decimalY = 0;
            delayUsed += 1;
            return;
        }

        decimalY += appliedGravity;

        while (decimalY > 1 && !isTouchingFloor()) {
            fallingTetromino.moveDown();
            decimalY -= 1;
        }

        appliedGravity = gravity;
    }

    public void setFallingTetromino(Tetromino t) {
        fallingTetromino = t;
        decimalY = 0;
        resetLockDelay();
    }

    public boolean isLocked() {
        boolean isLocked = delayUsed >= appliedLockDelayFrames;
        appliedLockDelayFrames = lockDelayFrames;
        return isLocked;
    }

    public void resetLockDelay() { delayUsed = 0; }

    public void increaseGravity() {
        int framesPerCellSubstracted;
        double oldGravity = gravity;

        if (gravity > ((1./12.9)*NES_FPS)/FPS) {
            if (gravity > ((1./7.9)*NES_FPS)/FPS) {
                if (gravity > ((1./1.9)*NES_FPS)/FPS) {
                    framesPerCellSubstracted = 0;
                }
                else {
                    framesPerCellSubstracted = 1;
                }
            }
            else {
                framesPerCellSubstracted = 2;
            }
        } else {
            framesPerCellSubstracted = 5;
        }

        gravity = 1 / ((1/gravity) - (framesPerCellSubstracted*FPS/NES_FPS));
        lockDelayFrames *= oldGravity / gravity;
    }

    public void softDrop() {
        appliedGravity = gravity * SOFT_DROP_FACTOR;
        appliedLockDelayFrames = lockDelayFrames / SOFT_DROP_DELAY_QUOTIENT;
    }

    public void hardDrop() {
        appliedGravity = BOARD_ROWS + BOARD_SPAWN_ROWS;
        appliedLockDelayFrames = 0;
    }

    private boolean isTouchingFloor() {
        Tetromino aux = fallingTetromino.createCopy();

        aux.moveDown();

        return collisionDetector.checkCollision(aux);
    }
}
