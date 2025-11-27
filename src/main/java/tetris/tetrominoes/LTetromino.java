package tetris.tetrominoes;

import tetris.generators.TetrominoFactory;

import java.awt.*;

public class LTetromino extends Tetromino {
    private static final boolean[][] L_ROTATION_0 = {
            {false, false, true },
            {true , true , true },
            {false, false, false}
    };
    private static final boolean[][] L_ROTATION_R = {
            {false, true , false},
            {false, true , false},
            {false, true , true }
    };
    private static final boolean[][] L_ROTATION_2 = {
            {false, false, false},
            {true , true , true },
            {true , false, false}
    };
    private static final boolean[][] L_ROTATION_L = {
            {true , true , false},
            {false, true , false},
            {false, true , false}
    };
    private static final boolean[][][] L_ROTATIONS = {L_ROTATION_0, L_ROTATION_R, L_ROTATION_2, L_ROTATION_L};
    private static final Color L_COLOR = Color.ORANGE.darker();

    public LTetromino(int rotationIndex, int x, int y, int parentX, int parentY) {
        super(L_ROTATIONS, rotationIndex, x, y, L_COLOR, parentX, parentY);
    }

    @Override
    public int getType() { return TetrominoFactory.L; }

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
        return new LTetromino(getRotationIndex(), getX(), getY(), getParentX(), getParentY());
    }
}
