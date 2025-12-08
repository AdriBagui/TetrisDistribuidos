package tetris.tetrominoes.generators;

/**
 * Factory class for creating {@link TetrominoesGenerator} instances.
 * <p>
 * This allows the game to easily switch between Modern (7-Bag) and Classic (NES)
 * RNG systems based on the selected game mode configuration.
 * </p>
 */
public class TetrominoesGeneratorFactory {

    /**
     * Creates a generator of the specified type.
     *
     * @param type The type of generator to create.
     * @param seed The seed to initialize the RNG with.
     * @return A concrete instance of a {@link TetrominoesGenerator}.
     */
    public static TetrominoesGenerator createTetrominoesGenerator(TetrominoesGeneratorType type, long seed) {
        return switch (type) {
            case NES_TETROMINOES_GENERATOR -> new NESTetrominoesGenerator(seed);
            case RANDOM_BAG_7_TETROMINOES_GENERATOR -> new RandomBag7TetrominoesGenerator(seed);
        };
    }
}