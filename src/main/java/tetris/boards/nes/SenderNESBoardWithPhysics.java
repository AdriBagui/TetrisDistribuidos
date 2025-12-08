package tetris.boards.nes;

import tetris.boards.io.SenderBoardOutputHandler;

public class SenderNESBoardWithPhysics extends NESBoardWithPhysics {
    protected SenderBoardOutputHandler updatesOutputHandler;

    public SenderNESBoardWithPhysics(int x, int y, long seed, SenderBoardOutputHandler updatesOutputHandler) {
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
