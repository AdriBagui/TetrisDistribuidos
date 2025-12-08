package tetris.boards.physics.rotationSystems;

import tetris.boards.components.BoardGrid;
import tetris.tetrominoes.Tetromino;
import tetris.tetrominoes.TetrominoType;

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

        if (tetromino.getType() == TetrominoType.I || tetromino.getType() == TetrominoType.S || tetromino.getType() == TetrominoType.Z) {
            aux.setRotationIndex(aux.getRotationIndex() % 2);
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

        if (tetromino.getType() == TetrominoType.I || tetromino.getType() == TetrominoType.S || tetromino.getType() == TetrominoType.Z) {
            aux.setRotationIndex(aux.getRotationIndex() % 2);
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
