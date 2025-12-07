package tetris.tetrominoes.generators;

import tetris.tetrominoes.Tetromino;
import tetris.tetrominoes.TetrominoFactory;
import tetris.tetrominoes.TetrominoType;
import static tetris.tetrominoes.Tetromino.NUMBER_OF_TETROMINOES;

public class NESTetrominoesGenerator extends TetrominoesGenerator {
    private int previousTetrominoType;

    public NESTetrominoesGenerator(long seed) {
        super(seed);
        previousTetrominoType = NUMBER_OF_TETROMINOES;
    }

    @Override
    public Tetromino getNext(int containerX, int containerY) {
        int nextTetrominoType = random.nextInt(NUMBER_OF_TETROMINOES + 1);

        if (nextTetrominoType == NUMBER_OF_TETROMINOES || nextTetrominoType == previousTetrominoType) {
            nextTetrominoType = random.nextInt(NUMBER_OF_TETROMINOES);
        }

        previousTetrominoType = nextTetrominoType;

        return TetrominoFactory.createTetromino(TetrominoType.values()[nextTetrominoType], 0, 0, 0, containerX, containerY);
    }
}
