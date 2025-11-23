package tetris.generators;

import tetris.tetrominoes.Tetromino;

public class RandomBagTetrominoesGenerator extends TetrominoesGenerator {
    private Tetromino[] bag;
    private int used;

    public RandomBagTetrominoesGenerator(long seed) {
        super(seed);

        bag = new Tetromino[7];
        used = 7;
    }

    @Override
    public Tetromino getNext(int containerX, int containerY) {
        if(used > 6) {
            fillBag(containerX, containerY);
        }

        used++;

        return bag[used - 1];
    }

    private void fillBag(int containerX, int containerY) {
        for(int i = 0; i < 7; i++) {
            bag[i] = TetrominoFactory.createTetromino(i, 0, 0, 0, containerX, containerY);
        }

        shuffleBag();

        used = 0;
    }

    private void shuffleBag() {
        for(int i = 0; i < 7; i++) {
            swap(i, random.nextInt(7));
        }
    }

    private void swap(int i, int j) {
        Tetromino aux = bag[i];
        bag[i] = bag[j];
        bag[j] = aux;
    }
}
