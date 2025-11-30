package src.tetris.tetrominoes;

import java.awt.*;

import static src.tetris.Config.*;

public class TetrominoShadow extends Tetromino {
    private Tetromino parent;

    public TetrominoShadow(Tetromino parent) {
        super(parent.getShapeRotations(), parent.rotationIndex, parent.getX(), parent.getY(), new Color(parent.getColor().darker().darker().getRGB() + SHADOW_TRANSPARENCY, true), parent.getParentX(), parent.getParentY());
        this.parent = parent;
    }

    @Override
    public int getType() { return parent.getType(); }

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
