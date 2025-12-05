package tetris.general.boards;

import tetris.general.boards.physics.BoardPhysics;
import tetris.tetrio.boards.physics.TetrioBoardPhysics;
import tetris.general.tetrominoes.TetrominoShadow;

import java.awt.*;

public abstract class BoardWithPhysics extends Board {
    // TETROMINO SHADOW
    protected TetrominoShadow fallingTetrominoShadow;
    // BOARD PHYSICS
    protected final BoardPhysics boardPhysics;

    public BoardWithPhysics(int x, int y, BoardGrid grid, long seed, BoardPhysics boardPhysics) {
        super(x, y, grid, seed);
        this.boardPhysics = boardPhysics;
        fallingTetrominoShadow = null;
    }

    @Override
    public void update() {
        super.update();
        if (boardPhysics.isLocked()) { lockTetromino(); }
    }

    @Override
    protected void setNextTetrominoAsFallingTetromino() {
        super.setNextTetrominoAsFallingTetromino();
        boardPhysics.setFallingTetromino(fallingTetromino);
        fallingTetrominoShadow = new TetrominoShadow(fallingTetromino);
    }

    @Override
    protected void updateFallingTetromino() {
        boardPhysics.update();
        updateFallingTetrominoShadow();
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

    private void updateFallingTetrominoShadow() {
        fallingTetrominoShadow.setXYRotationIndex(fallingTetromino.getX(), fallingTetromino.getY(), fallingTetromino.getRotationIndex());
        boardPhysics.drop(fallingTetrominoShadow);
    }
}
