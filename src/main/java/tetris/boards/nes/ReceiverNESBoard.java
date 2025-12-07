package tetris.boards.nes;

import tetris.boards.ReceiverBoard;
import tetris.boards.components.BoardGrid;
import tetris.tetrominoes.generators.TetrominoesGeneratorType;

import static tetris.boards.nes.NESConfig.TETROMINOES_QUEUE_SIZE;

public class ReceiverNESBoard extends ReceiverBoard {
    public ReceiverNESBoard(int x, int y, long seed) {
        super(x, y, BoardGrid.ROWS, BoardGrid.SPAWN_ROWS, BoardGrid.COLUMNS, TETROMINOES_QUEUE_SIZE,
                TetrominoesGeneratorType.NES_TETROMINOES_GENERATOR, seed);
    }
}
