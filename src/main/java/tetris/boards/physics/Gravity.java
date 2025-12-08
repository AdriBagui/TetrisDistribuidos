package tetris.boards.physics;

import tetris.boards.components.BoardGrid;
import client.userInterface.panels.tetris.TetrisPanel;
import tetris.tetrominoes.Tetromino;
import tetris.boards.nes.NESConfig;

/**
 * Manages the downward movement mechanics (Gravity) and locking logic for the active Tetromino.
 * <p>
 * This class handles:
 * <ul>
 * <li><b>Natural Gravity:</b> The piece falling automatically based on the current level.</li>
 * <li><b>Soft Drop:</b> Faster falling when the player holds 'Down'.</li>
 * <li><b>Hard Drop:</b> Instant locking when the player presses 'Up' (Modern Tetris only).</li>
 * <li><b>Lock Delay:</b> The grace period allowed when a piece touches the ground before it locks in place.</li>
 * </ul>
 * </p>
 */
public class Gravity {
    /**
     * The speed of soft dropping, normalized from NES speed (1/2G) to current FPS.
     */
    public static final double SOFT_DROP_GRAVITY = (NESConfig.SOFT_DROP_GRAVITY_IN_NES_FPS * TetrisPanel.NES_FPS) / TetrisPanel.FPS;

    private Tetromino fallingTetromino;

    /** Accumulator for fractional gravity application (since pixels are integers). */
    private double decimalY;

    private final BoardGrid grid;

    /** The base gravity speed (cells per frame) determined by the current level. */
    private double gravity;

    /** The actual gravity currently being applied (may be higher if soft dropping). */
    private double appliedGravity;

    /** The base lock delay in frames. */
    private double lockDelayFrames;

    /** The actual lock delay being applied (reduces during soft drop to prevent infinite stalling). */
    private double appliedLockDelayFrames;

    /** Counter for how many frames the piece has been on the ground. */
    private int delayUsed;

    /** Flag indicating if the piece has officially locked. */
    private boolean isLocked;

    /**
     * Constructs a new Gravity physics engine.
     *
     * @param grid            The board grid used to check for floor collisions.
     * @param gravity         The initial gravity value (cells per frame).
     * @param lockDelayFrames The number of frames a piece waits on the ground before locking.
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
     * Updates the gravity physics for the current frame.
     * <p>
     * Logic:
     * 1. If touching the floor, increment the lock delay timer.
     * 2. If in the air, accumulate gravity into {@code decimalY}.
     * 3. If {@code decimalY} exceeds 1.0, move the piece down cell-by-cell until blocked or gravity is exhausted.
     * 4. Update the {@code isLocked} state based on the delay timer.
     * </p>
     */
    public void update() {
        if (isTouchingFloor()) {
            decimalY = 0;
            delayUsed += 1;
        } else {
            decimalY += appliedGravity;

            // Apply accumulated gravity step-by-step
            while (decimalY >= 1 && !isTouchingFloor()) {
                fallingTetromino.moveDown();
                decimalY -= 1;
            }
        }

        // Lock if the delay timer has expired AND we are still touching the floor
        isLocked = (delayUsed >= appliedLockDelayFrames) && isTouchingFloor();

        // Reset applied values for next frame (unless input overrides them again)
        appliedGravity = gravity;
        appliedLockDelayFrames = lockDelayFrames;
    }

    /**
     * Assigns the active tetromino to be controlled by gravity.
     * Resets the fractional Y position and lock timers.
     *
     * @param t The new falling tetromino.
     */
    public void setFallingTetromino(Tetromino t) {
        fallingTetromino = t;
        decimalY = 0;
        resetLock();
    }

    /**
     * Checks if the falling tetromino has finished its lock delay and should be solidified.
     *
     * @return {@code true} if the piece is locked.
     */
    public boolean isLocked() { return isLocked; }

    /**
     * Resets the lock state and delay timer.
     * Typically called when a piece spawns or successfully moves/rotates (resetting the floor timer).
     */
    public void resetLock() {
        isLocked = false;
        delayUsed = 0;
    }

    /**
     * Permanently increases the base gravity value (usually on level up).
     * <p>
     * Also slightly reduces the lock delay to make higher levels more challenging.
     * </p>
     *
     * @param increment The amount to add to the current gravity (cells per frame).
     */
    public void increaseGravity(double increment) {
        double relativeIncrement = increment / gravity;
        this.gravity += increment;
        // Decrease lock delay proportional to speed increase
        lockDelayFrames -= lockDelayFrames * (relativeIncrement / 30);
    }

    /**
     * Temporarily increases gravity for this frame (Soft Drop).
     * <p>
     * This method is called by the input handler when the 'Down' key is held.
     * It also reduces the lock delay slightly to prevent players from stalling infinitely
     * by tapping soft drop.
     * </p>
     */
    public void softDrop() {
        // Apply whichever is faster: normal gravity * 1.05 OR the soft drop constant.
        appliedGravity = Math.max(gravity * 1.05, SOFT_DROP_GRAVITY);

        double relativeIncrement = (appliedGravity - gravity) / gravity;
        appliedLockDelayFrames = lockDelayFrames * (1 - relativeIncrement / 30);
    }

    /**
     * Instantly drops the piece to the bottom and forces an immediate lock (Hard Drop).
     * <p>
     * This sets gravity to an excessively high value (the height of the board) and sets
     * lock delay to zero.
     * </p>
     */
    public void hardDrop() {
        appliedGravity = grid.getTotalNumberOfRows();
        appliedLockDelayFrames = 0;
    }

    /**
     * Checks if the falling tetromino is directly above a solid surface (block or floor).
     *
     * @return {@code true} if moving down 1 cell would cause a collision.
     */
    private boolean isTouchingFloor() {
        if (fallingTetromino == null) return false;

        Tetromino aux = fallingTetromino.createCopy();
        aux.moveDown();
        return grid.hasCollision(aux);
    }
}