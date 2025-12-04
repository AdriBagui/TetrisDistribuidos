package tetris.tetrominoes;

import java.awt.*;

import static tetris.Config.*;

public class OTetromino extends Tetromino {
    private static final boolean[][] O_ROTATION_0 = {
            {true , true },
            {true , true }
    };
    private static final boolean[][][] O_ROTATIONS = {O_ROTATION_0, O_ROTATION_0, O_ROTATION_0, O_ROTATION_0};
    private static final Color O_COLOR = Color.YELLOW.darker();

    public OTetromino(int rotationIndex, int x, int y, int parentX, int parentY) {
        super(O_ROTATIONS, rotationIndex, x, y, O_COLOR, parentX, parentY);
    }

    @Override
    public int getType() { return O; }

    @Override
    public int getWidth() { return 2; }

    @Override
    public int getHeight() { return  2; }

    @Override
    public int getApparentWidth() { return 2; }

    @Override
    public int getApparentHeight() { return 2; }

    @Override
    public Tetromino createCopy() {
        return new OTetromino(getRotationIndex(), getX(), getY(), getParentX(), getParentY());
    }
}
