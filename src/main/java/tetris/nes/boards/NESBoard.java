package tetris.nes.boards;

import tetris.general.boards.Board;
import tetris.general.boards.BoardGrid;
import tetris.general.boards.BoardWithPhysics;
import tetris.general.boards.physics.BoardPhysics;
import tetris.tetrio.boards.physics.TetrioBoardPhysics;

public class NESBoard extends BoardWithPhysics {
    public NESBoard(int x, int y, BoardGrid grid, long seed) {
        super(x, y, grid, seed, new TetrioBoardPhysics(grid));
    }

    @Override
    public void moveLeftPressed() {

    }

    @Override
    public void moveLeftReleased() {

    }

    @Override
    public void moveRightPressed() {

    }

    @Override
    public void moveRightReleased() {

    }

    @Override
    public void softDropPressed() {

    }

    @Override
    public void softDropReleased() {

    }

    @Override
    public void hardDropPressed() {

    }

    @Override
    public void hardDropReleased() {

    }

    @Override
    public void rotateRightPressed() {

    }

    @Override
    public void rotateRightReleased() {

    }

    @Override
    public void rotateLeftPressed() {

    }

    @Override
    public void rotateLeftReleased() {

    }

    @Override
    public void flipPressed() {

    }

    @Override
    public void flipReleased() {

    }
}
