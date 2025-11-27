package tetris.tetrominoes;

import tetris.generators.TetrominoFactory;

import java.awt.*;

public class STetromino extends Tetromino {
    private static final boolean[][] S_ROTATION_0 = {
            {false, true , true },
            {true , true , false},
            {false, false, false}
    };
    private static final boolean[][] S_ROTATION_R = {
            {false, true , false},
            {false, true , true },
            {false, false, true }
    };
    private static final boolean[][] S_ROTATION_2 = {
            {false, false, false},
            {false, true , true },
            {true , true , false}
    };
    private static final boolean[][] S_ROTATION_L = {
            {true , false, false},
            {true , true , false},
            {false, true , false}
    };
    private static final boolean[][][] S_ROTATIONS = {S_ROTATION_0, S_ROTATION_R, S_ROTATION_2, S_ROTATION_L};
    private static final Color S_COLOR = Color.GREEN.darker();

    public STetromino(int rotationIndex, int x, int y, int parentX, int parentY) {
        super(S_ROTATIONS, rotationIndex, x, y, S_COLOR, parentX, parentY);
    }

    @Override
    public int getType() { return TetrominoFactory.S; }

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
        return new STetromino(getRotationIndex(), getX(), getY(), getParentX(), getParentY());
    }
}
