package tetris.physics;

import tetris.boards.BoardGrid;
import tetris.tetrominoes.Tetromino;

public abstract class BoardPhysics {
    protected BoardGrid grid;
    protected Tetromino fallingTetromino;

    public BoardPhysics(BoardGrid grid) {
        this.grid = grid;
    }

    public void setFallingTetromino(Tetromino fallingTetromino) {
        this.fallingTetromino = fallingTetromino;
    }

    public abstract void update();
    public abstract boolean isLocked();
}
