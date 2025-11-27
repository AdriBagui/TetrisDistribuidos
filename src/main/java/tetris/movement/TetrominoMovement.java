package tetris.movement;

import tetris.MainFrame;
import tetris.movement.rotationSystem.RotationSystem;
import tetris.movement.rotationSystem.SuperRotationSystemPlus;
import tetris.tetrominoes.Tetromino;

import static tetris.Config.*;

public class TetrominoMovement {
    private CollisionDetector collisionDetector;
    private RotationSystem rotationSystem;
    private Gravity gravity;
    private InputMovement inputMovement;
    private Tetromino fallingTetromino;

    public TetrominoMovement(boolean[][] grid) {
        collisionDetector = new CollisionDetector(grid);
        rotationSystem = new SuperRotationSystemPlus(collisionDetector);
        gravity = new Gravity(collisionDetector, INITIAL_GRAVITY);
        inputMovement = new InputMovement(collisionDetector, rotationSystem, gravity);
    }

    public void setFallingTetromino(Tetromino fallingTetromino) {
        this.fallingTetromino = fallingTetromino;
        gravity.setFallingTetromino(fallingTetromino);
        inputMovement.setFallingTetrimonio(fallingTetromino);
    }

    public void update() {
        inputMovement.update(); // IMPORTANT: Has to be before gravity. Soft drop and Hard drop rely on it (otherwise they will be a frame delayed)
        gravity.update();
    }

    public boolean thereIsCollision() { return collisionDetector.checkCollision(fallingTetromino); }
    public boolean hasCollision(Tetromino t) { return collisionDetector.checkCollision(t); }
    public boolean isLocked() { return gravity.isLocked(); }
    public void increaseGravity() { gravity.increaseGravity(); }
    public void drop(Tetromino t) {
        int dropCells = 0;
        Tetromino aux = t.createCopy();

        while (!collisionDetector.checkCollision(aux)) {
            aux.moveDown();
            dropCells += 1;
        }

        t.drop(dropCells-1);
    }
    public static void drop(Tetromino t, CollisionDetector collisionDetector) {
        int dropCells = 0;
        Tetromino aux = t.createCopy();

        while (!collisionDetector.checkCollision(aux)) {
            aux.moveDown();
            dropCells += 1;
        }

        t.drop(dropCells-1);
    }

    // Controls
    public void moveLeftPressed() { inputMovement.moveLeftPressed(); }
    public void moveLeftReleased() { inputMovement.moveLeftReleased(); }
    public void moveRightPressed() { inputMovement.moveRightPressed(); }
    public void moveRightReleased() { inputMovement.moveRightReleased(); }
    public void moveDownPressed() { inputMovement.softDropPressed(); }
    public void moveDownReleased() { inputMovement.softDropReleased(); }
    public void dropPressed() { inputMovement.hardDropPressed(); }
    public void dropReleased() { inputMovement.hardDropReleased(); }
    public void rotateRightPressed() { inputMovement.rotateRightPressed(); }
    public void rotateRightReleased() { inputMovement.rotateRightReleased(); }
    public void rotateLeftPressed() { inputMovement.rotateLeftPressed(); }
    public void rotateLeftReleased() { inputMovement.rotateLeftReleased(); }
    public void flipPressed() { inputMovement.flipPressed(); }
    public void flipReleased() { inputMovement.flipReleased(); }
}
