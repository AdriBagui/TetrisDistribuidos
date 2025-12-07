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

    /**
     * Manages the gravity physics for the falling tetromino.
     * @param grid the board grid to check for collisions.
     * @param gravity the initial gravity value (cells per frame).
     * @param lockDelayFrames the number of frames before a tetromino locks when touching the ground.
     */
    public Gravity(BoardGrid grid, double gravity, int lockDelayFrames) {
        this.fallingTetromino = null;
        decimalY = 0;
        this.grid = grid;
        this.gravity = gravity;
        this.appliedGravity = gravity;
        this.lockDelayFrames = lockDelayFrames;  // approx 500ms
        this.appliedLockDelayFrames = lockDelayFrames;
        delayUsed = 0;
    }

    /**
     * Updates the position of the falling tetromino based on gravity.
     * Handles the movement accumulation (decimalY) and lock delay.
     */
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

    /**
     * Sets the tetromino that is currently affected by gravity.
     * Resets internal state like decimal position and lock delay.
     * @param t the new falling tetromino.
     */
    public void setFallingTetromino(Tetromino t) {
        fallingTetromino = t;
        decimalY = 0;
        resetLockDelay();
    }

    /**
     * Checks if the falling tetromino should be locked in place.
     * @return true if the lock delay has expired while touching the floor.
     */
    public boolean isLocked() {
        boolean isLocked = delayUsed >= appliedLockDelayFrames;
        return isLocked;
    }

    /**
     * Resets the lock delay timer. Usually called when the tetromino moves or rotates.
     */
    public void resetLockDelay() {
        delayUsed = 0;
        appliedLockDelayFrames = lockDelayFrames;
    }

    /**
     * Increases the base gravity value.
     * @param increment the amount to add to the current gravity.
     */
    public void increaseGravity(double increment) {
        this.gravity += gravity;
        System.out.println(1/(gravity*FPS/NES_FPS)*NES_FPS);
    }

    /**
     * Temporarily increases gravity for a soft drop effect.
     */
    public void softDrop() {
        appliedGravity = gravity + SOFT_DROP_FACTOR_GRAVITY_INCREMENT;
        appliedLockDelayFrames = lockDelayFrames / SOFT_DROP_DELAY_QUOTIENT;
    }

    /**
     * Instantly moves the tetromino to the bottom and locks it.
     */
    public void hardDrop() {
        appliedGravity = BOARD_ROWS + BOARD_SPAWN_ROWS;
        appliedLockDelayFrames = 0;
    }

    /**
     * Checks if the falling tetromino is directly above a solid surface.
     * @return true if touching the floor or another block.
     */
    private boolean isTouchingFloor() {
        Tetromino aux = fallingTetromino.createCopy();

        aux.moveDown();

        return grid.hasCollision(aux);
    }
}