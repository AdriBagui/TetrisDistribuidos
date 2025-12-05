package tetris.boards;

import tetris.physics.BoardPhysics;
import tetris.physics.LocalBoardPhysics;
import tetris.tetrominoes.TetrominoShadow;

import java.awt.*;
import java.util.Random;

public abstract class PhysicsCalculatingBoard extends Board {
    // BOARD LOGIC
    protected LocalBoardPhysics localBoardPhysics;
    // TETROMINO SHADOW
    protected TetrominoShadow fallingTetrominoShadow;
    // GARBAGE SYSTEM
    protected Random garbageRandom;

    public PhysicsCalculatingBoard(int x, int y, long seed) {
        super(x, y, seed);
        localBoardPhysics = (LocalBoardPhysics) boardPhysics;

        fallingTetrominoShadow = null;

        garbageRandom = new Random();
    }

    @Override
    protected BoardPhysics initializeBoardPhysics() { return new LocalBoardPhysics(grid); }

    @Override
    protected void setNextTetrominoAsFallingTetromino() {
        super.setNextTetrominoAsFallingTetromino();
        fallingTetrominoShadow = new TetrominoShadow(fallingTetromino);
    }

    @Override
    protected void updateFallingTetromino() {
        super.updateFallingTetromino();
        updateFallingTetrominoShadow();
    }

    @Override
    protected void drawFallingTetromino(Graphics2D g2) {
        fallingTetrominoShadow.draw(g2);
        super.drawFallingTetromino(g2);
    }

    // Controls
    public void moveLeftPressed() { localBoardPhysics.moveLeftPressed(); }
    public void moveLeftReleased() { localBoardPhysics.moveLeftReleased(); }
    public void moveRightPressed() { localBoardPhysics.moveRightPressed(); }
    public void moveRightReleased() { localBoardPhysics.moveRightReleased(); }
    public void softDropPressed() { localBoardPhysics.moveDownPressed(); }
    public void softDropReleased() { localBoardPhysics.moveDownReleased(); }
    public void hardDropPressed() { localBoardPhysics.dropPressed(); }
    public void hardDropReleased() { localBoardPhysics.dropReleased(); }
    public void rotateRightPressed() { localBoardPhysics.rotateRightPressed(); }
    public void rotateRightReleased() { localBoardPhysics.rotateRightReleased(); }
    public void rotateLeftPressed() { localBoardPhysics.rotateLeftPressed(); }
    public void rotateLeftReleased() { localBoardPhysics.rotateLeftReleased(); }
    public void flipPressed() { localBoardPhysics.flipPressed(); }
    public void flipReleased() { localBoardPhysics.flipReleased(); }

    private void updateFallingTetrominoShadow() {
        fallingTetrominoShadow.setXYRotationIndex(fallingTetromino.getX(), fallingTetromino.getY(), fallingTetromino.getRotationIndex());
        localBoardPhysics.drop(fallingTetrominoShadow);
    }
}
