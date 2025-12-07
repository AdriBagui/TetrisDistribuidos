package tetris.boards.nes;

import tetris.boards.SenderBoardOutputHandler;

public class SenderNESBoardWithPhysicsSender extends NESBoardWithPhysics {
    protected SenderBoardOutputHandler updatesOutputHandler;

    public SenderNESBoardWithPhysicsSender(int x, int y, long seed, SenderBoardOutputHandler updatesOutputHandler) {
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
