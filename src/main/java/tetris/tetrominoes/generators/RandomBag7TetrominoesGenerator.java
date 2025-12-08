package tetris.tetrominoes.generators;

import tetris.tetrominoes.Tetromino;
import tetris.tetrominoes.TetrominoFactory;
import tetris.tetrominoes.TetrominoType;

import static tetris.tetrominoes.Tetromino.NUMBER_OF_TETROMINOES;

/**
 * Implements the "7-Bag" Random Generator used in Modern Tetris Guidelines.
 * <p>
 * <b>Algorithm Details:</b><br>
 * 1. A "bag" is filled with exactly one of each of the 7 Tetromino types.<br>
 * 2. The bag is shuffled randomly.<br>
 * 3. Pieces are drawn from the bag one by one.<br>
 * 4. When the bag is empty, a new bag is generated and shuffled.
 * </p>
 * <p>
 * This ensures a uniform distribution where players cannot receive the same piece more
 * than twice in a row (end of Bag A, start of Bag B) and cannot go too long without a specific piece.
 * </p>
 */
public class RandomBag7TetrominoesGenerator extends TetrominoesGenerator {
    /** The current bag of pieces. */
    private Tetromino[] bag;

    /** The index of pieces already dealt from the current bag. */
    private int used;

    /**
     * Constructs the 7-Bag generator.
     * @param seed The random seed.
     */
    public RandomBag7TetrominoesGenerator(long seed) {
        super(seed);

        bag = new Tetromino[NUMBER_OF_TETROMINOES];
        // Initialize 'used' to the max value to force a refill on the first call
        used = NUMBER_OF_TETROMINOES;
    }

    /**
     * Gets the next piece from the current bag, refilling it if necessary.
     *
     * @param containerX Screen X offset.
     * @param containerY Screen Y offset.
     * @return The next Tetromino.
     */
    @Override
    public Tetromino getNext(int containerX, int containerY) {
        if(used >= NUMBER_OF_TETROMINOES) {
            fillBag(containerX, containerY);
        }

        used++;

        return bag[used - 1];
    }

    /**
     * Refills the bag with fresh pieces and shuffles them.
     */
    private void fillBag(int containerX, int containerY) {
        // 1. Create one of each type
        for(int i = 0; i < NUMBER_OF_TETROMINOES; i++) {
            bag[i] = TetrominoFactory.createTetromino(TetrominoType.values()[i], 0, 0, 0, containerX, containerY);
        }

        // 2. Shuffle
        shuffleBag();

        used = 0;
    }

    /**
     * Performs a Fisher-Yates shuffle on the bag array.
     */
    private void shuffleBag() {
        for(int i = 0; i < NUMBER_OF_TETROMINOES; i++) {
            swap(i, random.nextInt(NUMBER_OF_TETROMINOES));
        }
    }

    /**
     * Helper to swap elements in the bag array.
     */
    private void swap(int i, int j) {
        Tetromino aux = bag[i];
        bag[i] = bag[j];
        bag[j] = aux;
    }
}