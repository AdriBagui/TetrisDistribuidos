package src.tetris.tetrominoes;

import java.awt.*;

import static src.tetris.Config.*;

public class TTetromino extends Tetromino {
    private static final boolean[][] T_ROTATION_0 = {
            {false, true , false},
            {true , true , true },
            {false, false, false}
    };
    private static final boolean[][] T_ROTATION_R = {
            {false, true , false},
            {false, true , true },
            {false, true , false}
    };
    private static final boolean[][] T_ROTATION_2 = {
            {false, false, false},
            {true , true , true },
            {false, true , false}
    };
    private static final boolean[][] T_ROTATION_L = {
            {false, true , false},
            {true , true , false},
            {false, true , false}
    };
    private static final boolean[][][] T_ROTATIONS = {T_ROTATION_0, T_ROTATION_R, T_ROTATION_2, T_ROTATION_L};
    private static final Color T_COLOR = Color.MAGENTA.darker();

    public TTetromino(int rotationIndex, int x, int y, int parentX, int parentY) {
        super(T_ROTATIONS, rotationIndex, x, y, T_COLOR, parentX, parentY);
    }

    @Override
    public int getType() { return T; }

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
        return new TTetromino(getRotationIndex(), getX(), getY(), getParentX(), getParentY());
    }
}
