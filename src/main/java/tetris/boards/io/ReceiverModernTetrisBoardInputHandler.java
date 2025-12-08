package tetris.boards.io;

import tetris.boards.modernTetris.ReceiverModernTetrisBoard;
import tetris.boards.modernTetris.SenderModernTetrisBoardWithPhysics;
import client.userInterface.panels.tetris.twoPlayerPanels.TwoPlayersTetrisPanel;

import java.io.IOException;
import java.io.InputStream;

/**
 * An extended input handler for Modern Tetris games.
 * <p>
 * This class adds support for Modern Tetris specific features:
 * <ul>
 * <li><b>Garbage Reception:</b> Reading {@code SEND_GARBAGE_ROWS} messages to add lines to the local player's board.</li>
 * <li><b>Hold Queue:</b> Reading {@code UPDATE_TETROMINO_HOLDER} messages to visualize the opponent's hold action.</li>
 * <li><b>Garbage Updates:</b> Reading {@code UPDATE_GARBAGE_ROWS} to visualize pending garbage on the opponent's board.</li>
 * </ul>
 * </p>
 */
public class ReceiverModernTetrisBoardInputHandler extends ReceiverBoardInputHandler {
    /** The local player's board (used to receive garbage attacks). */
    private final SenderModernTetrisBoardWithPhysics garbageReceiverBoard;

    /** The remote player's visual representation (used to update their UI state). */
    private final ReceiverModernTetrisBoard updatesReceiverModernTetrisBoard;

    /**
     * Creates a Modern Tetris input handler.
     *
     * @param boardsInputReceiver              The input stream.
     * @param garbageReceiverBoard             The local board that will <i>receive</i> garbage sent by the opponent.
     * @param updatesReceiverModernTetrisBoard The remote board representation to update with visuals.
     * @param twoPlayersTetrisPanel            The game panel controller.
     */
    public ReceiverModernTetrisBoardInputHandler(InputStream boardsInputReceiver, SenderModernTetrisBoardWithPhysics garbageReceiverBoard, ReceiverModernTetrisBoard updatesReceiverModernTetrisBoard, TwoPlayersTetrisPanel twoPlayersTetrisPanel) {
        super(boardsInputReceiver, updatesReceiverModernTetrisBoard, twoPlayersTetrisPanel);
        this.updatesReceiverModernTetrisBoard = updatesReceiverModernTetrisBoard;
        this.garbageReceiverBoard = garbageReceiverBoard;
    }

    /**
     * Extends the base message handling to support Modern Tetris actions.
     *
     * @param action The message type to process.
     * @throws IOException If reading the payload fails.
     */
    @Override
    protected void sendMessageToBoard(BoardMessageType action) throws IOException {
        byte numberOfGarbageRowsToAdd, emptyGarbageColumnToAdd;
        byte numberOfGarbageRowsToUpdate, emptyGarbageColumnToUpdate;

        // Process standard movement updates first
        super.sendMessageToBoard(action);

        switch (action) {
            case SEND_GARBAGE_ROWS:
                // Opponent sent an attack. Read lines and hole position, then apply to local board.
                numberOfGarbageRowsToAdd = boardsInputReceiver.readByte();
                emptyGarbageColumnToAdd = boardsInputReceiver.readByte();
                garbageReceiverBoard.addGarbage(numberOfGarbageRowsToAdd, emptyGarbageColumnToAdd);
                break;

            case UPDATE_TETROMINO_HOLDER:
                // Opponent performed a hold. Update their visual board.
                if (updatesReceiverModernTetrisBoard != null) {
                    updatesReceiverModernTetrisBoard.hold();
                }
                break;

            case UPDATE_GARBAGE_ROWS:
                // Update the visual indicator of pending garbage on the opponent's board.
                numberOfGarbageRowsToUpdate = boardsInputReceiver.readByte();
                emptyGarbageColumnToUpdate = boardsInputReceiver.readByte();
                if (updatesReceiverModernTetrisBoard != null) {
                    updatesReceiverModernTetrisBoard.addGarbage(numberOfGarbageRowsToUpdate, emptyGarbageColumnToUpdate);
                }
                break;
        }
    }
}