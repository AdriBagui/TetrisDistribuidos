package tetris.boards.physics.rotationSystems;

import tetris.boards.components.BoardGrid;
import tetris.tetrominoes.Tetromino;
import tetris.tetrominoes.TetrominoType;

/**
 * An enhanced version of the Super Rotation System (SRS+).
 * <p>
 * This class extends standard SRS to implement updated wall kick tables, specifically for the
 * 'I' piece. These modified tables are commonly found in modern web-based Tetris implementations
 * (like TETR.IO) to fix some of the more unintuitive behavior of the original SRS I-kicks.
 * </p>
 */
public class SuperRotationSystemPlus extends SuperRotationSystem {

    // --- UPDATED I-PIECE KICK DATA ---
    // These override the standard SRS tables for the I piece to provide "better" rotation feels.

    /** Updated Clockwise kicks for the I piece. */
    private static final int[][][] I_ROTATE_RIGHT_KICK_DATA = {
            // 0 -> R (Spawn -> Right)
            {{0, 0}, {1, 0}, {-2, 0}, {-2, -1}, {1, 2}},
            // R -> 2 (Right -> 180)
            {{0, 0}, {-1, 0}, {2, 0}, {-1, 2}, {2, -1}},
            // 2 -> L (180 -> Left)
            {{0, 0}, {2, 0}, {-1, 0}, {2, 1}, {-1, -2}},
            // L -> 0 (Left -> Spawn)
            {{0, 0}, {1, 0}, {-2, 0}, {1, -2}, {-2, 1}}
    };

    /** Updated Counter-Clockwise kicks for the I piece. */
    private static final int[][][] I_ROTATE_LEFT_KICK_DATA = {
            // 0 -> L (Spawn -> Left)
            {{0, 0}, {-1, 0}, {2, 0}, {2, -1}, {-1, 2}},
            // R -> 0 (Right -> 0)
            {{0, 0}, {-1, 0}, {2, 0}, {-1, -2}, {2, 1}},
            // 2 -> R (180 -> Right)
            {{0, 0}, {-2, 0}, {1, 0}, {-2, 1}, {1, -2}},
            // L -> 2 (Left -> 180)
            {{0, 0}, {1, 0}, {-2, 0}, {1, 2}, {-2, -1}}
    };

    /**
     * Creates a new SRS+ handler.
     *
     * @param grid The grid used for collision detection.
     */
    public SuperRotationSystemPlus(BoardGrid grid) {
        super(grid);
    }

    /**
     * Rotates the tetromino clockwise using SRS+ rules.
     * <p>
     * Intercepts the I-piece logic to use the updated tables; otherwise delegates to standard SRS.
     * </p>
     *
     * @param tetromino The tetromino to rotate.
     */
    @Override
    public void rotateRight(Tetromino tetromino) {
        boolean success;
        Tetromino aux;

        if (tetromino.getType() != TetrominoType.I) {
            // Use standard SRS for non-I pieces
            super.rotateRight(tetromino);
        } else  {
            // Use updated logic for I pieces
            aux = tetromino.createCopy();
            aux.rotateRight();

            success = tryAndApplyKickData(aux, I_ROTATE_RIGHT_KICK_DATA[tetromino.getRotationIndex()]);

            if (success) {
                tetromino.setXYRotationIndex(aux.getX(), aux.getY(), aux.getRotationIndex());
            }
        }
    }

    /**
     * Rotates the tetromino counter-clockwise using SRS+ rules.
     * <p>
     * Intercepts the I-piece logic to use the updated tables; otherwise delegates to standard SRS.
     * </p>
     *
     * @param tetromino The tetromino to rotate.
     */
    @Override
    public void rotateLeft(Tetromino tetromino) {
        boolean success;
        Tetromino aux;

        if (tetromino.getType() != TetrominoType.I) {
            super.rotateLeft(tetromino);
        } else  {
            aux = tetromino.createCopy();
            aux.rotateLeft();

            success = tryAndApplyKickData(aux, I_ROTATE_LEFT_KICK_DATA[tetromino.getRotationIndex()]);

            if (success) {
                tetromino.setXYRotationIndex(aux.getX(), aux.getY(), aux.getRotationIndex());
            }
        }
    }
}