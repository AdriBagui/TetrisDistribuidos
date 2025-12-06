package tetris.general.boards;

public abstract class BoardRepresentation extends Board {
    private int fallingTetrominoNextX, fallingTetrominoNextY, fallingTetrominoNextRotationIndex;
    private boolean isFallingTetrominoLocked;

    public BoardRepresentation(int x, int y, int gridRows, int gridSpawnRows, int gridColumns, int tetrominoesQueueSize, int tetrominoesQueueGeneratorType, long seed, boolean hasHolder) {
        super(x, y, gridRows, gridSpawnRows, gridColumns, tetrominoesQueueSize, tetrominoesQueueGeneratorType, seed, hasHolder);

        fallingTetrominoNextX = -128;
        fallingTetrominoNextY = -128;
        fallingTetrominoNextRotationIndex = -1;
        isFallingTetrominoLocked = false;
    }

    public void setFallingTetrominoXYRotationIndex(int x, int y, int rotationIndex) {
        this.fallingTetrominoNextX = x;
        this.fallingTetrominoNextY = y;
        this.fallingTetrominoNextRotationIndex = rotationIndex;
    }

    public void lockFallingTetromino() { isFallingTetrominoLocked = true; }

    @Override
    protected boolean isFallingTetrominoLocked() {
        return isFallingTetrominoLocked;
    }

    @Override
    protected void updateFallingTetromino() {
        fallingTetromino.setXYRotationIndex(fallingTetrominoNextX, fallingTetrominoNextY, fallingTetrominoNextRotationIndex);
    }
    @Override
    protected void setNextTetrominoAsFallingTetromino() {
        super.setNextTetrominoAsFallingTetromino();
        isFallingTetrominoLocked = false;
    }
}
