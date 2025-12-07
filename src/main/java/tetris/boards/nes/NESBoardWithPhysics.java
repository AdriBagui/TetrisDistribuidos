package tetris.boards.nes;

import tetris.boards.BoardWithPhysics;
import tetris.boards.physics.rotationSystems.RotationSystemFactory;
import tetris.tetrominoes.generators.TetrominoesGeneratorFactory;

import static tetris.Config.*;
import static tetris.Config.NES_TETROMINOES_QUEUE_SIZE;

public class NESBoardWithPhysics extends BoardWithPhysics {
    public NESBoardWithPhysics(int x, int y, long seed) {
        super(x, y, BOARD_ROWS, BOARD_SPAWN_ROWS, BOARD_COLUMNS, NES_TETROMINOES_QUEUE_SIZE, TetrominoesGeneratorFactory.COMPLETELY_RANDOM, seed, RotationSystemFactory.NES_ROTATION_SYSTEM, NES_INITIAL_GRAVITY, 1);
    }

    @Override
    public void increaseLevel() {
        super.increaseLevel();

        double increment = 0;

        switch (level) {
            case 1 -> increment = 0.002329411;
            case 2 -> increment = 0.002942414;
            case 3 -> increment = 0.003834054;
            case 4 -> increment = 0.005203359;
            case 5 -> increment = 0.007465689;
            case 6 -> increment = 0.011613295;
            case 7 -> increment = 0.020546598;
            case 8 -> increment = 0.046229846;
            case 9 -> increment = 0.040065867;
            case 10 -> increment = 0.032052693;
            case 13 -> increment = 0.04807904;
            case 16 -> increment = 0.080131733;
            case 19 -> increment = 0.160263467;
            case 29 -> increment = 0.4807904;
        }

        gravity.increaseGravity(increment);
    }
}
