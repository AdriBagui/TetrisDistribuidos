package tetris.tetrominoes.generators;

/**
 * Enumeration of the supported random generation algorithms.
 */
public enum TetrominoesGeneratorType {
    /**
     * Emulates the classic NES Tetris RNG.
     * <p>
     * It uses a "roll + re-roll" system that discourages (but does not prevent)
     * getting the same piece twice in a row. It is purely random otherwise, meaning
     * "piece droughts" (e.g., no Long bar for 30 turns) are possible.
     * </p>
     */
    NES_TETROMINOES_GENERATOR,

    /**
     * The standard Modern Tetris RNG (7-Bag).
     * <p>
     * Pieces are dealt in "bags" of 7. Each bag contains exactly one of each
     * Tetromino type. This guarantees that you will never go more than 12 pieces
     * without seeing a specific shape.
     * </p>
     */
    RANDOM_BAG_7_TETROMINOES_GENERATOR
}