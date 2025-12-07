package tetris.tetrominoes.generators;

public class TetrominoesGeneratorFactory {
    public static TetrominoesGenerator createTetrominoesGenerator(TetrominoesGeneratorType type, long seed) {
        return switch (type) {
            case NES_TETROMINOES_GENERATOR -> new NESTetrominoesGenerator(seed);
            case RANDOM_BAG_7_TETROMINOES_GENERATOR -> new RandomBag7TetrominoesGenerator(seed);
        };
    }
}
