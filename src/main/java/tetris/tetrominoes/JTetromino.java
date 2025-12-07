package tetris.tetrominoes;

import java.awt.*;

public class JTetromino extends Tetromino {
    private static final boolean[][] J_ROTATION_0 = {
            {true , false, false},
            {true , true , true },
            {false, false, false}
    };
    private static final boolean[][] J_ROTATION_R = {
            {false, true , true },
            {false, true , false},
            {false, true , false}
    };
    private static final boolean[][] J_ROTATION_2 = {
            {false, false, false},
            {true , true , true },
            {false, false, true }
    };
    private static final boolean[][] J_ROTATION_L = {
            {false, true , false},
            {false, true , false},
            {true , true , false}
    };
    private static final boolean[][][] J_ROTATIONS = {J_ROTATION_0, J_ROTATION_R, J_ROTATION_2, J_ROTATION_L};
    private static final Color J_COLOR = Color.BLUE.darker();

    public JTetromino(int rotationIndex, int x, int y, int parentX, int parentY) {
        super(J_ROTATIONS, rotationIndex, x, y, J_COLOR, parentX, parentY);
    }

    @Override
    public TetrominoType getType() { return TetrominoType.J; }

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
        return new JTetromino(getRotationIndex(), getX(), getY(), getParentX(), getParentY());
    }
}
