package tetris.general.boards.physics;

import tetris.general.boards.BoardGrid;
import tetris.general.boards.physics.rotationSystems.RotationSystem;
import tetris.general.tetrominoes.Tetromino;

public abstract class BoardPhysics {
    protected BoardGrid grid;
    protected Tetromino fallingTetromino;

    protected RotationSystem rotationSystem;
    protected Gravity gravity;
    protected InputMovement inputMovement;

    public BoardPhysics(BoardGrid grid, RotationSystem rotationSystem, double initialGravity) {
        this.grid = grid;
        this.rotationSystem = rotationSystem;
        gravity = new Gravity(grid, initialGravity);
        inputMovement = new InputMovement(grid, rotationSystem, gravity);
    }

    public void setFallingTetromino(Tetromino fallingTetromino) {
        this.fallingTetromino = fallingTetromino;
        gravity.setFallingTetromino(fallingTetromino);
        inputMovement.setFallingTetromino(fallingTetromino);
    }

    public void update() {
        inputMovement.update(); // IMPORTANT: Has to be before gravity. Soft drop and Hard drop rely on it (otherwise they will be a frame delayed)
        gravity.update();
    }

    public boolean isLocked() { return gravity.isLocked(); }

    public void increaseGravity() { gravity.increaseGravity(); }

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
