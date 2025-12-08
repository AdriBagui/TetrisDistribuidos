package tetris.boards.physics;

import tetris.boards.components.BoardGrid;
import tetris.boards.physics.rotationSystems.RotationSystem;
import client.userInterface.panels.tetris.TetrisPanel;
import tetris.tetrominoes.Tetromino;

/**
 * Handles processing of raw keyboard input and applying it to the game state.
 * <p>
 * This class implements standard Tetris control mechanics, specifically:
 * <ul>
 * <li><b>DAS (Delayed Auto Shift):</b> The initial delay before a held key starts repeating.</li>
 * <li><b>ARR (Automatic Repeat Rate):</b> The speed at which the movement repeats after DAS kicks in.</li>
 * </ul>
 * It buffers inputs to ensure responsive movement and prevents conflicting actions (like moving left and right simultaneously).
 * </p>
 */
public class PlayerInputHandler {
    // --- INPUT TIMING CONSTANTS ---
    /** * Automatic Repeat Rate: How many frames between movement repeats.
     * Calculated to be approx 30hz (every 2 frames at 60fps).
     */
    public static final int AUTOMATIC_REPEAT_RATE_FRAMES = ((int) TetrisPanel.FPS) / 30;

    /** * Delayed Auto Shift: How many frames a key must be held before auto-repeat begins.
     * Calculated to be approx 6hz (wait ~10 frames at 60fps).
     */
    public static final int DELAYED_AUTO_SHIFT_FRAMES = ((int) TetrisPanel.FPS) / 6;

    private final BoardGrid grid;
    private final RotationSystem rotationSystem;
    private final Gravity gravity;
    private Tetromino fallingTetromino;

    // --- INPUT STATE COUNTERS ---
    // -1: Released (inactive)
    //  0: Just Pressed (action triggered this frame)
    // >0: Held down (counting frames for DAS/ARR)

    private int softDropping;
    private int movingLeft;
    private int movingRight;
    private int hardDropped;
    private int rotatedLeft;
    private int rotatedRight;
    private int flipped;

    /**
     * Creates a new Input Handler.
     *
     * @param grid           The board grid for collision checking during movement.
     * @param rotationSystem The system to use for calculating rotations (e.g., SRS, NES).
     * @param gravity        The gravity engine to trigger drops.
     */
    public PlayerInputHandler(BoardGrid grid, RotationSystem rotationSystem, Gravity gravity) {
        this.grid = grid;
        this.rotationSystem = rotationSystem;
        this.gravity = gravity;
        this.fallingTetromino = null;

        // Initialize all inputs to "Released" state
        softDropping = -1;
        movingLeft = -1;
        movingRight = -1;
        hardDropped = -1;
        rotatedLeft = -1;
        rotatedRight = -1;
        flipped = -1;
    }

    /**
     * Sets the active tetromino that will respond to player commands.
     *
     * @param fallingTetromino The currently falling piece.
     */
    public void setFallingTetromino(Tetromino fallingTetromino) {
        this.fallingTetromino = fallingTetromino;
    }

    /**
     * Processes input states and updates the tetromino's position or orientation.
     * <p>
     * This method should be called exactly once per game frame. It handles:
     * 1. Hard Drop (Instant)
     * 2. Left/Right Movement (DAS/ARR logic)
     * 3. Soft Drop
     * 4. Rotations
     * </p>
     */
    public void update() {
        // 1. Handle Hard Drop (Executes immediately and only once per press)
        if (hardDropped == 0) {
            gravity.hardDrop();
            hardDropped = 1; // Mark as processed
            return; // Hard drop ends the turn logic for this piece effectively
        }

        // 2. Handle Horizontal Movement with DAS/ARR priority
        if (movingLeft < 0) {
            // Left is not held, process Right normally
            movingRightUpdate();
        } else {
            if (movingRight < 0) {
                // Right is not held, process Left normally
                movingLeftUpdate();
            } else {
                // Both keys held: Prioritize the most recently pressed or resolve conflict
                if (movingRight < movingLeft) {
                    // Right was pressed more recently (smaller counter)
                    movingRightUpdate();

                    // Keep the Left counter ticking but don't move, so if Right is released,
                    // Left resumes DAS immediately without resetting.
                    if (movingLeft < DELAYED_AUTO_SHIFT_FRAMES + AUTOMATIC_REPEAT_RATE_FRAMES + 1) {
                        movingLeft++;
                    }
                } else {
                    // Left was pressed more recently
                    movingLeftUpdate();

                    if (movingRight < DELAYED_AUTO_SHIFT_FRAMES + AUTOMATIC_REPEAT_RATE_FRAMES + 1) {
                        movingRight++;
                    }
                }
            }
        }

        // 3. Handle Soft Drop
        if (softDropping >= 0) {
            gravity.softDrop();
            // Note: Soft drop usually doesn't need DAS, just immediate repeated gravity application
        }

        // 4. Handle Rotations (Action on press only)
        if (rotatedRight == 0) {
            rotationSystem.rotateRight(fallingTetromino);
            rotatedRight = 1;
        }

        if (rotatedLeft == 0) {
            rotationSystem.rotateLeft(fallingTetromino);
            rotatedLeft = 1;
        }

        if (flipped == 0) {
            rotationSystem.flip(fallingTetromino);
            flipped = 1;
        }
    }

