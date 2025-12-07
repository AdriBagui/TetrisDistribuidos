package tetris.boards.physics;

import tetris.boards.components.BoardGrid;
import tetris.panels.TetrisPanel;
import tetris.tetrominoes.Tetromino;
import tetris.boards.nes.NESConfig;

public class Gravity {
    public static final double SOFT_DROP_GRAVITY = (NESConfig.SOFT_DROP_GRAVITY_IN_NES_FPS *TetrisPanel.NES_FPS)/ TetrisPanel.FPS;

    private Tetromino fallingTetromino;
    private double decimalY;
    private final BoardGrid grid;
    private double gravity; // cells per frame
    private double appliedGravity;
    private double lockDelayFrames;
    private double appliedLockDelayFrames;
    private int delayUsed;
    private boolean isLocked;

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
        this.lockDelayFrames = lockDelayFrames;
        this.appliedLockDelayFrames = lockDelayFrames;
        delayUsed = 0;
        isLocked = false;
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

        isLocked = (delayUsed >= appliedLockDelayFrames);

        appliedGravity = gravity;
        appliedLockDelayFrames = lockDelayFrames;
    }

    /**
     * Sets the tetromino that is currently affected by gravity.
     * Resets internal state like decimal position and lock delay.
     * @param t the new falling tetromino.
     */
    public void setFallingTetromino(Tetromino t) {
        fallingTetromino = t;
        decimalY = 0;
        resetLock();
    }

    /**
     * Checks if the falling tetromino should be locked in place.
     * @return true if the lock delay has expired while touching the floor.
     */
    public boolean isLocked() { return isLocked; }

    /**
     * Resets the lock and the lock delay timer. Called when a new Tetromino is spawned on the Board
     */
    public void resetLock() {
        isLocked = false;
        delayUsed = 0;
    }

    /**
     * Increases the base gravity value.
     * @param increment the amount to add to the current gravity.
     */
    public void increaseGravity(double increment) {
        double relativeIncrement = increment/gravity;
        this.gravity += increment;
        lockDelayFrames -= lockDelayFrames*(relativeIncrement/30);
    }

    /**
     * Temporarily increases gravity for a soft drop effect.
     */
    public void softDrop() {
        appliedGravity = Math.max(gravity*1.05, SOFT_DROP_GRAVITY); // The 1.05 multiplication is our own addition
        double relativeIncrement = (appliedGravity-gravity)/gravity;
        appliedLockDelayFrames = lockDelayFrames*(1 - relativeIncrement/30);
    }

    /**
     * Instantly moves the tetromino to the bottom and locks it.
     */
    public void hardDrop() {
        appliedGravity = grid.getTotalNumberOfRows();
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