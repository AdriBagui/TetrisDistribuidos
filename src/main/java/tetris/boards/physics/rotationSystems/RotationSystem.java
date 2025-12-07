package tetris.boards.physics.rotationSystems;

import tetris.boards.components.BoardGrid;
import tetris.tetrominoes.Tetromino;

public abstract class RotationSystem {
    protected BoardGrid grid;

    public RotationSystem(BoardGrid grid) { this.grid = grid; }

    public abstract void rotateRight(Tetromino tetromino);
    public abstract void rotateLeft(Tetromino tetromino);
    public abstract void flip(Tetromino tetromino);
}
