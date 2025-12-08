package tetris.tetrominoes;

import java.awt.*;

/**
 * Represents the 'Z' Tetromino (Left-facing skew/zigzag).
 * <p>
 * <b>Characteristics:</b>
 * <ul>
 * <li><b>Color:</b> Red.</li>
 * <li><b>Grid:</b> 3x3 matrix.</li>
 * <li><b>Rotations:</b> 2 distinct visual states, 4 logic states for SRS wall kicks.</li>
 * </ul>
 * </p>
 */
public class ZTetromino extends Tetromino {
    // --- ROTATION MATRICES (3x3) ---

    private static final boolean[][] Z_ROTATION_0 = {
            {true , true , false},
            {false, true , true },
            {false, false, false}
    };
    private static final boolean[][] Z_ROTATION_R = {
            {false, false, true },
            {false, true , true },
            {false, true , false}
    };
    private static final boolean[][] Z_ROTATION_2 = {
            {false, false, false},
            {true , true , false},
            {false, true , true }
    };
    private static final boolean[][] Z_ROTATION_L = {
            {false, true , false},
            {true , true , false},
            {true , false, false}
    };

    /** Collection of all rotations indexed 0-3. */
    private static final boolean[][][] Z_ROTATIONS = {Z_ROTATION_0, Z_ROTATION_R, Z_ROTATION_2, Z_ROTATION_L};

    /** The standard color for the Z piece. */
    private static final Color Z_COLOR = Color.RED.darker();

    /**
     * Constructs a Z Tetromino.
     *
     * @param rotationIndex Initial rotation (0-3).
     * @param x             Logical grid X.
     * @param y             Logical grid Y.
     * @param parentX       Screen offset X.
     * @param parentY       Screen offset Y.
     */
    public ZTetromino(int rotationIndex, int x, int y, int parentX, int parentY) {
        super(Z_ROTATIONS, rotationIndex, x, y, Z_COLOR, parentX, parentY);
    }

    @Override
    public TetrominoType getType() { return TetrominoType.Z; }

    /** Returns 3, as the Z piece rotates within a 3x3 bounding box. */
    @Override
    public int getWidth() {
        return 3;
    }

    /** Returns 3, as the Z piece rotates within a 3x3 bounding box. */
    @Override
    public int getHeight() {
        return 3;
    }

    /**
     * Returns the visual width.
     * Rotation 0/2 (Horizontal): 3 blocks.
     * Rotation 1/3 (Vertical): 2 blocks.
     */
    @Override
    public int getApparentWidth() {
        if (rotationIndex % 2 == 0) return 3;
        else return 2;
    }

    /**
     * Returns the visual height.
     * Rotation 0/2 (Horizontal): 2 blocks.
     * Rotation 1/3 (Vertical): 3 blocks.
     */
    @Override
    public int getApparentHeight() {
        if (rotationIndex % 2 == 0) return 2;
        else return 3;
    }

    @Override
    public Tetromino createCopy() {
        return new ZTetromino(getRotationIndex(), getX(), getY(), getParentX(), getParentY());
    }
}