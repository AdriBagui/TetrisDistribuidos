package tetris.tetrominoes.generators;

import tetris.tetrominoes.*;

import static tetris.Config.*;

public class TetrominoFactory {
    /**
     * Generates a tetromino
     * @param type Type of tetromino (I, O, T, J, L, S or Z)
     * @param rotationIndex rotation index of the tetromino
     * @param x value of the x coordinate of the tetromino's container
     * @param y value of the y coordinate of the tetromino's container
     * @param containerX value of the x coordinate of tetromino's parent container
     * @param containerY value of the y coordinate of tetromino's parent container
     * @return Tetromino with the values introduced
     */
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
