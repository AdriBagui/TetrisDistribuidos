package tetris.general.boards.physics;

import tetris.general.boards.BoardGrid;
import tetris.general.boards.physics.rotationSystems.RotationSystem;
import tetris.general.tetrominoes.Tetromino;

import static tetris.Config.*;

public class InputMovement {
    private BoardGrid grid;
    private RotationSystem rotationSystem;
    private Gravity gravity;
    private Tetromino fallingTetromino;
    private int softDropping;
    private int movingLeft;
    private int movingRight;
    private int hardDropped;
    private int rotatedLeft;
    private int rotatedRight;
    private int flipped;

    public InputMovement(BoardGrid grid, RotationSystem rotationSystem, Gravity gravity) {
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

    public void setFallingTetromino(Tetromino fallingTetromino) {
        this.fallingTetromino = fallingTetromino;
    }

    public void update() {
        if (hardDropped == 0) {
            gravity.hardDrop();

            hardDropped = 1;
            return;
        }

        if (movingLeft < 0) {
            movingRightUpdate();
        } else {
            if (movingRight < 0) {
                movingLeftUpdate();
            } else {
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

    public void moveLeftPressed() {
        if (movingLeft < 0) {
            movingLeft = 0;

            if (movingRight == 0) { // Make sure if movingRight has been pressed before, moving right is greater than movingLeft
                movingRight++;
            }
        }
    }

    public void moveLeftReleased() {
        movingLeft = -1;
    }

    public void moveRightPressed() {
        if (movingRight < 0) {
            movingRight = 0;

            if (movingLeft == 0) { // Make sure if movingLeft has been pressed before, moving right is greater than movingRight
                movingLeft++;
            }
        }
    }

    public void moveRightReleased() {
        movingRight = -1;
    }

    public void softDropPressed() {
        if (softDropping < 0) {
            softDropping = 0;
        }
    }

    public void softDropReleased() {
        softDropping = -1;
    }

    public void hardDropPressed() {
        if (hardDropped < 0) {
            hardDropped = 0;
        }
    }

    public void hardDropReleased() {
        hardDropped = -1;
    }

    public void rotateRightPressed() {
        if (rotatedRight < 0) {
            rotatedRight = 0;
        }
    }

    public void rotateRightReleased() {
        rotatedRight = -1;
    }

    public void rotateLeftPressed() {
        if (rotatedLeft < 0) {
            rotatedLeft = 0;
        }
    }

    public void rotateLeftReleased() {
        rotatedLeft = -1;
    }

    public void flipPressed() {
        if (flipped < 0) {
            flipped = 0;
        }
    }

    public void flipReleased() {
        flipped = -1;
    }

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

    private void fastMovingDownUpdate() {
        if (softDropping < AUTOMATIC_REPEAT_RATE_FRAMES + DELAYED_AUTO_SHIFT_FRAMES) {
            softDropping++;
        } else {
            tryAndApplyMoveDown();
            softDropping = DELAYED_AUTO_SHIFT_FRAMES;
        }
    }
}
