package tetris.tetrominoes.generators;

import tetris.tetrominoes.Tetromino;

import static tetris.Config.NUMBER_TETROMINOES;

public class CompletelyRandomTetrominoesGenerator extends TetrominoesGenerator {
    public CompletelyRandomTetrominoesGenerator(long seed) {
        super(seed);
    }

    @Override
    public Tetromino getNext(int containerX, int containerY) {
        return TetrominoFactory.createTetromino(random.nextInt(NUMBER_TETROMINOES), 0, 0, 0, containerX, containerY);
    }
}
