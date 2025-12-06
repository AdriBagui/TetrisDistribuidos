package tetris.tetrio.boards;

import tetris.general.boards.BoardRepresentation;
import tetris.general.tetrominoes.generators.TetrominoesGeneratorFactory;

import static tetris.Config.*;

public class TetrioBoardRepresentation extends BoardRepresentation {
    protected GarbageRecievingSystem garbageRecievingSystem;

    public TetrioBoardRepresentation(int x, int y, long seed) {
        super(x, y, BOARD_ROWS, BOARD_SPAWN_ROWS, BOARD_COLUMNS, TETRIO_TETROMINOES_QUEUE_SIZE, TetrominoesGeneratorFactory.BAG_WITH_7, seed, true);
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
