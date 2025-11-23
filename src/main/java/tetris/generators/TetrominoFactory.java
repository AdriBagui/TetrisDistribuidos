package tetris.generators;

import tetris.Board;
import tetris.tetrominoes.*;

public class TetrominoFactory {
    public static final int I = 0;
    public static final int O = 1;
    public static final int T = 2;
    public static final int J = 3;
    public static final int L = 4;
    public static final int S = 5;
    public static final int Z = 6;



    public static Tetromino createTetromino(int type, int rotationIndex, int x, int y, int containerX, int containerY) {
        Tetromino tetromino = null;

        switch (type) {
            case I:
                tetromino = new ITetromino(rotationIndex, x, y, containerX, containerY);
                break;
            case O:
                tetromino = new OTetromino(rotationIndex, x, y, containerX, containerY);
                break;
            case T:
                tetromino = new TTetromino(rotationIndex, x, y, containerX, containerY);
                break;
            case J:
                tetromino = new JTetromino(rotationIndex, x, y, containerX, containerY);
                break;
            case L:
                tetromino = new LTetromino(rotationIndex, x, y, containerX, containerY);
                break;
            case S:
                tetromino = new STetromino(rotationIndex, x, y, containerX, containerY);
                break;
            case Z:
                tetromino = new ZTetromino(rotationIndex, x, y, containerX, containerY);
                break;
        }

        return tetromino;
    }
}
