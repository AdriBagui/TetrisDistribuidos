package tetris.boards.nes;

import tetris.boards.BoardWithPhysics;
import tetris.boards.physics.rotationSystems.RotationSystemFactory;
import tetris.tetrominoes.generators.TetrominoesGeneratorFactory;

import static tetris.Config.*;
import static tetris.Config.NES_TETROMINOES_QUEUE_SIZE;

public class NESBoardWithPhysics extends BoardWithPhysics {
    public NESBoardWithPhysics(int x, int y, long seed) {
        super(x, y, BOARD_ROWS, BOARD_SPAWN_ROWS, BOARD_COLUMNS, NES_TETROMINOES_QUEUE_SIZE, TetrominoesGeneratorFactory.COMPLETELY_RANDOM, seed, RotationSystemFactory.NES_ROTATION_SYSTEM, NES_INITIAL_GRAVITY);
    }
}
