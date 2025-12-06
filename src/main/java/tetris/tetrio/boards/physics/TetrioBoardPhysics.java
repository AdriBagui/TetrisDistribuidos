package tetris.tetrio.boards.physics;

import tetris.general.boards.BoardGrid;
import tetris.general.boards.physics.BoardPhysics;
import tetris.tetrio.boards.physics.rotationSystems.SuperRotationSystemPlus;
import tetris.general.tetrominoes.Tetromino;

import static tetris.Config.TETRIO_INITIAL_GRAVITY;

public class TetrioBoardPhysics extends BoardPhysics {
    public TetrioBoardPhysics(BoardGrid grid) {
        super(grid, new SuperRotationSystemPlus(grid), TETRIO_INITIAL_GRAVITY);
    }
}
