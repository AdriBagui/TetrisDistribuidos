package tetris.boards.nes;

import tetris.boards.ReceiverBoard;
import tetris.tetrominoes.generators.TetrominoesGeneratorFactory;

import static tetris.Config.*;

public class ReceiverNESBoard extends ReceiverBoard {
    public ReceiverNESBoard(int x, int y, long seed) {
        super(x, y, BOARD_ROWS, BOARD_SPAWN_ROWS, BOARD_COLUMNS, NES_TETROMINOES_QUEUE_SIZE, TetrominoesGeneratorFactory.COMPLETELY_RANDOM, seed);
    }
}
