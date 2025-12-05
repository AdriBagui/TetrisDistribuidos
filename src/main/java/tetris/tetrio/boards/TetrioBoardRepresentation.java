package tetris.tetrio.boards;

import tetris.general.boards.BoardGrid;
import tetris.general.boards.BoardRepresentation;

public class TetrioBoardRepresentation extends BoardRepresentation {
    protected GarbageRecievingSystem garbageRecievingSystem;

    public TetrioBoardRepresentation(int x, int y, BoardGrid grid, long seed) {
        super(x, y, grid, seed);
    }

    public void addGarbage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        garbageRecievingSystem.addGarbage(numberOfGarbageRows, emptyGarbageColumn);
    }
}
