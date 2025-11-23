package tetris.generators;

import tetris.tetrominoes.Tetromino;

public class CompletelyRandomTetrominoesGenerator extends TetrominoesGenerator {
    public CompletelyRandomTetrominoesGenerator(long seed) {
        super(seed);
    }

    @Override
    public Tetromino getNext(int containerX, int containerY) {
        return TetrominoFactory.createTetromino(random.nextInt(7), 0, 0, 0, containerX, containerY);
    }
}
