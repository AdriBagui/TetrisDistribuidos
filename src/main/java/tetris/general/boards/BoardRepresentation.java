package tetris.general.boards;

public class BoardRepresentation extends Board {
    private int fallingTetrominoNextX, fallingTetrominoNextY, fallingTetrominoNextRotationIndex;

    public BoardRepresentation(int x, int y, BoardGrid grid, long seed) {
        super(x, y, grid, seed);
    }

    @Override
    protected void updateFallingTetromino() {

    }

    public void setFallingTetrominoNextRotationIndex(int fallingTetrominoNextRotationIndex) {
        this.fallingTetrominoNextRotationIndex = fallingTetrominoNextRotationIndex;
    }
}
