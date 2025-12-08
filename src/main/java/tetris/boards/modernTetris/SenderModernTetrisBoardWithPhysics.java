package tetris.boards.modernTetris;

import tetris.boards.io.SenderBoardOutputHandler;

/**
 * A Modern Tetris board that broadcasts its state to an output stream.
 * <p>
 * This class extends the playable physics board to intercept key game events (movement,
 * locking, garbage generation) and send them via the {@link SenderBoardOutputHandler}.
 * This is used for the "Local Player" in a multiplayer match.
 * </p>
 */
public class SenderModernTetrisBoardWithPhysics extends ModernTetrisBoardWithPhysics {
    /** The handler responsible for encoding and sending messages. */
    protected SenderBoardOutputHandler garbageAndUpdatesOutputHandler;

    /**
     * Constructs a sender board.
     *
     * @param x                              X screen coordinate.
     * @param y                              Y screen coordinate.
     * @param seed                           Random seed.
     * @param garbageAndUpdatesOutputHandler The output handler for network communication.
     */
    public SenderModernTetrisBoardWithPhysics(int x, int y, long seed, SenderBoardOutputHandler garbageAndUpdatesOutputHandler) {
        super(x, y, seed);
        this.garbageAndUpdatesOutputHandler = garbageAndUpdatesOutputHandler;
    }

    /**
     * Intercepts the hold action to notify the opponent.
     */
    @Override
    public void hold() {
        super.hold();
        garbageAndUpdatesOutputHandler.sendHoldMessage();
    }

    /**
     * Intercepts garbage reception to notify the opponent (so they see the garbage appear on your board).
     */
    @Override
    public void addGarbage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        super.addGarbage(numberOfGarbageRows, emptyGarbageColumn);
        garbageAndUpdatesOutputHandler.sendUpdateGarbageMessage(numberOfGarbageRows, emptyGarbageColumn);
    }

    /**
     * Intercepts the update loop to broadcast the falling piece's position every frame.
     */
    @Override
    public void update() {
        super.update();
        garbageAndUpdatesOutputHandler.sendUpdateMessage(
                (byte) fallingTetromino.getX(),
                (byte) fallingTetromino.getY(),
                (byte) fallingTetromino.getRotationIndex(),
                isFallingTetrominoLocked()
        );
    }

    /**
     * Intercepts line clears to generate garbage attacks sent to the opponent.
     * <p>
     * <b>Garbage Rules:</b>
     * <ul>
     * <li>2 Lines cleared -> Send 1 Line</li>
     * <li>3 Lines cleared -> Send 2 Lines</li>
     * <li>4 Lines cleared (Tetris) -> Send 4 Lines</li>
     * </ul>
     * </p>
     */
    @Override
    protected int lockFallingTetrominoAndClearLines() {
        int clearedLines = super.lockFallingTetrominoAndClearLines();

        // Check if attack garbage should be sent
        switch (clearedLines) {
            case 2:
                garbageAndUpdatesOutputHandler.sendAddGarbageMessage((byte) 1, (byte) garbageRandom.nextInt(grid.getNumberOfColumns()));
                break;
            case 3:
                garbageAndUpdatesOutputHandler.sendAddGarbageMessage((byte) 2, (byte) garbageRandom.nextInt(grid.getNumberOfColumns()));
                break;
            case 4:
                garbageAndUpdatesOutputHandler.sendAddGarbageMessage((byte) 4, (byte) garbageRandom.nextInt(grid.getNumberOfColumns()));
                break;
        }

        return clearedLines;
    }
}