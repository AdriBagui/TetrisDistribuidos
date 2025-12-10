package tetris.tetrominoes;

/**
 * Factory class for creating {@link Tetromino} instances.
 * <p>
 * This class encapsulates the instantiation logic, switching based on the requested
 * {@link TetrominoType}. It is used by generators (like the 7-Bag or Random generator)
 * to spawn pieces.
 * </p>
 */
public class TetrominoFactory {
    /**
     * Creates a specific Tetromino instance.
     *
     * @param type           The type of tetromino (I, O, T, J, L, S, or Z).
     * @param rotationIndex  The initial rotation state (0-3).
     * @param x              The logical X coordinate on the board.
     * @param y              The logical Y coordinate on the board.
     * @param containerX     The screen X offset of the parent container.
     * @param containerY     The screen Y offset of the parent container.
     * @return A new instance of the requested Tetromino.
     */
    public static Tetromino createTetromino(TetrominoType type, int rotationIndex, int x, int y, int containerX, int containerY) {
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