    // --- EVENT TRIGGERS (Called by KeyListeners) ---

    /**
     * Call when Move Left key is pressed.
     * Initializes the counter to 0 to trigger immediate movement.
     */
    public void moveLeftPressed() {
        if (movingLeft < 0) {
            movingLeft = 0;
            // If we were holding Right, ensure Right counter > Left counter so Left takes priority
            if (movingRight == 0) movingRight++;
        }
    }

    /** Call when Move Left key is released. Resets counter to -1. */
    public void moveLeftReleased() { movingLeft = -1; }

    /**
     * Call when Move Right key is pressed.
     * Initializes the counter to 0 to trigger immediate movement.
     */
    public void moveRightPressed() {
        if (movingRight < 0) {
            movingRight = 0;
            // If we were holding Left, ensure Left counter > Right counter so Right takes priority
            if (movingLeft == 0) movingLeft++;
        }
    }

    /** Call when Move Right key is released. */
    public void moveRightReleased() { movingRight = -1; }

    /**
     * Call when Soft Drop key is pressed.
     * Initializes the counter to 0 to trigger immediate movement.
     */
    public void softDropPressed() { if (softDropping < 0) softDropping = 0; }
    /** Call when Soft Drop key is released. */
    public void softDropReleased() { softDropping = -1; }

    /**
     * Call when Hard Drop key is pressed.
     * Initializes the counter to 0 to trigger immediate movement.
     */
    public void hardDropPressed() { if (hardDropped < 0) hardDropped = 0; }
    /** Call when Hard Drop key is released. */
    public void hardDropReleased() { hardDropped = -1; }

    /**
     * Call when Rotate Right key is pressed.
     * Initializes the counter to 0 to trigger immediate movement.
     */
    public void rotateRightPressed() { if (rotatedRight < 0) rotatedRight = 0; }
    /** Call when Rotate Right key is released. */
    public void rotateRightReleased() { rotatedRight = -1; }

    /**
     * Call when Rotate Left key is pressed.
     * Initializes the counter to 0 to trigger immediate movement.
     */
    public void rotateLeftPressed() { if (rotatedLeft < 0) rotatedLeft = 0; }
    /** Call when Rotate Left key is released. */
    public void rotateLeftReleased() { rotatedLeft = -1; }

    /**
     * Call when Flip key is pressed.
     * Initializes the counter to 0 to trigger immediate movement.
     */
    public void flipPressed() { if (flipped < 0) flipped = 0; }
    /** Call when Flip key is released. */
    public void flipReleased() { flipped = -1; }

    // --- INTERNAL MOVEMENT LOGIC (DAS/ARR) ---

    private void tryAndApplyMoveLeft() {
        Tetromino aux = fallingTetromino.createCopy();
        aux.moveLeft();
        if (!grid.hasCollision(aux)) {
            fallingTetromino.moveLeft();
        }
    }

    private void tryAndApplyMoveRight() {
        Tetromino aux = fallingTetromino.createCopy();
        aux.moveRight();
        if (!grid.hasCollision(aux)) {
            fallingTetromino.moveRight();
        }
    }

    /**
     * Logic for applying Right movement based on the counter state.
     */
    private void movingRightUpdate() {
        // State 0: Initial Press (Move immediately)
        if (movingRight == 0) {
            tryAndApplyMoveRight();
            movingRight++;
        }
        // State > 0: Button is held
        else if (movingRight > 0) {
            // Waiting for DAS (Initial Delay)
            if (movingRight < DELAYED_AUTO_SHIFT_FRAMES + AUTOMATIC_REPEAT_RATE_FRAMES) {
                movingRight++;
            }
            // DAS expired, trigger movement (ARR) and reset ARR timer
            else {
                tryAndApplyMoveRight();
                movingRight = DELAYED_AUTO_SHIFT_FRAMES; // Reset to just after DAS
            }
        }
    }

    /**
     * Logic for applying Left movement based on the counter state.
     */
    private void movingLeftUpdate() {
        // State 0: Initial Press (Move immediately)
        if (movingLeft == 0) {
            tryAndApplyMoveLeft();
            movingLeft++;
        }
        // State > 0: Button is held
        else if (movingLeft > 0) {
            // Waiting for DAS
            if (movingLeft < DELAYED_AUTO_SHIFT_FRAMES + AUTOMATIC_REPEAT_RATE_FRAMES) {
                movingLeft++;
            }
            // Trigger movement
            else {
                tryAndApplyMoveLeft();
                movingLeft = DELAYED_AUTO_SHIFT_FRAMES;
            }
        }
    }
}