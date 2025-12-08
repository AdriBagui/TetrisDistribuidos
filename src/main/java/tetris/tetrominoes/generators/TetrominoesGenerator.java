package tetris.tetrominoes.generators;

import tetris.tetrominoes.Tetromino;

import java.util.Random;

/**
 * Abstract base class for Tetromino generation algorithms.
 * <p>
 * This class provides the common infrastructure for random number generation seeded
 * deterministically. Subclasses must implement the specific logic for how the "Next"
 * piece is selected from the available pool.
 * </p>
 */
public abstract class TetrominoesGenerator {
    /** The random number generator instance, seeded for deterministic multiplayer. */
    protected Random random;

    /**
     * Constructs the generator with a specific seed.
     *
     * @param seed The random seed (synchronized across players in multiplayer).
     */
    public TetrominoesGenerator(long seed) {
        random = new Random(seed);
    }

    /**
     * Generates the next Tetromino in the sequence.
     *
     * @param containerX The screen X offset for the container where the piece will appear.
     * @param containerY The screen Y offset for the container where the piece will appear.
     * @return A new {@link Tetromino} instance.
     */
    public abstract Tetromino getNext(int containerX, int containerY);
}