package tetris.tetrominoes;

import java.awt.*;

/**
 * Represents the "Ghost Piece" or Shadow.
 * <p>
 * The Shadow mimics the shape and rotation of a target {@link Tetromino} but is rendered
 * semi-transparently at the bottom of the board to indicate where the piece will land.
 * </p>
 */
public class TetrominoShadow extends Tetromino {
    // --- SHADOW VISUAL CONFIGURATION ---
    /** The opacity percentage (0-100) for the shadow. */
    public static final int SHADOW_TRANSPARENCY_PERCENTAGE = 15;

    /** The alpha value (0-255) derived from the percentage. */
    public static final int SHADOW_TRANSPARENCY_OVER_255 = SHADOW_TRANSPARENCY_PERCENTAGE * 255 / 100;

    /** Reference to the actual falling tetromino this shadow tracks. */
    private Tetromino parent;

    /**
     * Creates a Shadow for the given parent tetromino.
     * <p>
     * The shadow is initialized with the same shape and position, but with a color
     * set to White with low alpha (transparency).
     * </p>
     *
     * @param parent The falling tetromino to track.
     */
    public TetrominoShadow(Tetromino parent) {
        // Usage of white with alpha creates a "highlight" effect on the dark background
        super(parent.getShapeRotations(),
                parent.rotationIndex,
                parent.getX(),
                parent.getY(),
                new Color(255, 255, 255, SHADOW_TRANSPARENCY_OVER_255),
                parent.getParentX(),
                parent.getParentY());
        this.parent = parent;
    }

    @Override
    public TetrominoType getType() { return parent.getType(); }

    @Override
    public int getWidth() { return parent.getWidth(); }

    @Override
    public int getHeight() { return parent.getHeight(); }

    @Override
    public int getApparentWidth() { return parent.getApparentWidth(); }

    @Override
    public int getApparentHeight() { return parent.getApparentHeight(); }

    /**
     * Creates a copy of the shadow.
     * Note: This recursively copies the parent piece to maintain independent state.
     */
    @Override
    public Tetromino createCopy() {
        return new TetrominoShadow(parent.createCopy());
    }
}