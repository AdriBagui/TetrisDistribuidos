package tetris.boards.physics.rotationSystems;

import tetris.boards.components.BoardGrid;
import tetris.tetrominoes.Tetromino;
import tetris.tetrominoes.TetrominoType;

/**
 * Implements the Nintendo Rotation System (NRS) used in the classic NES Tetris.
 * <p>
 * This system is characterized by its strict rotation rules. Unlike modern systems,
 * it does not support standard wall kicks (pieces generally cannot rotate if obstructed).
 * It also treats the rotation of I, S, and Z pieces as a toggle between two states
 * rather than four unique orientations.
 * </p>
 */
public class NESRotationSystem extends RotationSystem {

    /**
     * Creates a new NES rotation system handler.
     *
     * @param grid The grid used for collision detection.
     */
    public NESRotationSystem(BoardGrid grid) { super(grid); }

    /**
     * Rotates the tetromino 90 degrees clockwise using classic NES logic.
     * <p>
     * For I, S, and Z pieces, this effectively toggles between two rotation states.
     * No wall kicks are applied; if the new position collides, the rotation fails.
     * </p>
     *
     * @param tetromino The tetromino to rotate.
     */
    @Override
    public void rotateRight(Tetromino tetromino) {
        Tetromino aux = tetromino.createCopy();
        aux.rotateRight();

        // I, S, and Z pieces in NES Tetris only have 2 visual states.
        // We enforce this by modulo 2 on the rotation index.
        if (tetromino.getType() == TetrominoType.I || tetromino.getType() == TetrominoType.S || tetromino.getType() == TetrominoType.Z) {
            aux.setRotationIndex(aux.getRotationIndex() % 2);
        }

        if (!grid.hasCollision(aux)) {
            tetromino.setXYRotationIndex(aux.getX(), aux.getY(), aux.getRotationIndex());
        }
    }

    /**
     * Rotates the tetromino 90 degrees counter-clockwise using classic NES logic.
     * <p>
     * Similar to {@link #rotateRight(Tetromino)}, this toggles state for 2-state pieces
     * and performs a strict collision check.
     * </p>
     *
     * @param tetromino The tetromino to rotate.
     */
    @Override
    public void rotateLeft(Tetromino tetromino) {
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
     * Performs no action.
     * <p>
     * The NES version of Tetris does not support 180-degree flips.
     * </p>
     *
     * @param tetromino The tetromino to flip (ignored).
     */
    @Override
    public void flip(Tetromino tetromino) {
        // Do nothing (feature not available in NES Tetris)
    }
}