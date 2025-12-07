package tetris.boards.tetrio;

import tetris.boards.BoardOutputHandler;

import java.io.IOException;

import static tetris.Config.BOARD_COLUMNS;

public class TetrioBoardWithPhysicsSender extends TetrioBoardWithPhysics {
    protected BoardOutputHandler garbageAndUpdatesOutputHandler;

    public TetrioBoardWithPhysicsSender(int x, int y, long seed, BoardOutputHandler garbageAndUpdatesOutputHandler) {
        super(x, y, seed);

        this.garbageAndUpdatesOutputHandler = garbageAndUpdatesOutputHandler;
    }

    @Override
    public void hold() {
        super.hold();
        garbageAndUpdatesOutputHandler.sendHoldMessage();
    }

    @Override
    public void addGarbage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        super.addGarbage(numberOfGarbageRows, emptyGarbageColumn);
        garbageAndUpdatesOutputHandler.sendUpdateGarbageMessage(numberOfGarbageRows, emptyGarbageColumn);
    }

    @Override
    public void update() {
        super.update();
        garbageAndUpdatesOutputHandler.sendUpdateMessage((byte) fallingTetromino.getX(), (byte) fallingTetromino.getY(),
                (byte) fallingTetromino.getRotationIndex(), isFallingTetrominoLocked());
    }

    @Override
    protected int lockFallingTetrominoAndClearLines() {
        int clearedLines = super.lockFallingTetrominoAndClearLines();

        switch (clearedLines) {
            case 2:
                garbageAndUpdatesOutputHandler.sendAddGarbageMessage((byte) 1, (byte) garbageRandom.nextInt(BOARD_COLUMNS));
                break;
            case 3:
                garbageAndUpdatesOutputHandler.sendAddGarbageMessage((byte) 2, (byte) garbageRandom.nextInt(BOARD_COLUMNS));
                break;
            case 4:
                garbageAndUpdatesOutputHandler.sendAddGarbageMessage((byte) 4, (byte) garbageRandom.nextInt(BOARD_COLUMNS));
                break;
        }

        return clearedLines;
    }
}
