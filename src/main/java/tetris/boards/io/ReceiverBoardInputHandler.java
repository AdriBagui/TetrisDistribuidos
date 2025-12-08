package tetris.boards.io;

import tetris.boards.ReceiverBoard;
import client.userInterface.panels.tetris.twoPlayerPanels.TwoPlayersTetrisPanel;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * A specialized Thread that listens for incoming game updates from an input stream.
 * <p>
 * This class acts as the "client" side of the communication protocol. It continually reads
 * bytes from the stream, determines the message type using {@link BoardMessageType}, and
 * delegates the action to the {@link ReceiverBoard} to update the visual state.
 * </p>
 */
public class ReceiverBoardInputHandler extends Thread {
    /** The input stream wrapper for reading primitive data types. */
    protected final DataInputStream boardsInputReceiver;

    /** The local representation of the remote board to update. */
    protected final ReceiverBoard updatesReceiverBoard;

    /** The game panel, used to handle connection lifecycle events (errors, closure). */
    protected final TwoPlayersTetrisPanel twoPlayersTetrisPanel;

    /**
     * Creates a new input handler thread.
     *
     * @param boardsInputReceiver   The raw input stream (socket or pipe) to read from.
     * @param updatesReceiverBoard  The board instance to update with received data.
     * @param twoPlayersTetrisPanel The main game panel controller.
     */
    public ReceiverBoardInputHandler(InputStream boardsInputReceiver, ReceiverBoard updatesReceiverBoard, TwoPlayersTetrisPanel twoPlayersTetrisPanel) {
        this.boardsInputReceiver = new DataInputStream(boardsInputReceiver);
        this.updatesReceiverBoard = updatesReceiverBoard;
        this.twoPlayersTetrisPanel = twoPlayersTetrisPanel;
    }

    /**
     * The main listening loop.
     * <p>
     * Continuously reads message types and payloads. If the stream closes (EOF) or breaks,
     * it notifies the panel to handle the disconnection.
     * </p>
     */
    @Override
    public void run() {
        BoardMessageType action;

        // The loop terminates naturally when readMessageType() throws an exception (EOF or IO)
        try {
            while (true) {
                action = readMessageType();
                sendMessageToBoard(action);
            }
        }
        catch (EOFException eofe) {
            // Stream closed normally (e.g., opponent quit or game ended)
            twoPlayersTetrisPanel.closeCommunications();
        }
        catch (IOException ioe) {
            // Stream broke unexpectedly
            twoPlayersTetrisPanel.handleConnectionError();
        }
    }

    /**
     * Reads a single byte and converts it to the corresponding enum constant.
     *
     * @return The message type.
     * @throws IOException If an I/O error occurs.
     */
    protected BoardMessageType readMessageType() throws IOException {
        return BoardMessageType.values()[boardsInputReceiver.readByte()];
    }

    /**
     * Parses the payload for a specific action and updates the board.
     * <p>
     * The base implementation only handles {@link BoardMessageType#UPDATE_FALLING_TETROMINO}.
     * Subclasses should override this to handle additional message types.
     * </p>
     *
     * @param action The message type to process.
     * @throws IOException If reading the payload fails.
     */
    protected void sendMessageToBoard(BoardMessageType action) throws IOException {
        byte x, y, rotationIndex;
        boolean isLocked;

        if (action == BoardMessageType.UPDATE_FALLING_TETROMINO) {
            // Protocol: [X] [Y] [ROT] [LOCK_BOOL]
            x = boardsInputReceiver.readByte();
            y = boardsInputReceiver.readByte();
            rotationIndex = boardsInputReceiver.readByte();
            isLocked = (boardsInputReceiver.readByte() == 1);

            if (updatesReceiverBoard != null)  {
                updatesReceiverBoard.setFallingTetrominoXYRotationIndex(x, y, rotationIndex);
                if (isLocked) updatesReceiverBoard.lockFallingTetromino();
                updatesReceiverBoard.update();
            }
        }
    }
}