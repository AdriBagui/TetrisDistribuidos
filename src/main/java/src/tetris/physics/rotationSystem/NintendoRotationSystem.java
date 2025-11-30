package src.tetris.physics.rotationSystem;

import src.tetris.physics.CollisionDetector;
import src.tetris.tetrominoes.Tetromino;

import static src.tetris.Config.*;

public class NintendoRotationSystem extends RotationSystem {
    public NintendoRotationSystem(boolean[][] grid) { super(grid); }

    @Override
    public void rotateRight(Tetromino tetromino) {
        boolean success = false;
        Tetromino aux = tetromino.createCopy();
        aux.rotateRight();

        if (tetromino.getType() == I || tetromino.getType() == S || tetromino.getType() == Z) {
            switch (tetromino.getRotationIndex()) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    aux.moveRight();
                    break;
                case 3:
                    aux.moveDown();
                    break;
            }
        }

        if (!CollisionDetector.checkCollision(grid, aux)) {
            tetromino.setXYRotationIndex(aux.getX(), aux.getY(), aux.getRotationIndex());
        }
    }

    @Override
    public void rotateLeft(Tetromino tetromino) {
        boolean success = false;
        Tetromino aux = tetromino.createCopy();
        aux.rotateLeft();

        if (tetromino.getType() == I || tetromino.getType() == S || tetromino.getType() == Z) {
            switch (tetromino.getRotationIndex()) {
                case 0:
                    aux.moveRight();
                    break;
                case 1:
                    aux.moveDown();
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        }

        if (!CollisionDetector.checkCollision(grid, aux)) {
            tetromino.setXYRotationIndex(aux.getX(), aux.getY(), aux.getRotationIndex());
        }
    }

    @Override
    public void flip(Tetromino tetromino) {
        // Do nothing
    }
}
