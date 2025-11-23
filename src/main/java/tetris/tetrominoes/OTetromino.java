package tetris.tetrominoes;

import tetris.generators.TetrominoFactory;

import java.awt.*;

public class OTetromino extends Tetromino {
    private static final boolean[][] O_ROTATION_0 = {
            {true , true },
            {true , true }
    };
    private static final boolean[][][] O_ROTATIONS = {O_ROTATION_0, O_ROTATION_0, O_ROTATION_0, O_ROTATION_0};
    private static final Color O_COLOR = Color.YELLOW;

    public OTetromino(int rotationIndex, int x, int y, int boardX, int boardY) {
        super(O_ROTATIONS, rotationIndex, x, y, O_COLOR, boardX, boardY);
    }

    @Override
    public int getType() { return TetrominoFactory.O; }

    @Override
    public int getWidth() { return 2; }

    @Override
    public int getHeight() { return  2; }

    @Override
    public int getApparentWidth() { return 2; }

    @Override
    public int getApparentHeight() { return 2; }
}
