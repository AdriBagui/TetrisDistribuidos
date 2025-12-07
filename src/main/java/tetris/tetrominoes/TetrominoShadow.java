package tetris.tetrominoes;

import tetris.tetrominoes.generators.TetrominoesGeneratorType;

import java.awt.*;

public class TetrominoShadow extends Tetromino {
    // SHADOW CONFIGURATION
    public static final int SHADOW_TRANSPARENCY_PERCENTAGE = 15;
    public static final int SHADOW_TRANSPARENCY_OVER_255 = SHADOW_TRANSPARENCY_PERCENTAGE*255/100;
    public static final int SHADOW_TRANSPARENCY = (SHADOW_TRANSPARENCY_OVER_255) * 33554432;

    private Tetromino parent;

    public TetrominoShadow(Tetromino parent) {
        // For same color as parent use the following code: new Color(parent.getColor().darker().darker().getRGB() + SHADOW_TRANSPARENCY, true)
        super(parent.getShapeRotations(), parent.rotationIndex, parent.getX(), parent.getY(), new Color(255, 255, 255, SHADOW_TRANSPARENCY_OVER_255), parent.getParentX(), parent.getParentY());
        this.parent = parent;
    }

    @Override
    public TetrominoType getType() { return parent.getType(); }

    @Override
    public int getWidth() {
        return parent.getWidth();
    }

    @Override
    public int getHeight() {
        return parent.getHeight();
    }

    @Override
    public int getApparentWidth() { return parent.getApparentWidth(); }

    @Override
    public int getApparentHeight() { return parent.getApparentHeight(); }

    @Override
    public Tetromino createCopy() {
        return new TetrominoShadow(parent.createCopy());
    }
}
