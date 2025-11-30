package src.tetris.physics;

import src.tetris.physics.rotationSystem.RotationSystem;
import src.tetris.physics.rotationSystem.SuperRotationSystemPlus;
import src.tetris.tetrominoes.Tetromino;

import static src.tetris.Config.INITIAL_GRAVITY;

public class LocalBoardPhysics extends BoardPhysics {
    private RotationSystem rotationSystem;
    private Gravity gravity;
    private InputMovement inputMovement;

    public LocalBoardPhysics(boolean[][] grid) {
        super(grid);
        rotationSystem = new SuperRotationSystemPlus(grid);
        gravity = new Gravity(grid, INITIAL_GRAVITY);
        inputMovement = new InputMovement(grid, rotationSystem, gravity);
    }

    @Override
    public void setFallingTetromino(Tetromino fallingTetromino) {
        super.setFallingTetromino(fallingTetromino);
        gravity.setFallingTetromino(fallingTetromino);
        inputMovement.setFallingTetrimonio(fallingTetromino);
    }

    @Override
    public void update() {
        inputMovement.update(); // IMPORTANT: Has to be before gravity. Soft drop and Hard drop rely on it (otherwise they will be a frame delayed)
        gravity.update();
    }

    @Override
    public boolean isLocked() { return gravity.isLocked(); }

    public boolean thereIsCollision() { return CollisionDetector.checkCollision(grid, fallingTetromino); }
    public void increaseGravity() { gravity.increaseGravity(); }
    public void drop(Tetromino t) {
        int dropCells = 0;
        Tetromino aux = t.createCopy();

        while (!CollisionDetector.checkCollision(grid, aux)) {
            aux.moveDown();
            dropCells += 1;
        }

        if (dropCells > 0) dropCells -= 1;

        t.drop(dropCells);
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
