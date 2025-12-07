package tetris.boards.nes;

import tetris.boards.BoardWithPhysics;
import tetris.boards.components.BoardGrid;
import tetris.boards.physics.rotationSystems.RotationSystemType;
import tetris.tetrominoes.generators.TetrominoesGeneratorType;

import static tetris.boards.nes.NESConfig.*;

public class NESBoardWithPhysics extends BoardWithPhysics {
    public NESBoardWithPhysics(int x, int y, long seed) {
        super(x, y, BoardGrid.ROWS, BoardGrid.SPAWN_ROWS, BoardGrid.COLUMNS, TETROMINOES_QUEUE_SIZE,
                TetrominoesGeneratorType.NES_TETROMINOES_GENERATOR, seed, RotationSystemType.NES_ROTATION_SYSTEM,
                INITIAL_GRAVITY, LOCK_DELAY_FRAMES);
    }

    @Override
    protected void increaseLevel() {
        super.increaseLevel();

        double increment = 0;

        switch (level) {
            case 1 -> increment = 0.002778167;
            case 2 -> increment = 0.002586569;
            case 3 -> increment = 0.004414411;
            case 4 -> increment = 0.004364247;
            case 5 -> increment = 0.008081939;
            case 6 -> increment = 0.008890133;
            case 7 -> increment = 0.019396655;
            case 8 -> increment = 0.04156426;
            case 9 -> increment = 0.045720686;
            case 10 -> increment = 0.0400056;
            case 13 -> increment = 0.066676;
            case 16 -> increment = 0.133352;
            case 19 -> increment = 0.400056;
        }

        gravity.increaseGravity(increment);
    }
}
