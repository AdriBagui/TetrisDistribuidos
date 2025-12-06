package tetris.general.boards;

import tetris.general.boards.physics.BoardPhysics;
import tetris.general.boards.physics.BoardPhysicsFactory;

import java.awt.*;

public abstract class BoardWithPhysics extends Board {
    // BOARD PHYSICS
    protected final BoardPhysics boardPhysics;

    public BoardWithPhysics(int x, int y, int gridRows, int gridSpawnRows, int gridColumns, int tetrominoesQueueSize, int tetrominoesQueueGeneratorType, long seed, boolean hasHolder, int boardPhysicsType) {
        super(x, y, gridRows, gridSpawnRows, gridColumns, tetrominoesQueueSize, tetrominoesQueueGeneratorType, seed, hasHolder);
        this.boardPhysics = BoardPhysicsFactory.createBoardPhysics(boardPhysicsType, grid);
        fallingTetrominoShadow = null;
    }

    // Board auxiliary methods
    @Override
    protected boolean isFallingTetrominoLocked() { return boardPhysics.isLocked(); }
    @Override
    protected void updateFallingTetromino() { boardPhysics.update(); }
    @Override
    protected void setNextTetrominoAsFallingTetromino() {
        super.setNextTetrominoAsFallingTetromino();
        boardPhysics.setFallingTetromino(fallingTetromino);
    }
    @Override
    protected void drawFallingTetromino(Graphics2D g2) {
        fallingTetrominoShadow.draw(g2);
        super.drawFallingTetromino(g2);
    }

    // Controls
    public void moveLeftPressed() { boardPhysics.moveLeftPressed(); }
    public void moveLeftReleased() { boardPhysics.moveLeftReleased(); }
    public void moveRightPressed() { boardPhysics.moveRightPressed(); }
    public void moveRightReleased() { boardPhysics.moveRightReleased(); }
    public void softDropPressed() { boardPhysics.moveDownPressed(); }
    public void softDropReleased() { boardPhysics.moveDownReleased(); }
    public void hardDropPressed() { boardPhysics.dropPressed(); }
    public void hardDropReleased() { boardPhysics.dropReleased(); }
    public void rotateRightPressed() { boardPhysics.rotateRightPressed(); }
    public void rotateRightReleased() { boardPhysics.rotateRightReleased(); }
    public void rotateLeftPressed() { boardPhysics.rotateLeftPressed(); }
    public void rotateLeftReleased() { boardPhysics.rotateLeftReleased(); }
    public void flipPressed() { boardPhysics.flipPressed(); }
    public void flipReleased() { boardPhysics.flipReleased(); }
}
