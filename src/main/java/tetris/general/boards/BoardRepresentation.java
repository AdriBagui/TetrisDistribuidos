package tetris.general.boards;

public class BoardRepresentation extends Board {
    private int fallingTetrominoNextX, fallingTetrominoNextY, fallingTetrominoNextRotationIndex;
    private boolean isFallingTetrominoLocked;

    public BoardRepresentation(int x, int y, BoardGrid grid, long seed) {
        super(x, y, grid, seed);

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
}
