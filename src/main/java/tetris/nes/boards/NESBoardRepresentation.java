package tetris.nes.boards;

import tetris.general.boards.BoardRepresentation;
import tetris.general.boards.physics.BoardPhysicsFactory;
import tetris.general.tetrominoes.generators.TetrominoesGeneratorFactory;

import static tetris.Config.*;

public class NESBoardRepresentation extends BoardRepresentation {
    public NESBoardRepresentation(int x, int y, long seed) {
        super(x, y, BOARD_ROWS, BOARD_SPAWN_ROWS, BOARD_COLUMNS, NES_TETROMINOES_QUEUE_SIZE, TetrominoesGeneratorFactory.COMPLETELY_RANDOM, seed, false);
    }
}
