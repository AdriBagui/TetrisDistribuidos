package tetris.general.boards.physics;

import tetris.general.boards.BoardGrid;
import tetris.general.tetrominoes.Tetromino;

public abstract class BoardPhysics {
    protected BoardGrid grid;
    protected Tetromino fallingTetromino;

    public BoardPhysics(BoardGrid grid) {
        this.grid = grid;
    }

    public abstract void update();
    public abstract boolean isLocked();

    public void setFallingTetromino(Tetromino fallingTetromino) {
        this.fallingTetromino = fallingTetromino;
    }
}
