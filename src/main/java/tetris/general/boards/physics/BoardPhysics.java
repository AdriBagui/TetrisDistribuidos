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

    public void drop(Tetromino t) {
        int dropCells = 0;
        Tetromino aux = t.createCopy();

        while (!grid.hasCollision(aux)) {
            aux.moveDown();
            dropCells += 1;
        }

        if (dropCells > 0) dropCells -= 1;

        t.drop(dropCells);
    }
}
