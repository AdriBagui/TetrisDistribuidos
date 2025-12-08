package tetris.boards.io;

import client.userInterface.panels.tetris.twoPlayerPanels.TwoPlayersTetrisPanel;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Handles the encoding and transmission of game events to an output stream.
 * <p>
 * This class acts as the "server" side of the communication protocol (from the perspective
 * of the local client). It provides high-level methods to send game actions, which are
 * then serialized into the byte protocol defined in {@link BoardMessageType}.
 * </p>
 */
public class SenderBoardOutputHandler {
    private final TwoPlayersTetrisPanel twoPlayersTetrisPanel;
    private final DataOutputStream dataOutputStream;
    private boolean connectionLost;

    /**
     * Creates a new output handler.
     *
     * @param outputStream          The stream (socket or pipe) to write to.
     * @param twoPlayersTetrisPanel The game panel, used to report connection errors.
     */
    public SenderBoardOutputHandler(OutputStream outputStream, TwoPlayersTetrisPanel twoPlayersTetrisPanel) {
        this.twoPlayersTetrisPanel = twoPlayersTetrisPanel;
        this.dataOutputStream = new DataOutputStream(outputStream);
        connectionLost = false;
    }

    /**
     * Sends a command to attack the opponent with garbage lines.
     *
     * @param numberOfGarbageRows Count of lines to send.
     * @param emptyGarbageColumn  Column index for the hole.
     */
    public void sendAddGarbageMessage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        sendMessage(BoardMessageType.SEND_GARBAGE_ROWS, new byte[] {
                numberOfGarbageRows,
                emptyGarbageColumn
        });
    }

    /**
     * Sends the current position and state of the falling tetromino.
     * This is typically called every frame or every update cycle.
     *
     * @param x                        X coordinate.
     * @param y                        Y coordinate.
     * @param rotationIndex            Rotation state (0-3).
     * @param isFallingTetrominoLocked Whether the piece has locked into the grid.
     */
    public void sendUpdateMessage(byte x, byte y, byte rotationIndex, boolean isFallingTetrominoLocked) {
        sendMessage(BoardMessageType.UPDATE_FALLING_TETROMINO, new byte[] {
                x,
                y,
                rotationIndex,
                (byte) (isFallingTetrominoLocked ? 1 : 0)
        });
    }

    /**
     * Sends a signal that the local player has performed a Hold action.
     */
    public void sendHoldMessage() {
        sendMessage(BoardMessageType.UPDATE_TETROMINO_HOLDER, null);
    }

    /**
     * Sends an update about the garbage currently pending in the local buffer.
     *
     * @param numberOfGarbageRows Count of pending lines.
     * @param emptyGarbageColumn  Column index for the hole.
     */
    public void sendUpdateGarbageMessage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        sendMessage(BoardMessageType.UPDATE_GARBAGE_ROWS, new byte[] {
                numberOfGarbageRows,
                emptyGarbageColumn
        });
    }

    /**
     * Marks the connection as broken to prevent further write attempts.
     */
    public void notifyConnectionLost() { this.connectionLost = true; }

    // ---------------------------------------------------------------------------------
    // Auxiliary methods

    /**
     * Thread-safe method to serialize and write a message to the stream.
     *
     * @param action  The message type.
     * @param content The payload bytes (can be null for signal-only messages).
     */
    private synchronized void sendMessage(BoardMessageType action, byte[] content) {
        // If the connection is known to be dead, do not attempt to write.
        // This allows games (like NES mode) to continue locally even if the opponent disconnects.
        if (connectionLost) return;

        try {
            // Write Message Type ID
            dataOutputStream.writeByte((byte) action.ordinal());

            // Write Payload
            if (content != null) {
                for (byte b : content) {
                    dataOutputStream.writeByte(b);
                }
            }

            dataOutputStream.flush();
        }
        catch (IOException ioe) {
            // If writing fails, we assume the connection is broken.
            // We notify the panel to handle the error UI/logic.
            twoPlayersTetrisPanel.handleConnectionError();
        }
    }
}