package tetris.tetrominoes;

import java.awt.*;

/**
 * Represents the 'S' Tetromino (Right-facing skew/zigzag).
 * <p>
 * <b>Characteristics:</b>
 * <ul>
 * <li><b>Color:</b> Green.</li>
 * <li><b>Grid:</b> 3x3 matrix.</li>
 * <li><b>Rotations:</b> 2 distinct visual states, 4 logic states for SRS wall kicks.</li>
 * </ul>
 * </p>
 */
public class STetromino extends Tetromino {
    // --- ROTATION MATRICES (3x3) ---

    private static final boolean[][] S_ROTATION_0 = {
            {false, true , true },
            {true , true , false},
            {false, false, false}
    };
    private static final boolean[][] S_ROTATION_R = {
            {false, true , false},
            {false, true , true },
            {false, false, true }
    };
    private static final boolean[][] S_ROTATION_2 = {
            {false, false, false},
            {false, true , true },
            {true , true , false}
    };
    private static final boolean[][] S_ROTATION_L = {
            {true , false, false},
            {true , true , false},
            {false, true , false}
    };

    /** Collection of all rotations indexed 0-3. */
    private static final boolean[][][] S_ROTATIONS = {S_ROTATION_0, S_ROTATION_R, S_ROTATION_2, S_ROTATION_L};

    /** The standard color for the S piece. */
    private static final Color S_COLOR = Color.GREEN.darker();

    /**
     * Constructs an S Tetromino.
     *
     * @param rotationIndex Initial rotation (0-3).
     * @param x             Logical grid X.
     * @param y             Logical grid Y.
     * @param parentX       Screen offset X.
     * @param parentY       Screen offset Y.
     */
    public STetromino(int rotationIndex, int x, int y, int parentX, int parentY) {
        super(S_ROTATIONS, rotationIndex, x, y, S_COLOR, parentX, parentY);
    }

    @Override
    public TetrominoType getType() { return TetrominoType.S; }

    /** Returns 3, as the S piece rotates within a 3x3 bounding box. */
    @Override
    public int getWidth() {
        return 3;
    }

    /** Returns 3, as the S piece rotates within a 3x3 bounding box. */
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
        return new STetromino(getRotationIndex(), getX(), getY(), getParentX(), getParentY());
    }
}