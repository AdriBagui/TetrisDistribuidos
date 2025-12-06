package tetris.tetrio.boards;

import tetris.general.boards.BoardGrid;
import tetris.general.boards.BoardRepresentation;

public class TetrioBoardRepresentation extends BoardRepresentation {
    protected GarbageRecievingSystem garbageRecievingSystem;

    public TetrioBoardRepresentation(int x, int y, BoardGrid grid, long seed) {
        super(x, y, grid, seed);
        garbageRecievingSystem = new GarbageRecievingSystem();
    }

    public void addGarbage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        garbageRecievingSystem.addGarbage(numberOfGarbageRows, emptyGarbageColumn);
    }

    @Override
    public void update() {
        super.update();

        if (garbageRecievingSystem.updateGarbage(grid, fallingTetromino, isFallingTetrominoLocked()))
            isAlive = false;
    }
}
