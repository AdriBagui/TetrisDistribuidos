package tetris.general.boards;

public class BoardRepresentation extends Board {
    private int fallingTetrominoNextX, fallingTetrominoNextY, fallingTetrominoNextRotationIndex;

    public BoardRepresentation(int x, int y, BoardGrid grid, long seed) {
        super(x, y, grid, seed);
    }

    @Override
    protected void updateFallingTetromino() {
        fallingTetromino.setXYRotationIndex(fallingTetrominoNextX, fallingTetrominoNextY, fallingTetrominoNextRotationIndex);
    }

    public void setFallingTetrominoXYRotationIndex(int x, int y, int rotationIndex) {
        this.fallingTetrominoNextX = x;
        this.fallingTetrominoNextY = y;
        this.fallingTetrominoNextRotationIndex = rotationIndex;
    }
}
