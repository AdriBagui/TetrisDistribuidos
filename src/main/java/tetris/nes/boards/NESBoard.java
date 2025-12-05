package tetris.nes.boards;

import tetris.general.boards.Board;
import tetris.general.boards.physics.BoardPhysics;

public class NESBoard extends Board {
    public NESBoard(int x, int y, long seed) {
        super(x, y, seed);
    }

    @Override
    protected BoardPhysics initializeBoardPhysics() {
        return null;
    }
}
