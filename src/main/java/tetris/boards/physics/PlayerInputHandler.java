package tetris.boards.physics;

import tetris.boards.components.BoardGrid;
import tetris.boards.physics.rotationSystems.RotationSystem;
import tetris.tetrominoes.Tetromino;

import static tetris.Config.*;

public class PlayerInputHandler {
    private BoardGrid grid;
    private RotationSystem rotationSystem;
    private Gravity gravity;
    private Tetromino fallingTetromino;

    // INPUT STATE COUNTERS
    // -1: Released
    // 0: Just Pressed
    // >0: Held down (counting frames)
    private int softDropping;
    private int movingLeft;
    private int movingRight;
    private int hardDropped;
    private int rotatedLeft;
    private int rotatedRight;
    private int flipped;

    /**
     * Handles player input and applies it to the falling tetromino.
     * Manages DAS (Delayed Auto Shift) and ARR (Automatic Repeat Rate).
     * @param grid the game grid.
     * @param rotationSystem the system used to calculate rotations.
     * @param gravity the gravity controller.
     */
    public PlayerInputHandler(BoardGrid grid, RotationSystem rotationSystem, Gravity gravity) {
        this.grid = grid;
        this.rotationSystem = rotationSystem;
        this.gravity = gravity;
        this.fallingTetromino = null;

        softDropping = -1;
        movingLeft = -1;
        movingRight = -1;
        hardDropped = -1;
        rotatedLeft = -1;
        rotatedRight = -1;
        flipped = -1;
    }

    /**
     * Sets the tetromino that will receive inputs.
     * @param fallingTetromino the active tetromino.
     */
    public void setFallingTetromino(Tetromino fallingTetromino) {
        this.fallingTetromino = fallingTetromino;
    }

    /**
     * Processes input states and updates the tetromino's position or orientation.
     * Should be called every frame.
     */
    public void update() {
        if (hardDropped == 0) {
            gravity.hardDrop();

            hardDropped = 1;
            return;
        }

        // Handle Horizontal Movement (Left/Right) with DAS/ARR logic
        if (movingLeft < 0) {
            movingRightUpdate();
        } else {
            if (movingRight < 0) {
                movingLeftUpdate();
            } else {
                // If both are held, prioritize the most recently pressed or handle simultaneously
                if (movingRight < movingLeft) {
                    movingRightUpdate();

                    if (movingLeft < DELAYED_AUTO_SHIFT_FRAMES + AUTOMATIC_REPEAT_RATE_FRAMES + 1) {
                        movingLeft++;
                    }
                } else {
                    movingLeftUpdate();

                    if (movingRight < DELAYED_AUTO_SHIFT_FRAMES + AUTOMATIC_REPEAT_RATE_FRAMES + 1) {
                        movingRight++;
                    }
                }
            }
        }

        if (softDropping >= 0) {
            gravity.softDrop();
        }

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

    // INPUT EVENT METHODS

    /**
     * Registers that the move left key has been pressed.
     */
    public void moveLeftPressed() {
        if (movingLeft < 0) {
            movingLeft = 0;

            if (movingRight == 0) { // Make sure if movingRight has been pressed before, moving right is greater than movingLeft
                movingRight++;
            }
        }
    }

    /**
     * Registers that the move left key has been released.
     */
    public void moveLeftReleased() {
        movingLeft = -1;
    }

    /**
     * Registers that the move right key has been pressed.
     */
    public void moveRightPressed() {
        if (movingRight < 0) {
            movingRight = 0;

            if (movingLeft == 0) { // Make sure if movingLeft has been pressed before, moving right is greater than movingRight
                movingLeft++;
            }
        }
    }

    /**
     * Registers that the move right key has been released.
     */
    public void moveRightReleased() {
        movingRight = -1;
    }

    /**
     * Registers that the soft drop key has been pressed.
     */
    public void softDropPressed() {
        if (softDropping < 0) {
            softDropping = 0;
        }
    }

    /**
     * Registers that the soft drop key has been released.
     */
    public void softDropReleased() {
        softDropping = -1;
    }

    /**
     * Registers that the hard drop key has been pressed.
     */
    public void hardDropPressed() {
        if (hardDropped < 0) {
            hardDropped = 0;
        }
    }

    /**
     * Registers that the hard drop key has been released.
     */
    public void hardDropReleased() {
        hardDropped = -1;
    }

    /**
     * Registers that the rotate right key has been pressed.
     */
    public void rotateRightPressed() {
        if (rotatedRight < 0) {
            rotatedRight = 0;
        }
    }

    /**
     * Registers that the rotate right key has been released.
     */
    public void rotateRightReleased() {
        rotatedRight = -1;
    }

    /**
     * Registers that the rotate left key has been pressed.
     */
    public void rotateLeftPressed() {
        if (rotatedLeft < 0) {
            rotatedLeft = 0;
        }
    }

    /**
     * Registers that the rotate left key has been released.
     */
    public void rotateLeftReleased() {
        rotatedLeft = -1;
    }

    /**
     * Registers that the flip (180 rotation) key has been pressed.
     */
    public void flipPressed() {
        if (flipped < 0) {
            flipped = 0;
        }
    }

    /**
     * Registers that the flip key has been released.
     */
    public void flipReleased() {
        flipped = -1;
    }

    // INTERNAL MOVEMENT LOGIC

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

    private void tryAndApplyMoveDown() {
        Tetromino aux = fallingTetromino.createCopy();

        aux.moveDown();

        if (!grid.hasCollision(aux)) {
            fallingTetromino.moveDown();
        }
    }

    /**
     * Updates logic for moving right, including handling DAS and ARR.
     */
    private void movingRightUpdate() {
        if (movingRight == 0) {
            tryAndApplyMoveRight();
            movingRight++;
        } else if (movingRight > 0) {
            if (movingRight < DELAYED_AUTO_SHIFT_FRAMES + AUTOMATIC_REPEAT_RATE_FRAMES) {
                movingRight++;
            } else {
                tryAndApplyMoveRight();
                movingRight = DELAYED_AUTO_SHIFT_FRAMES;
            }
        }
    }

    /**
     * Updates logic for moving left, including handling DAS and ARR.
     */
    private void movingLeftUpdate() {
        if (movingLeft == 0) {
            tryAndApplyMoveLeft();
            movingLeft++;
        } else if (movingLeft > 0) {
            if (movingLeft < DELAYED_AUTO_SHIFT_FRAMES + AUTOMATIC_REPEAT_RATE_FRAMES) {
                movingLeft++;
            } else {
                tryAndApplyMoveLeft();
                movingLeft = DELAYED_AUTO_SHIFT_FRAMES;
            }
        }
    }

    /**
     * Updates logic for soft dropping, including handling DAS and ARR.
     */
    private void softDroppingUpdate() {
        if (softDropping < AUTOMATIC_REPEAT_RATE_FRAMES + DELAYED_AUTO_SHIFT_FRAMES) {
            softDropping++;
        } else {
            tryAndApplyMoveDown();
            softDropping = DELAYED_AUTO_SHIFT_FRAMES;
        }
    }
}