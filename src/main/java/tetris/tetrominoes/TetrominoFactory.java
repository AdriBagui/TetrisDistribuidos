package tetris.tetrominoes;

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
    public static Tetromino createTetromino(TetrominoType type, int rotationIndex, int x, int y, int containerX, int containerY) {
        return switch (type) {
            case I -> new ITetromino(rotationIndex, x, y, containerX, containerY);
            case O -> new OTetromino(rotationIndex, x, y, containerX, containerY);
            case T -> new TTetromino(rotationIndex, x, y, containerX, containerY);
            case J -> new JTetromino(rotationIndex, x, y, containerX, containerY);
            case L -> new LTetromino(rotationIndex, x, y, containerX, containerY);
            case S -> new STetromino(rotationIndex, x, y, containerX, containerY);
            case Z -> new ZTetromino(rotationIndex, x, y, containerX, containerY);
        };
    }
}
