package tetris.movement;

import tetris.movement.srs.SuperRotationSystem;
import tetris.tetrominoes.Tetromino;

public class InputMovement {
    private static final int INPUT_SKIPPED_FRAMES = 2;
    private static final int INPUT_DELAY = 10;

    private CollisionDetector collisionDetector;
    private SuperRotationSystem superRotationSystem;
    private Tetromino fallingTetrimonio;
    private int fastMovingDown;
    private int movingLeft;
    private int movingRight;
    private boolean rotatedLeft;
    private boolean rotatedRight;
    private boolean flipped;

    public InputMovement(CollisionDetector collisionDetector, SuperRotationSystem superRotationSystem) {
        this.collisionDetector = collisionDetector;
        this.superRotationSystem = superRotationSystem;
        this.fallingTetrimonio = null;

        fastMovingDown = -1;
        movingLeft = -1;
        movingRight = -1;
        rotatedLeft = false;
        rotatedRight = false;
        flipped = false;
    }

    public void setFallingTetrimonio(Tetromino fallingTetrimonio) {
        this.fallingTetrimonio = fallingTetrimonio;
    }

    public void update() {
        if (movingLeft <= 0 || movingRight <= 0) {
            if (movingLeft >= 0) {
                if (movingLeft < INPUT_SKIPPED_FRAMES + INPUT_DELAY) {
                    movingLeft++;
                } else {
                    moveLeft();
                    movingLeft -= INPUT_SKIPPED_FRAMES;
                }
            }

            if (movingRight >= 0) {
                if (movingRight < INPUT_SKIPPED_FRAMES + INPUT_DELAY) {
                    movingRight++;
                } else {
                    moveRight();
                    movingRight -= INPUT_SKIPPED_FRAMES;
                }
            }
        } else {
            movingRight = INPUT_DELAY/2;
            movingLeft = INPUT_DELAY/2;
        }

        if (fastMovingDown >= 0) {
            if (fastMovingDown < INPUT_SKIPPED_FRAMES + INPUT_DELAY) {
                fastMovingDown++;
            } else {
                moveDown();
                fastMovingDown -= INPUT_SKIPPED_FRAMES;
            }
        }
    }

    public void moveLeftPressed() {
        if (movingLeft == -1) {
            moveLeft();
            movingLeft = 0;
        }
    }

    public void moveLeftReleased() {
        movingLeft = -1;
    }

    public void moveRightPressed() {
        if (movingRight == -1) {
            moveRight();
            movingRight = 0;
        }
    }

    public void moveRightReleased() {
        movingRight = -1;
    }

    public void moveDownPressed() {
        if (fastMovingDown == -1) {
            moveDown();
            fastMovingDown = 0;
        }
    }

    public void moveDownReleased() {
        fastMovingDown = -1;
    }

    public void rotateRightPressed() {
        if (rotatedRight) return;

        superRotationSystem.rotateRight(fallingTetrimonio);
        rotatedRight = true;
    }

    public void rotateRightReleased() {
        rotatedRight = false;
    }

    public void rotateLeftPressed() {
        if (rotatedLeft) return;

        superRotationSystem.rotateLeft(fallingTetrimonio);
        rotatedLeft = true;
    }

    public void rotateLeftReleased() {
        rotatedLeft = false;
    }

    public void flipPressed() {
        if (flipped) return;

        superRotationSystem.flip(fallingTetrimonio);
        flipped = true;
    }

    public void flipReleased() {
        flipped = false;
    }

    private void moveLeft() {
        if (movingRight >= 0) return;

        fallingTetrimonio.moveLeft();

        if (collisionDetector.checkCollision(fallingTetrimonio)) {
            fallingTetrimonio.moveRight();
        }
    }

    private void moveRight() {
        if (movingLeft >= 0) return;

        fallingTetrimonio.moveRight();

        if (collisionDetector.checkCollision(fallingTetrimonio)) {
            fallingTetrimonio.moveLeft();
        }
    }

    private void moveDown() {
        fallingTetrimonio.moveDown();

        if (collisionDetector.checkCollision(fallingTetrimonio)) {
            fallingTetrimonio.moveUp();
        }
    }
}
