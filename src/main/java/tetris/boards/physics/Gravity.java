package tetris.boards.physics;

import tetris.boards.components.BoardGrid;
import tetris.tetrominoes.Tetromino;

import static tetris.Config.*;

public class Gravity {
    private Tetromino fallingTetromino;
    private double decimalY;
    private BoardGrid grid;
    private double gravity; // cells per frame
    private double appliedGravity;
    private double lockDelayFrames;
    private double appliedLockDelayFrames;
    private int delayUsed;

    public Gravity(BoardGrid grid, double gravity, int lockDelayFrames) {
        this.fallingTetromino = null;
        decimalY = 0;
        this.grid = grid;
        this.gravity = gravity;
        this.appliedGravity = gravity;
        this.lockDelayFrames = lockDelayFrames;  // aprox 500ms
        this.appliedLockDelayFrames = lockDelayFrames;
        delayUsed = 0;
    }

    public void update() {
        if (isTouchingFloor()) {
            decimalY = 0;
            delayUsed += 1;
        } else {
            decimalY += appliedGravity;

            while (decimalY > 1 && !isTouchingFloor()) {
                fallingTetromino.moveDown();
                decimalY -= 1;
            }
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
        return isLocked;
    }

    public void resetLockDelay() {
        delayUsed = 0;
        appliedLockDelayFrames = lockDelayFrames;
    }

    public void increaseGravity(double increment) {
        this.gravity += gravity;
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

        return grid.hasCollision(aux);
    }
}
