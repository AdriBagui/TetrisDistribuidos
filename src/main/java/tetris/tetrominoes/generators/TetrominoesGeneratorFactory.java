package tetris.tetrominoes.generators;

public class TetrominoesGeneratorFactory {
    public static final int COMPLETELY_RANDOM = 0;
    public static final int BAG_WITH_7 = 1;

    public static TetrominoesGenerator createTetrominoesGenerator(int type, long seed) {
        TetrominoesGenerator tetrominoesGenerator = null;

        switch (type) {
            case COMPLETELY_RANDOM:
                tetrominoesGenerator = new CompletelyRandomTetrominoesGenerator(seed);
                break;
            case BAG_WITH_7:
                tetrominoesGenerator = new RandomBagTetrominoesGenerator(seed);
                break;
        }

        return tetrominoesGenerator;
    }
}
