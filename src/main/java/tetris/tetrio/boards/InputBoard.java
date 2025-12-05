package tetris.tetrio.boards;

import tetris.general.boards.Board;
import tetris.general.boards.physics.BoardPhysics;
import tetris.general.boards.physics.OnlineBoardPhysics;

import java.io.InputStream;

public class InputBoard extends Board {
    private InputStream inputStream;
    private final OnlineBoardPhysics onlineBoardPhysics;
    private int garbageLinesRecieved;
    private int emptyGarbageColumnRecieved;

    public InputBoard(int x, int y, long seed, InputStream inputStream) {
        this.inputStream = inputStream;
        super(x, y, seed);
        onlineBoardPhysics = (OnlineBoardPhysics) boardPhysics;
        garbageLinesRecieved = 0;
        emptyGarbageColumnRecieved = 0;
    }

    @Override
    protected BoardPhysics initializeBoardPhysics() {
        return new OnlineBoardPhysics(grid, this, inputStream);
    }

    @Override
    public void update() {
        super.update();
        garbageLinesRecieved = onlineBoardPhysics.getGarbageLinesRecieved();
        emptyGarbageColumnRecieved = onlineBoardPhysics.getEmptyGarbageColumnRecieved();

        //if (garbageLinesRecieved > 0) enemyBoard.addGarbage(garbageLinesRecieved, emptyGarbageColumnRecieved);

        //numberOfGarbageRowsToAdd = onlineBoardPhysics.getGarbageLinesToUpdate();
        //emptyGarbageColumn = onlineBoardPhysics.getEmptyGarbageColumnToUpdate();
    }
}
