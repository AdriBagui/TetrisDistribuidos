package tetris.tetrominoes;

import tetris.generators.TetrominoFactory;

import java.awt.*;

public class ITetromino extends Tetromino {
    private static final boolean[][] I_ROTATION_0 = {
            {false, false, false, false},
            {true , true , true , true },
            {false, false, false, false},
            {false, false, false, false}
    };
    private static final boolean[][] I_ROTATION_R  = {
            {false, false, true , false},
            {false, false, true , false},
            {false, false, true , false},
            {false, false, true , false}
    };
    private static final boolean[][] I_ROTATION_2 = {
            {false, false, false, false},
            {false, false, false, false},
            {true , true , true , true },
            {false, false, false, false}
    };
    private static final boolean[][] I_ROTATION_L = {
            {false, true , false, false},
            {false, true , false, false},
            {false, true , false, false},
            {false, true , false, false}
    };
    private static final boolean[][][] I_ROTATIONS = {I_ROTATION_0, I_ROTATION_R, I_ROTATION_2, I_ROTATION_L};
    private static final Color I_COLOR = Color.CYAN;

    public ITetromino(int rotationIndex, int x, int y, int boardX, int boardY) {
        super(I_ROTATIONS, rotationIndex, x, y, I_COLOR, boardX, boardY);
    }

    @Override
    public int getType() { return TetrominoFactory.I; }

    @Override
    public int getWidth() {
        return 4;
    }

    @Override
    public int getHeight() {
        return 4;
    }

    @Override
    public int getApparentWidth() {
        int width;

        if (rotationIndex % 2 == 0) width = 4;
        else width = 1;

        return width;
    }

    @Override
    public int getApparentHeight() {
        int height;

        if (rotationIndex % 2 == 0) height = 1;
        else height = 4;

        return height;
    }
}
