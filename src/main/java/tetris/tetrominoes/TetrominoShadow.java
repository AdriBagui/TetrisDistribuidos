package tetris.tetrominoes;

import java.awt.*;

public class TetrominoShadow extends Tetromino {
    private static final int TRANSPARENCY_PERCENTAGE = 25;
    private static final int TRANSPARENCY = (TRANSPARENCY_PERCENTAGE *255/100) * 33554432;

    private Tetromino parent;

    public TetrominoShadow(Tetromino parent) {
        super(parent.getShapeRotations(), parent.rotationIndex, parent.getX(), parent.getY(), new Color(parent.getColor().darker().darker().getRGB() + TRANSPARENCY, true), parent.getParentX(), parent.getParentY());
        this.parent = parent;
    }

    public void project(int distance) { setY(getY() + distance); }

    @Override
    public void setColor(Color color) { super.setColor(color.darker().darker());}

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
