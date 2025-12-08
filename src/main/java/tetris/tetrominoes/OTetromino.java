package tetris.tetrominoes;

import java.awt.*;

/**
 * Represents the 'O' Tetromino (the Square).
 * <p>
 * <b>Characteristics:</b>
 * <ul>
 * <li><b>Color:</b> Yellow.</li>
 * <li><b>Grid:</b> 2x2 matrix (effectively, though rotation logic often fits it in larger bounds).</li>
 * <li><b>Rotations:</b> Visually identical in all 4 states. Often does not rotate in strict SRS, but state index changes.</li>
 * </ul>
 * </p>
 */
public class OTetromino extends Tetromino {
    // --- ROTATION MATRICES (2x2) ---
    // The O piece does not change appearance upon rotation.

    private static final boolean[][] O_ROTATION_0 = {
            {true , true },
            {true , true }
    };

    /** Collection of identical rotations indexed 0-3. */
    private static final boolean[][][] O_ROTATIONS = {O_ROTATION_0, O_ROTATION_0, O_ROTATION_0, O_ROTATION_0};

    /** The standard color for the O piece. */
    private static final Color O_COLOR = Color.YELLOW.darker();

    /**
     * Constructs an O Tetromino.
     *
     * @param rotationIndex Initial rotation (0-3).
     * @param x             Logical grid X.
     * @param y             Logical grid Y.
     * @param parentX       Screen offset X.
     * @param parentY       Screen offset Y.
     */
    public OTetromino(int rotationIndex, int x, int y, int parentX, int parentY) {
        super(O_ROTATIONS, rotationIndex, x, y, O_COLOR, parentX, parentY);
    }

    @Override
    public TetrominoType getType() { return TetrominoType.O; }

    /** Returns 2, as the O piece fits within a 2x2 bounding box. */
    @Override
    public int getWidth() { return 2; }

    /** Returns 2, as the O piece fits within a 2x2 bounding box. */
    @Override
    public int getHeight() { return  2; }

    /** Returns 2 (Square is always 2 wide). */
    @Override
    public int getApparentWidth() { return 2; }

    /** Returns 2 (Square is always 2 high). */
    @Override
    public int getApparentHeight() { return 2; }

    @Override
    public Tetromino createCopy() {
        return new OTetromino(getRotationIndex(), getX(), getY(), getParentX(), getParentY());
    }
}