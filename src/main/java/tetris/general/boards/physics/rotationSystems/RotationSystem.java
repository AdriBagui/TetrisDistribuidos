package tetris.general.boards.physics.rotationSystems;

import tetris.general.boards.BoardGrid;
import tetris.general.tetrominoes.Tetromino;

public abstract class RotationSystem {
    protected BoardGrid grid;

    public RotationSystem(BoardGrid grid) { this.grid = grid; }

    public abstract void rotateRight(Tetromino tetromino);
    public abstract void rotateLeft(Tetromino tetromino);
    public abstract void flip(Tetromino tetromino);
}
