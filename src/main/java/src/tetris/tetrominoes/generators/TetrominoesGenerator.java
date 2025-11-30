package src.tetris.tetrominoes.generators;

import src.tetris.tetrominoes.Tetromino;

import java.util.Random;

public abstract class TetrominoesGenerator {
    protected Random random;

    public TetrominoesGenerator(long seed) {
        random = new Random(seed);
    }

    public abstract Tetromino getNext(int containerX, int containerY);
}
