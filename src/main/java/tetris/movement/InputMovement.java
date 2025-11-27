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
    private int dropped;
    private int rotatedLeft;
    private int rotatedRight;
    private int flipped;

    public InputMovement(CollisionDetector collisionDetector, SuperRotationSystem superRotationSystem) {
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
            while(!collisionDetector.checkCollision(fallingTetrimonio)) {
                moveDown();
            }

            dropped = 1;
            return;
        }

        if (movingLeft < 0 || movingRight < 0) {
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

    public void dropPressed() {
        if (dropped >= 0) return;

        dropped = 0;
    }

    public void dropReleased() {
        dropped = -1;
    }

    public void rotateRightPressed() {
        if (rotatedRight >= 0) return;

        superRotationSystem.rotateRight(fallingTetrimonio);
        rotatedRight = 0;
    }

    public void rotateRightReleased() {
        rotatedRight = -1;
    }

    public void rotateLeftPressed() {
        if (rotatedLeft >= 0) return;

        superRotationSystem.rotateLeft(fallingTetrimonio);
        rotatedLeft = 0;
    }

    public void rotateLeftReleased() {
        rotatedLeft = -1;
    }

    public void flipPressed() {
        if (flipped >= 0) return;

        superRotationSystem.flip(fallingTetrimonio);
        flipped = 0;
    }

    public void flipReleased() {
        flipped = -1;
    }

    private void moveLeft() {
        fallingTetrimonio.moveLeft();

        if (collisionDetector.checkCollision(fallingTetrimonio)) {
            fallingTetrimonio.moveRight();
        }
    }

    private void moveRight() {
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
