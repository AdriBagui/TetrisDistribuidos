package tetris.tetrio.boards.physics;

import tetris.general.boards.BoardGrid;
import tetris.general.boards.physics.BoardPhysics;
import tetris.general.boards.physics.Gravity;
import tetris.general.boards.physics.InputMovement;
import tetris.general.boards.physics.rotationSystems.RotationSystem;
import tetris.tetrio.boards.physics.rotationSystems.SuperRotationSystemPlus;
import tetris.general.tetrominoes.Tetromino;

import static tetris.Config.INITIAL_GRAVITY;

public class TetrioBoardPhysics extends BoardPhysics {
    private RotationSystem rotationSystem;
    private Gravity gravity;
    private InputMovement inputMovement;

    public TetrioBoardPhysics(BoardGrid grid) {
        super(grid);
        rotationSystem = new SuperRotationSystemPlus(grid);
        gravity = new Gravity(grid, INITIAL_GRAVITY);
        inputMovement = new InputMovement(grid, rotationSystem, gravity);
    }

    @Override
    public void setFallingTetromino(Tetromino fallingTetromino) {
        super.setFallingTetromino(fallingTetromino);
        gravity.setFallingTetromino(fallingTetromino);
        inputMovement.setFallingTetromino(fallingTetromino);
    }

    @Override
    public void update() {
        inputMovement.update(); // IMPORTANT: Has to be before gravity. Soft drop and Hard drop rely on it (otherwise they will be a frame delayed)
        gravity.update();
    }

    @Override
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
