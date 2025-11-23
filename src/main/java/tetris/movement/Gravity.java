package tetris.movement;

import tetris.tetrominoes.Tetromino;

public class Gravity {
    private boolean[][] grid;
    private Tetromino fallingTetromino;
    private double decimalYPart;
    private CollisionDetector collisionDetector;
    private double gravity; // cells per frame
    private double extraInputGravity;

    public Gravity(boolean[][] grid, Tetromino fallingTetromino, CollisionDetector collisionDetector, double gravity) {
        this.grid = grid;
        this.fallingTetromino = fallingTetromino;
        decimalYPart = 0;
        this.collisionDetector = collisionDetector;
        this.gravity = gravity;
    }

    public void update() {
        decimalYPart += gravity;

        while (decimalYPart > 1) {
            moveDown();
            decimalYPart -= 1;
        }
    }

    public void setFallingTetromino(Tetromino t) {
        fallingTetromino = t;
        decimalYPart = 0;
    }

    private void moveDown() {
        fallingTetromino.moveDown();

        if (collisionDetector.checkCollision(fallingTetromino)) {
            fallingTetromino.moveUp();
        }
    }
}
