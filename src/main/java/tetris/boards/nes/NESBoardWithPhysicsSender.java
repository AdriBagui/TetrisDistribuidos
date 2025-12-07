package tetris.boards.nes;

import tetris.boards.BoardOutputHandler;

public class NESBoardWithPhysicsSender extends NESBoardWithPhysics {
    protected BoardOutputHandler updatesOutputHandler;

    public NESBoardWithPhysicsSender(int x, int y, long seed, BoardOutputHandler updatesOutputHandler) {
        super(x, y, seed);

        this.updatesOutputHandler = updatesOutputHandler;
    }

    @Override
    public void update() {
        super.update();
        updatesOutputHandler.sendUpdateMessage((byte) fallingTetromino.getX(), (byte) fallingTetromino.getY(),
                (byte) fallingTetromino.getRotationIndex(), isFallingTetrominoLocked());
    }
}
