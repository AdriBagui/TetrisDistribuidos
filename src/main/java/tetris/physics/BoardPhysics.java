package tetris.physics;

import tetris.tetrominoes.Tetromino;

public abstract class BoardPhysics {
    protected boolean[][] grid;
    protected Tetromino fallingTetromino;

    public BoardPhysics(boolean[][] grid) {
        this.grid = grid;
    }

    public void setFallingTetromino(Tetromino fallingTetromino) {
        this.fallingTetromino = fallingTetromino;
    }

    public abstract void update();
    public abstract boolean isLocked();
}
