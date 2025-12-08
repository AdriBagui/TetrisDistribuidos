package tetris.boards.physics.rotationSystems;

import tetris.boards.components.BoardGrid;
import tetris.tetrominoes.Tetromino;
import tetris.tetrominoes.TetrominoType;

/**
 * Implements the Super Rotation System (SRS), the current standard for modern Tetris games.
 * <p>
 * SRS is known for its "Wall Kick" capability, allowing pieces to rotate even when visually obstructed
 * by "kicking" them to an adjacent free space. It relies on lookup tables to determine
 * the sequence of offset tests to attempt for each rotation.
 * </p>
 */
public class SuperRotationSystem extends RotationSystem {
    // --- WALL KICK DATA TABLES ---
    // These tables define the (x, y) offsets to test when a rotation fails.
    // Format: [Rotation State][Test Case][x, y]

    /** Kick data for J, L, S, T, Z pieces (Clockwise). */
    private static final int[][][] ROTATE_RIGHT_KICK_DATA = {
            // 0 -> R
            {{0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2}},
            // R -> 2
            {{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}},
            // 2 -> L
            {{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}},
            // L -> 0
            {{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}}
    };

    /** Kick data for J, L, S, T, Z pieces (Counter-Clockwise). */
    private static final int[][][] ROTATE_LEFT_KICK_DATA = {
            // 0 -> L
            {{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}},
            // R -> 0
            {{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}},
            // 2 -> R
            {{0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2}},
            // L -> 2
            {{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}}
    };

    /** Kick data specifically for the I piece (Clockwise). I pieces have different pivot rules. */
    private static final int[][][] I_ROTATE_RIGHT_KICK_DATA = {
            // 0 -> R
            {{0, 0}, {-2, 0}, {1, 0}, {-2, -1}, {1, 2}},
            // R -> 2
            {{0, 0}, {-1, 0}, {2, 0}, {-1, 2}, {2, -1}},
            // 2 -> L
            {{0, 0}, {2, 0}, {-1, 0}, {2, 1}, {-1, -2}},
            // L -> 0
            {{0, 0}, {1, 0}, {-2, 0}, {1, -2}, {-2, 1}}
    };

    /** Kick data specifically for the I piece (Counter-Clockwise). */
    private static final int[][][] I_ROTATE_LEFT_KICK_DATA = {
            // 0 -> L
            {{0, 0}, {-1, 0}, {2, 0}, {-1, 2}, {2, -1}},
            // R -> 0
            {{0, 0}, {2, 0}, {-1, 0}, {2, 1}, {-1, -2}},
            // 2 -> R
            {{0, 0}, {1, 0}, {-2, 0}, {1, -2}, {-2, 1}},
            // L -> 2
            {{0, 0}, {-2, 0}, {1, 0}, {-2, -1}, {1, 2}}
    };

    /** Kick data for 180-degree flips (Generic). */
    private static final int[][][] FLIP_KICK_DATA = {
            // 0 -> 2
            {{0, 0}, {0, 1}, {1, 1}, {-1, 1}, {1, 0}, {-1, 0}},
            // 1 -> 3
            {{0, 0}, {1, 0}, {1, 2}, {1, 1}, {0, 2}, {0, 1}},
            // 2 -> 0
            {{0, 0}, {0, -1}, {-1, -1}, {1, -1}, {-1, 0}, {1, 0}},
            // 3 -> 1
            {{0, 0}, {-1, 0}, {-1, 2}, {-1, 1}, {0, 2}, {0, 1}}
    };

    /**
     * Creates a new SRS handler.
     *
     * @param grid The grid used for collision detection.
     */
    public SuperRotationSystem(BoardGrid grid) {
        super(grid);
    }

    /**
     * Rotates the tetromino 90 degrees clockwise, applying SRS wall kicks if necessary.
     *
     * @param tetromino The tetromino to rotate.
     */
    @Override
    public void rotateRight(Tetromino tetromino) {
        boolean success = false;
        Tetromino aux = tetromino.createCopy();
        aux.rotateRight();

        // Attempt rotation with appropriate kick table
        if (tetromino.getType() == TetrominoType.I) {
            success = tryAndApplyKickData(aux, I_ROTATE_RIGHT_KICK_DATA[tetromino.getRotationIndex()]);
        } else if (tetromino.getType() != TetrominoType.O) { // O piece never kicks (it's a square)
            success = tryAndApplyKickData(aux, ROTATE_RIGHT_KICK_DATA[tetromino.getRotationIndex()]);
        }

        if (success) {
            tetromino.setXYRotationIndex(aux.getX(), aux.getY(), aux.getRotationIndex());
        }
    }

    /**
     * Rotates the tetromino 90 degrees counter-clockwise, applying SRS wall kicks if necessary.
     *
     * @param tetromino The tetromino to rotate.
     */
    @Override
    public void rotateLeft(Tetromino tetromino) {
        boolean success = false;
        Tetromino aux = tetromino.createCopy();
        aux.rotateLeft();

        if (tetromino.getType() == TetrominoType.I) {
            success = tryAndApplyKickData(aux, I_ROTATE_LEFT_KICK_DATA[tetromino.getRotationIndex()]);
        } else if (tetromino.getType() != TetrominoType.O) {
            success = tryAndApplyKickData(aux, ROTATE_LEFT_KICK_DATA[tetromino.getRotationIndex()]);
        }

        if (success) {
            tetromino.setXYRotationIndex(aux.getX(), aux.getY(), aux.getRotationIndex());
        }
    }

    /**
     * Flips the tetromino 180 degrees using SRS-style flip kicks.
     *
     * @param tetromino The tetromino to flip.
     */
    @Override
    public void flip(Tetromino tetromino) {
        boolean success;
        Tetromino aux = tetromino.createCopy();
        aux.flip();

        success = tryAndApplyKickData(aux, FLIP_KICK_DATA[tetromino.getRotationIndex()]);

        if (success) {
            tetromino.setXYRotationIndex(aux.getX(), aux.getY(), aux.getRotationIndex());
        }
    }

    /**
     * Iterates through a set of kick offsets (tests) until one is valid.
     *
     * @param t        The temporary tetromino having the rotation applied.
     * @param kickData The array of offsets {x, y} to try.
     * @return {@code true} if a valid position was found, {@code false} if all tests failed.
     */
    protected boolean tryAndApplyKickData(Tetromino t, int[][] kickData) {
        boolean success = false;

        for (int i = 0; i < kickData.length && !success; i++) {
            // Apply kick: standard SRS defines y-up positive, but our grid is y-down positive.
            // Hence, we negate the Y component from the standard table.
            success = tryAndApplyKick(t, kickData[i][0], -kickData[i][1]);
        }

        return success;
    }

    /**
     * Tests a specific offset for the tetromino.
     *
     * @param t       The tetromino to test.
     * @param offsetX The X shift.
     * @param offsetY The Y shift.
     * @return {@code true} if the position is collision-free.
     */
    protected boolean tryAndApplyKick(Tetromino t, int offsetX, int offsetY) {
        boolean success;

        t.setXY(t.getX() + offsetX, t.getY() + offsetY);

        success = !grid.hasCollision(t);

        // Revert position if failed
        if (!success) { t.setXY(t.getX() - offsetX, t.getY() - offsetY); }

        return success;
    }
}