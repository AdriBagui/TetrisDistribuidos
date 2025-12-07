package tetris.tetrominoes.generators;

import tetris.tetrominoes.Tetromino;
import tetris.tetrominoes.TetrominoFactory;
import tetris.tetrominoes.TetrominoType;

import static tetris.tetrominoes.Tetromino.NUMBER_OF_TETROMINOES;

public class RandomBag7TetrominoesGenerator extends TetrominoesGenerator {
    private Tetromino[] bag;
    private int used;

    public RandomBag7TetrominoesGenerator(long seed) {
        super(seed);

        bag = new Tetromino[NUMBER_OF_TETROMINOES];
        used = NUMBER_OF_TETROMINOES;
    }

    @Override
    public Tetromino getNext(int containerX, int containerY) {
        if(used >= NUMBER_OF_TETROMINOES) {
            fillBag(containerX, containerY);
        }

        used++;

        return bag[used - 1];
    }

    private void fillBag(int containerX, int containerY) {
        for(int i = 0; i < NUMBER_OF_TETROMINOES; i++) {
            bag[i] = TetrominoFactory.createTetromino(TetrominoType.values()[i], 0, 0, 0, containerX, containerY);
        }

        shuffleBag();

        used = 0;
    }

    private void shuffleBag() {
        for(int i = 0; i < NUMBER_OF_TETROMINOES; i++) {
            swap(i, random.nextInt(NUMBER_OF_TETROMINOES));
        }
    }

    private void swap(int i, int j) {
        Tetromino aux = bag[i];
        bag[i] = bag[j];
        bag[j] = aux;
    }
}
