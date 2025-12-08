package tetris.tetrominoes;

import java.awt.*;

/**
 * Represents the 'T' Tetromino (T-shape).
 * <p>
 * <b>Characteristics:</b>
 * <ul>
 * <li><b>Color:</b> Magenta (Purple).</li>
 * <li><b>Grid:</b> 3x3 matrix.</li>
 * <li><b>Rotations:</b> 4 distinct visual states. Used for "T-Spins" in modern Tetris.</li>
 * </ul>
 * </p>
 */
public class TTetromino extends Tetromino {
    // --- ROTATION MATRICES (3x3) ---

    private static final boolean[][] T_ROTATION_0 = {
            {false, true , false},
            {true , true , true },
            {false, false, false}
    };
    private static final boolean[][] T_ROTATION_R = {
            {false, true , false},
            {false, true , true },
            {false, true , false}
    };
    private static final boolean[][] T_ROTATION_2 = {
            {false, false, false},
            {true , true , true },
            {false, true , false}
    };
    private static final boolean[][] T_ROTATION_L = {
            {false, true , false},
            {true , true , false},
            {false, true , false}
    };

    /** Collection of all rotations indexed 0-3. */
    private static final boolean[][][] T_ROTATIONS = {T_ROTATION_0, T_ROTATION_R, T_ROTATION_2, T_ROTATION_L};

    /** The standard color for the T piece. */
    private static final Color T_COLOR = Color.MAGENTA.darker();

    /**
     * Constructs a T Tetromino.
     *
     * @param rotationIndex Initial rotation (0-3).
     * @param x             Logical grid X.
     * @param y             Logical grid Y.
     * @param parentX       Screen offset X.
     * @param parentY       Screen offset Y.
     */
    public TTetromino(int rotationIndex, int x, int y, int parentX, int parentY) {
        super(T_ROTATIONS, rotationIndex, x, y, T_COLOR, parentX, parentY);
    }

    @Override
    public TetrominoType getType() { return TetrominoType.T; }

    /** Returns 3, as the T piece rotates within a 3x3 bounding box. */
    @Override
    public int getWidth() {
        return 3;
    }

    /** Returns 3, as the T piece rotates within a 3x3 bounding box. */
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
        return new TTetromino(getRotationIndex(), getX(), getY(), getParentX(), getParentY());
    }
}