package tetris.tetrominoes.generators;

import tetris.tetrominoes.Tetromino;
import tetris.tetrominoes.TetrominoFactory;
import tetris.tetrominoes.TetrominoType;
import static tetris.tetrominoes.Tetromino.NUMBER_OF_TETROMINOES;

/**
 * Implements the NES Tetris random generation algorithm.
 * <p>
 * <b>Algorithm Details:</b><br>
 * 1. A random number between 0 and 7 (inclusive) is generated.<br>
 * 2. If the number is 7 (dummy value) OR equal to the previously generated piece, a new roll is attempted (0-6).<br>
 * 3. The result is used as the next piece.
 * </p>
 * <p>
 * This creates a slight bias against repeats, but unlike the 7-Bag system, it is possible
 * to get "bad luck" sequences (e.g., sequences without an 'I' piece).
 * </p>
 */
public class NESTetrominoesGenerator extends TetrominoesGenerator {
    /** Stores the ordinal of the last generated piece to check for repeats. */
    private int previousTetrominoType;

    /**
     * Constructs the NES generator.
     * @param seed The random seed.
     */
    public NESTetrominoesGenerator(long seed) {
        super(seed);
        // Initialize with a value outside the valid range so the first piece is truly random.
        previousTetrominoType = NUMBER_OF_TETROMINOES;
    }

    /**
     * Generates the next piece using the NES "roll and verify" logic.
     *
     * @param containerX Screen X offset.
     * @param containerY Screen Y offset.
     * @return The next Tetromino.
     */
    @Override
    public Tetromino getNext(int containerX, int containerY) {
        // Roll 0-7 (where 7 is a dummy value intended to trigger a reroll)
        int nextTetrominoType = random.nextInt(NUMBER_OF_TETROMINOES + 1);

        // If we rolled the dummy value OR the same piece as last time...
        if (nextTetrominoType == NUMBER_OF_TETROMINOES || nextTetrominoType == previousTetrominoType) {
            // ...force a re-roll (0-6). Note: This re-roll is final, even if it matches again.
            nextTetrominoType = random.nextInt(NUMBER_OF_TETROMINOES);
        }

        previousTetrominoType = nextTetrominoType;

        return TetrominoFactory.createTetromino(TetrominoType.values()[nextTetrominoType], 0, 0, 0, containerX, containerY);
    }
}