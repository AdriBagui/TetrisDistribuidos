package tetris.movement.rotationSystem;

import tetris.movement.CollisionDetector;
import tetris.tetrominoes.Tetromino;

public abstract class RotationSystem {
    protected CollisionDetector collisionDetector;

    public RotationSystem(CollisionDetector collisionDetector) {
        this.collisionDetector = collisionDetector;
    }

    public abstract void rotateRight(Tetromino tetromino);
    public abstract void rotateLeft(Tetromino tetromino);
    public abstract void flip(Tetromino tetromino);
}
