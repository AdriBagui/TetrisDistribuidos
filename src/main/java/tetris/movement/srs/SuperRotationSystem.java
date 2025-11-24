package tetris.movement.srs;

import tetris.movement.CollisionDetector;
import tetris.tetrominoes.Tetromino;

public abstract class SuperRotationSystem {

    protected CollisionDetector collisionDetector;

    public SuperRotationSystem(CollisionDetector collisionDetector) {
        this.collisionDetector = collisionDetector;
    }

    public abstract void rotateRight(Tetromino t);
    public abstract void rotateLeft(Tetromino t);
    public abstract void flip(Tetromino t);

    protected boolean tryKick(Tetromino t, int originalX, int originalY, int offsetX, int offsetY) {
        t.setXY(originalX + offsetX, originalY + offsetY);
        return !collisionDetector.checkCollision(t);
    }
}
