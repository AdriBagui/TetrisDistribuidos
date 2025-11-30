package src.tetris.physics.rotationSystem;

import src.tetris.physics.CollisionDetector;
import src.tetris.tetrominoes.Tetromino;

public abstract class RotationSystem {
    protected boolean[][] grid;

    public RotationSystem(boolean[][] grid) { this.grid = grid; }

    public abstract void rotateRight(Tetromino tetromino);
    public abstract void rotateLeft(Tetromino tetromino);
    public abstract void flip(Tetromino tetromino);
}
