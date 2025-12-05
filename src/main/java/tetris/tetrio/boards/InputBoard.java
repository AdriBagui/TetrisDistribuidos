package tetris.tetrio.boards;

import tetris.general.boards.Board;
import tetris.general.boards.BoardGrid;
import tetris.general.boards.physics.BoardPhysics;
import tetris.general.boards.physics.OnlineBoardPhysics;

import java.io.InputStream;

public class InputBoard extends Board {
    private InputStream inputStream;
    private int garbageLinesRecieved;
    private int emptyGarbageColumnRecieved;

    public InputBoard(int x, int y, BoardGrid grid, long seed, InputStream inputStream) {
        this.inputStream = inputStream;
        super(x, y, grid, seed);
        garbageLinesRecieved = 0;
        emptyGarbageColumnRecieved = 0;
    }

    @Override
    public void update() {
        super.update();
//        garbageLinesRecieved = onlineBoardPhysics.getGarbageLinesRecieved();
//        emptyGarbageColumnRecieved = onlineBoardPhysics.getEmptyGarbageColumnRecieved();

//        if (garbageLinesRecieved > 0) enemyBoard.addGarbage(garbageLinesRecieved, emptyGarbageColumnRecieved);
//
//        numberOfGarbageRowsToAdd = onlineBoardPhysics.getGarbageLinesToUpdate();
//        emptyGarbageColumn = onlineBoardPhysics.getEmptyGarbageColumnToUpdate();
    }

    @Override
    protected void updateFallingTetromino() {

    }
}
