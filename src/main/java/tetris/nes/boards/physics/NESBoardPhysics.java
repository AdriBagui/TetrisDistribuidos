package tetris.nes.boards.physics;

import tetris.general.boards.BoardGrid;
import tetris.general.boards.physics.BoardPhysics;
import tetris.nes.boards.physics.rotationSystems.NESRotationSystem;
import static tetris.Config.*;

public class NESBoardPhysics extends BoardPhysics {
    public NESBoardPhysics(BoardGrid grid) {
        super(grid, new NESRotationSystem(grid), NES_INITIAL_GRAVITY);
    }
}
