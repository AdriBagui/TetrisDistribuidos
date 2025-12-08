package tetris.tetrominoes;

import java.awt.*;

/**
 * Represents the 'J' Tetromino (L-shape with a left-facing hook).
 * <p>
 * <b>Characteristics:</b>
 * <ul>
 * <li><b>Color:</b> Blue.</li>
 * <li><b>Grid:</b> 3x3 matrix.</li>
 * <li><b>Rotations:</b> Standard 4-state rotation within the 3x3 bounding box.</li>
 * </ul>
 * </p>
 */
public class JTetromino extends Tetromino {
    // --- ROTATION MATRICES (3x3) ---

    private static final boolean[][] J_ROTATION_0 = {
            {true , false, false},
            {true , true , true },
            {false, false, false}
    };
    private static final boolean[][] J_ROTATION_R = {
            {false, true , true },
            {false, true , false},
            {false, true , false}
    };
    private static final boolean[][] J_ROTATION_2 = {
            {false, false, false},
            {true , true , true },
            {false, false, true }
    };
    private static final boolean[][] J_ROTATION_L = {
            {false, true , false},
            {false, true , false},
            {true , true , false}
    };

    /** Collection of all rotations indexed 0-3. */
    private static final boolean[][][] J_ROTATIONS = {J_ROTATION_0, J_ROTATION_R, J_ROTATION_2, J_ROTATION_L};

    /** The standard color for the J piece. */
    private static final Color J_COLOR = Color.BLUE.darker();

    /**
     * Constructs a J Tetromino.
     *
     * @param rotationIndex Initial rotation (0-3).
     * @param x             Logical grid X.
     * @param y             Logical grid Y.
     * @param parentX       Screen offset X.
     * @param parentY       Screen offset Y.
     */
    public JTetromino(int rotationIndex, int x, int y, int parentX, int parentY) {
        super(J_ROTATIONS, rotationIndex, x, y, J_COLOR, parentX, parentY);
    }

    @Override
    public TetrominoType getType() { return TetrominoType.J; }

    /** Returns 3, as the J piece rotates within a 3x3 bounding box. */
    @Override
    public int getWidth() {
        return 3;
    }

    /** Returns 3, as the J piece rotates within a 3x3 bounding box. */
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
        return new JTetromino(getRotationIndex(), getX(), getY(), getParentX(), getParentY());
    }
}