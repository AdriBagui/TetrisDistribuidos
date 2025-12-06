package tetris.nes.boards.physics.rotationSystems;

import tetris.general.boards.BoardGrid;
import tetris.general.boards.physics.rotationSystems.RotationSystem;
import tetris.general.tetrominoes.Tetromino;

import static tetris.Config.*;

public class NESRotationSystem extends RotationSystem {
    public NESRotationSystem(BoardGrid grid) { super(grid); }

    /**
     * Rotates right using the classic tetris logic
     * @param tetromino The tetromino which gets rotated to de right
     */
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

        if (!grid.hasCollision(aux)) {
            tetromino.setXYRotationIndex(aux.getX(), aux.getY(), aux.getRotationIndex());
        }
    }

    /**
     * Rotates left using the classic tetris logic
     * @param tetromino The tetromino which gets rotated to the left
     */
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

        if (!grid.hasCollision(aux)) {
            tetromino.setXYRotationIndex(aux.getX(), aux.getY(), aux.getRotationIndex());
        }
    }

    /**
     * Do nothing (it's not included in classic tetris)
     * @param tetromino The tetromino which gets rotated (do nothing)
     */
    @Override
    public void flip(Tetromino tetromino) {
        // Do nothing
    }
}
