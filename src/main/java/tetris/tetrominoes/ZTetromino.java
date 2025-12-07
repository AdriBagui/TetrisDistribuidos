package tetris.tetrominoes;

import java.awt.*;

import static tetris.Config.*;

public class ZTetromino extends Tetromino {
    private static final boolean[][] Z_ROTATION_0 = {
            {true , true , false},
            {false, true , true },
            {false, false, false}
    };
    private static final boolean[][] Z_ROTATION_R = {
            {false, false, true },
            {false, true , true },
            {false, true , false}
    };
    private static final boolean[][] Z_ROTATION_2 = {
            {false, false, false},
            {true , true , false},
            {false, true , true }
    };
    private static final boolean[][] Z_ROTATION_L = {
            {false, true , false},
            {true , true , false},
            {true , false, false}
    };
    private static final boolean[][][] Z_ROTATIONS = {Z_ROTATION_0, Z_ROTATION_R, Z_ROTATION_2, Z_ROTATION_L};
    private static final Color Z_COLOR = Color.RED.darker();

    public ZTetromino(int rotationIndex, int x, int y, int parentX, int parentY) {
        super(Z_ROTATIONS, rotationIndex, x, y, Z_COLOR, parentX, parentY);
    }

    @Override
    public TetrominoType getType() { return TetrominoType.Z; }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 3;
    }

    @Override
    public int getApparentWidth() {
        int width;

        if (rotationIndex % 2 == 0) width = 3;
        else width = 2;

        return width;
    }

    @Override
    public int getApparentHeight() {
        int height;

        if (rotationIndex % 2 == 0) height = 2;
        else height = 3;

        return height;
    }

    @Override
    public Tetromino createCopy() {
        return new ZTetromino(getRotationIndex(), getX(), getY(), getParentX(), getParentY());
    }
}
