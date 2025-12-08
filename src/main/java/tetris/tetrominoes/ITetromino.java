package tetris.tetrominoes;

import java.awt.*;

/**
 * Represents the 'I' Tetromino (the straight line).
 * <p>
 * <b>Characteristics:</b>
 * <ul>
 * <li><b>Color:</b> Cyan.</li>
 * <li><b>Grid:</b> 4x4 matrix (unique among standard pieces, which are typically 3x3 or 2x2).</li>
 * <li><b>Rotations:</b> Has 2 distinct visual states, but 4 logic states for SRS wall kicks.</li>
 * </ul>
 * </p>
 */
public class ITetromino extends Tetromino {
    // --- ROTATION MATRICES (4x4) ---

    private static final boolean[][] I_ROTATION_0 = {
            {false, false, false, false},
            {true , true , true , true },
            {false, false, false, false},
            {false, false, false, false}
    };
    private static final boolean[][] I_ROTATION_R  = {
            {false, false, true , false},
            {false, false, true , false},
            {false, false, true , false},
            {false, false, true , false}
    };
    private static final boolean[][] I_ROTATION_2 = {
            {false, false, false, false},
            {false, false, false, false},
            {true , true , true , true },
            {false, false, false, false}
    };
    private static final boolean[][] I_ROTATION_L = {
            {false, true , false, false},
            {false, true , false, false},
            {false, true , false, false},
            {false, true , false, false}
    };

    /** Collection of all rotations indexed 0-3. */
    private static final boolean[][][] I_ROTATIONS = {I_ROTATION_0, I_ROTATION_R, I_ROTATION_2, I_ROTATION_L};

    /** The standard color for the I piece. */
    private static final Color I_COLOR = Color.CYAN.darker();

    /**
     * Constructs an I Tetromino.
     *
     * @param rotationIndex Initial rotation (0-3).
     * @param x             Logical grid X.
     * @param y             Logical grid Y.
     * @param parentX       Screen offset X.
     * @param parentY       Screen offset Y.
     */
    public ITetromino(int rotationIndex, int x, int y, int parentX, int parentY) {
        super(I_ROTATIONS, rotationIndex, x, y, I_COLOR, parentX, parentY);
    }

    @Override
    public TetrominoType getType() { return TetrominoType.I; }

    /** Returns 4, as the I piece rotates within a 4x4 bounding box. */
    @Override
    public int getWidth() { return 4; }

    /** Returns 4, as the I piece rotates within a 4x4 bounding box. */
    @Override
    public int getHeight() { return 4; }

    /**
     * Returns the visual width.
     * Rotation 0/2 (Horizontal): 4 blocks.
     * Rotation 1/3 (Vertical): 1 block.
     */
    @Override
    public int getApparentWidth() {
        if (rotationIndex % 2 == 0) return 4;
        else return 1;
    }

    /**
     * Returns the visual height.
     * Rotation 0/2 (Horizontal): 1 block.
     * Rotation 1/3 (Vertical): 4 blocks.
     */
    @Override
    public int getApparentHeight() {
        if (rotationIndex % 2 == 0) return 1;
        else return 4;
    }

    @Override
    public Tetromino createCopy() {
        return new ITetromino(getRotationIndex(), getX(), getY(), getParentX(), getParentY());
    }
}