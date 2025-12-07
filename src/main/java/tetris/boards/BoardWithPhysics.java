package tetris.boards;

import tetris.boards.physics.Gravity;
import tetris.boards.physics.PlayerInputHandler;
import tetris.boards.physics.rotationSystems.RotationSystem;
import tetris.boards.physics.rotationSystems.RotationSystemFactory;
import tetris.boards.physics.rotationSystems.RotationSystemType;
import tetris.tetrominoes.generators.TetrominoesGeneratorType;

import java.awt.*;

public abstract class BoardWithPhysics extends Board {
    // BOARD PHYSICS
    protected RotationSystem rotationSystem;
    protected Gravity gravity;
    protected PlayerInputHandler playerInputHandler;

    public BoardWithPhysics(int x, int y, int gridRows, int gridSpawnRows, int gridColumns, int tetrominoesQueueSize,
                            TetrominoesGeneratorType tetrominoesQueueGeneratorType, long seed, RotationSystemType rotationSystemType,
                            double initialGravity, int lockDelayFrames) {
        super(x, y, gridRows, gridSpawnRows, gridColumns, tetrominoesQueueSize, tetrominoesQueueGeneratorType, seed);

        rotationSystem = RotationSystemFactory.createRotationSystem(rotationSystemType, grid);
        gravity = new Gravity(grid, initialGravity, lockDelayFrames);
        playerInputHandler = new PlayerInputHandler(grid, rotationSystem, gravity);
    }

    // Board auxiliary methods
    @Override
    protected boolean isFallingTetrominoLocked() { return gravity.isLocked(); }
    @Override
    protected void updateFallingTetromino() {
        playerInputHandler.update(); // IMPORTANT: Has to be before gravity. Soft drop and Hard drop rely on it (otherwise they will be a frame delayed)
        gravity.update();
    }
    @Override
    protected void setNextTetrominoAsFallingTetromino() {
        super.setNextTetrominoAsFallingTetromino();
        gravity.setFallingTetromino(fallingTetromino);
        playerInputHandler.setFallingTetromino(fallingTetromino);
    }
    @Override
    protected void drawFallingTetromino(Graphics2D g2) {
        fallingTetrominoShadow.draw(g2);
        super.drawFallingTetromino(g2);
    }

    // Controls
    public void moveLeftPressed() { playerInputHandler.moveLeftPressed(); }
    public void moveLeftReleased() { playerInputHandler.moveLeftReleased(); }
    public void moveRightPressed() { playerInputHandler.moveRightPressed(); }
    public void moveRightReleased() { playerInputHandler.moveRightReleased(); }
    public void softDropPressed() { playerInputHandler.softDropPressed(); }
    public void softDropReleased() { playerInputHandler.softDropReleased(); }
    public void hardDropPressed() { playerInputHandler.hardDropPressed(); }
    public void hardDropReleased() { playerInputHandler.hardDropReleased(); }
    public void rotateRightPressed() { playerInputHandler.rotateRightPressed(); }
    public void rotateRightReleased() { playerInputHandler.rotateRightReleased(); }
    public void rotateLeftPressed() { playerInputHandler.rotateLeftPressed(); }
    public void rotateLeftReleased() { playerInputHandler.rotateLeftReleased(); }
    public void flipPressed() { playerInputHandler.flipPressed(); }
    public void flipReleased() { playerInputHandler.flipReleased(); }
}
