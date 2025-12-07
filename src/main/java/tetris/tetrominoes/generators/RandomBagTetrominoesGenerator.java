package tetris.tetrominoes.generators;

import tetris.tetrominoes.Tetromino;

import static tetris.Config.NUMBER_TETROMINOES;

public class RandomBagTetrominoesGenerator extends TetrominoesGenerator {
    private Tetromino[] bag;
    private int used;

    public RandomBagTetrominoesGenerator(long seed) {
        super(seed);

        bag = new Tetromino[NUMBER_TETROMINOES];
        used = NUMBER_TETROMINOES;
    }

    @Override
    public Tetromino getNext(int containerX, int containerY) {
        if(used >= NUMBER_TETROMINOES) {
            fillBag(containerX, containerY);
        }

        used++;

        return bag[used - 1];
    }

    private void fillBag(int containerX, int containerY) {
        for(int i = 0; i < NUMBER_TETROMINOES; i++) {
            bag[i] = TetrominoFactory.createTetromino(i, 0, 0, 0, containerX, containerY);
        }

        shuffleBag();

        used = 0;
    }

    private void shuffleBag() {
        for(int i = 0; i < NUMBER_TETROMINOES; i++) {
            swap(i, random.nextInt(NUMBER_TETROMINOES));
        }
    }

    private void swap(int i, int j) {
        Tetromino aux = bag[i];
        bag[i] = bag[j];
        bag[j] = aux;
    }
}
