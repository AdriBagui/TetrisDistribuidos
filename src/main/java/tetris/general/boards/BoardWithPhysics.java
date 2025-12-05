package tetris.general.boards;

import tetris.general.boards.physics.BoardPhysics;

import java.awt.*;

public abstract class BoardWithPhysics extends Board {
    // BOARD PHYSICS
    protected final BoardPhysics boardPhysics;

    public BoardWithPhysics(int x, int y, BoardGrid grid, long seed, BoardPhysics boardPhysics) {
        super(x, y, grid, seed);
        this.boardPhysics = boardPhysics;
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
    public abstract void moveLeftPressed();
    public abstract void moveLeftReleased();
    public abstract void moveRightPressed();
    public abstract void moveRightReleased();
    public abstract void softDropPressed();
    public abstract void softDropReleased();
    public abstract void hardDropPressed();
    public abstract void hardDropReleased();
    public abstract void rotateRightPressed();
    public abstract void rotateRightReleased();
    public abstract void rotateLeftPressed();
    public abstract void rotateLeftReleased();
    public abstract void flipPressed();
    public abstract void flipReleased();
}
