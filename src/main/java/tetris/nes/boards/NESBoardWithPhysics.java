package tetris.nes.boards;

import tetris.general.boards.BoardGrid;
import tetris.general.boards.BoardWithPhysics;
import tetris.general.boards.physics.BoardPhysicsFactory;
import tetris.general.tetrominoes.generators.TetrominoesGeneratorFactory;
import tetris.tetrio.boards.physics.TetrioBoardPhysics;

import static tetris.Config.*;
import static tetris.Config.NES_TETROMINOES_QUEUE_SIZE;

public class NESBoardWithPhysics extends BoardWithPhysics {
    public NESBoardWithPhysics(int x, int y, long seed) {
        super(x, y, BOARD_ROWS, BOARD_SPAWN_ROWS, BOARD_COLUMNS, NES_TETROMINOES_QUEUE_SIZE, TetrominoesGeneratorFactory.COMPLETELY_RANDOM, seed, false, BoardPhysicsFactory.NES_BOARD_PHYSICS);
    }
}
