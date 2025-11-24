package tetris.tetrominoes;

import java.awt.*;

public class TetrominoShadow extends Tetromino {
    private static final int TRANSPARENCY_PERCENTAGE = 20;
    private static final int TRANSPARENCY = (TRANSPARENCY_PERCENTAGE *255/100) * 33554432;

    private Tetromino t;

    public TetrominoShadow(Tetromino t) {
        super(t.getShapeRotations(), t.rotationIndex, t.getX(), t.getY(), new Color(t.getColor().darker().darker().getRGB() + TRANSPARENCY, true), t.getParentX(), t.getParentY());
        this.t = t;
    }

    @Override
    public int getType() { return t.getType(); }

    @Override
    public int getWidth() {
        return t.getWidth();
    }

    @Override
    public int getHeight() {
        return t.getHeight();
    }

    @Override
    public int getApparentWidth() { return t.getApparentWidth(); }

    @Override
    public int getApparentHeight() { return t.getApparentHeight(); }
}
