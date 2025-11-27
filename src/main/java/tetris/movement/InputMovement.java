package tetris.movement;

import tetris.MainFrame;
import tetris.movement.rotationSystem.RotationSystem;
import tetris.movement.rotationSystem.SuperRotationSystem;
import tetris.tetrominoes.Tetromino;

public class InputMovement {
    private static final int BETWEEN_MOVEMENT_FRAMES = ((int) MainFrame.FPS) / 30;
    private static final int INITIAL_INPUT_DELAY_FRAMES = ((int) MainFrame.FPS) / 6;

    private CollisionDetector collisionDetector;
    private RotationSystem superRotationSystem;
    private Tetromino fallingTetrimonio;
    private int fastMovingDown;
    private int movingLeft;
    private int movingRight;
    private int dropped;
    private int rotatedLeft;
    private int rotatedRight;
    private int flipped;

    public InputMovement(CollisionDetector collisionDetector, RotationSystem superRotationSystem) {
        this.collisionDetector = collisionDetector;
        this.superRotationSystem = superRotationSystem;
        this.fallingTetrimonio = null;

        fastMovingDown = -1;
        movingLeft = -1;
        movingRight = -1;
        dropped = -1;
        rotatedLeft = -1;
        rotatedRight = -1;
        flipped = -1;
    }

    public void setFallingTetrimonio(Tetromino fallingTetrimonio) {
        this.fallingTetrimonio = fallingTetrimonio;
    }

    public void update() {
        if (dropped == 0) {
            TetrominoMovement.drop(fallingTetrimonio, collisionDetector);

            dropped = 1;
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

                    if (movingLeft < INITIAL_INPUT_DELAY_FRAMES + BETWEEN_MOVEMENT_FRAMES + 1) {
                        movingLeft++;
                    }
                } else {
                    movingLeftUpdate();

                    if (movingRight < INITIAL_INPUT_DELAY_FRAMES + BETWEEN_MOVEMENT_FRAMES + 1) {
                        movingRight++;
                    }
                }
            }
        }

        if (fastMovingDown >= 0) {
            fastMovingDownUpdate();
        }

        if (rotatedRight == 0) {
            superRotationSystem.rotateRight(fallingTetrimonio);
            rotatedRight = 1;
        }

        if (rotatedLeft == 0) {
            superRotationSystem.rotateLeft(fallingTetrimonio);
            rotatedLeft = 1;
        }

        if (flipped == 0) {
            superRotationSystem.flip(fallingTetrimonio);
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

    public void moveDownPressed() {
        if (fastMovingDown < 0) {
            fastMovingDown = 0;
        }
    }

    public void moveDownReleased() {
        fastMovingDown = -1;
    }

    public void dropPressed() {
        if (dropped < 0) {
            dropped = 0;
        }
    }

    public void dropReleased() {
        dropped = -1;
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
        Tetromino aux = fallingTetrimonio.createCopy();

        aux.moveLeft();

        if (!collisionDetector.checkCollision(aux)) {
            fallingTetrimonio.moveLeft();
        }
    }

    private void tryAndApplyMoveRight() {
        Tetromino aux = fallingTetrimonio.createCopy();

        aux.moveRight();

        if (!collisionDetector.checkCollision(aux)) {
            fallingTetrimonio.moveRight();
        }
    }

    private void tryAndApplyMoveDown() {
        Tetromino aux = fallingTetrimonio.createCopy();

        aux.moveDown();

        if (!collisionDetector.checkCollision(aux)) {
            fallingTetrimonio.moveDown();
        }
    }

    private void movingRightUpdate() {
        if (movingRight == 0) {
            tryAndApplyMoveRight();
            movingRight++;
        } else if (movingRight > 0) {
            if (movingRight < INITIAL_INPUT_DELAY_FRAMES + BETWEEN_MOVEMENT_FRAMES) {
                movingRight++;
            } else {
                tryAndApplyMoveRight();
                movingRight = INITIAL_INPUT_DELAY_FRAMES;
            }
        }
    }

    private void movingLeftUpdate() {
        if (movingLeft == 0) {
            tryAndApplyMoveLeft();
            movingLeft++;
        } else if (movingLeft > 0) {
            if (movingLeft < INITIAL_INPUT_DELAY_FRAMES + BETWEEN_MOVEMENT_FRAMES) {
                movingLeft++;
            } else {
                tryAndApplyMoveLeft();
                movingLeft = INITIAL_INPUT_DELAY_FRAMES;
            }
        }
    }

    private void fastMovingDownUpdate() {
        if (fastMovingDown < BETWEEN_MOVEMENT_FRAMES + INITIAL_INPUT_DELAY_FRAMES) {
            fastMovingDown++;
        } else {
            tryAndApplyMoveDown();
            fastMovingDown = INITIAL_INPUT_DELAY_FRAMES;
        }
    }
}
