package tetris.movement;

import tetris.movement.srs.SuperRotationSystem;
import tetris.movement.srs.SuperRotationSystemPlus;
import tetris.tetrominoes.Tetromino;

public class TetrominoMovement {
    private CollisionDetector collisionDetector;
    private SuperRotationSystem superRotationSystem;
    private Gravity gravity;
    private InputMovement inputMovement;
    private Tetromino fallingTetromino;

    public TetrominoMovement(boolean[][] grid) {
        collisionDetector = new CollisionDetector(grid);
        superRotationSystem = new SuperRotationSystemPlus(collisionDetector);
        gravity = new Gravity(grid, collisionDetector, 0.05);
        inputMovement = new InputMovement(collisionDetector, superRotationSystem);
    }

    public void setFallingTetromino(Tetromino fallingTetromino) {
        this.fallingTetromino = fallingTetromino;
        gravity.setFallingTetromino(fallingTetromino);
        inputMovement.setFallingTetrimonio(fallingTetromino);
    }

    public void update() {
        inputMovement.update();
        gravity.update();
    }

    public boolean thereIsCollision() { return collisionDetector.checkCollision(fallingTetromino); }
    public boolean hasCollision(Tetromino t) { return collisionDetector.checkCollision(t); }
    public boolean locked() { return gravity.locked(); }
    public void increaseGravity() { gravity.increaseGravity(); }

    // Controls
    public void moveLeftPressed() { inputMovement.moveLeftPressed(); }
    public void moveLeftReleased() { inputMovement.moveLeftReleased(); }
    public void moveRightPressed() { inputMovement.moveRightPressed(); }
    public void moveRightReleased() { inputMovement.moveRightReleased(); }
    public void moveDownPressed() { inputMovement.moveDownPressed(); }
    public void moveDownReleased() { inputMovement.moveDownReleased(); }
    public void dropPressed() { inputMovement.dropPressed(); }
    public void dropReleased() { inputMovement.dropReleased(); }
    public void rotateRightPressed() { inputMovement.rotateRightPressed(); }
    public void rotateRightReleased() { inputMovement.rotateRightReleased(); }
    public void rotateLeftPressed() { inputMovement.rotateLeftPressed(); }
    public void rotateLeftReleased() { inputMovement.rotateLeftReleased(); }
    public void flipPressed() { inputMovement.flipPressed(); }
    public void flipReleased() { inputMovement.flipReleased(); }
}
