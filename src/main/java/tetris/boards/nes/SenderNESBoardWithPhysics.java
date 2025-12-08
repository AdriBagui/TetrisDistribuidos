package tetris.boards.nes;

import tetris.boards.io.SenderBoardOutputHandler;

/**
 * An NES Tetris board that broadcasts its state to an output stream.
 * <p>
 * This class extends the playable physics board to intercept key game events and send them
 * via the network. Note that NES Tetris in this implementation is typically score-based
 * (no garbage sending), so this class primarily syncs position and locking state.
 * </p>
 */
public class SenderNESBoardWithPhysics extends NESBoardWithPhysics {
    /** The handler responsible for encoding and sending messages. */
    protected SenderBoardOutputHandler updatesOutputHandler;

    /**
     * Constructs a sender board.
     *
     * @param x                    The X screen coordinate.
     * @param y                    The Y screen coordinate.
     * @param seed                 The random seed.
     * @param updatesOutputHandler The output handler for network communication.
     */
    public SenderNESBoardWithPhysics(int x, int y, long seed, SenderBoardOutputHandler updatesOutputHandler) {
        super(x, y, seed);
        this.updatesOutputHandler = updatesOutputHandler;
    }

    /**
     * Intercepts the update loop to broadcast the falling piece's state.
     * This ensures the opponent sees smooth movement of the local player's piece.
     */
    @Override
    public void update() {
        super.update();
        updatesOutputHandler.sendUpdateMessage(
                (byte) fallingTetromino.getX(),
                (byte) fallingTetromino.getY(),
                (byte) fallingTetromino.getRotationIndex(),
                isFallingTetrominoLocked()
        );
    }
}