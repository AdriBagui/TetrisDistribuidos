package tetris.general.boards.physics;

import tetris.general.boards.BoardGrid;
import tetris.general.tetrominoes.*;
import tetris.nes.boards.physics.NESBoardPhysics;
import tetris.tetrio.boards.physics.TetrioBoardPhysics;

import static tetris.Config.*;
import static tetris.Config.J;
import static tetris.Config.L;
import static tetris.Config.S;
import static tetris.Config.Z;

public class BoardPhysicsFactory {
    public static final int NES_BOARD_PHYSICS = 0;
    public static final int TETRIO_BOARD_PHYSICS = 1;

    public static BoardPhysics createBoardPhysics(int type, BoardGrid grid) {
        BoardPhysics boardPhysics = null;

        switch (type) {
            case NES_BOARD_PHYSICS:
                boardPhysics = new NESBoardPhysics(grid);
                break;
            case TETRIO_BOARD_PHYSICS:
                boardPhysics = new TetrioBoardPhysics(grid);
                break;
        }

        return boardPhysics;
    }
}
