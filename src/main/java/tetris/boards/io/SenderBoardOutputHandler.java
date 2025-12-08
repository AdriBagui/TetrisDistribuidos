package tetris.boards.io;

import client.userInterface.panels.tetris.twoPlayerPanels.TwoPlayersPanel;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SenderBoardOutputHandler {
    /*
     * Protocol Definition:
     * Messages: TetrioBoardWithPhysics messages
     * - SEND_GARBAGE_ROWS: followed by 2 bytes [LINES, EMPTY_COLUMN]
     * Messages: Updates to representations
     * - UPDATE_FALLING_TETROMINO: followed by 4 bytes [X, Y, ROT, LOCK]
     * - UPDATE_TETROMINO_HOLDER: no payload
     * - UPDATE_GARBAGE_ROWS: followed by 2 bytes [LINES, EMPTY_COLUMN]
     */

    private TwoPlayersPanel twoPlayersPanel;
    private DataOutputStream dataOutputStream;
    private boolean connectionLost;

    /**
     * Handles sending encoded game events over an output stream (e.g., network or pipe).
     * @param outputStream the destination stream.
     */
    public SenderBoardOutputHandler(OutputStream outputStream, TwoPlayersPanel twoPlayersPanel) {
        this.twoPlayersPanel = twoPlayersPanel;
        this.dataOutputStream = new DataOutputStream(outputStream);
        connectionLost = false;
    }

    /**
     * Sends a command to add garbage lines to the opponent.
     * Protocol: [SEND_GARBAGE] [Lines] [Empty Column]
     * @param numberOfGarbageRows number of lines to add.
     * @param emptyGarbageColumn the column index that is empty in the garbage lines.
     */
    public void sendAddGarbageMessage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        sendMessage(BoardMessageType.SEND_GARBAGE_ROWS, new byte[] {
                numberOfGarbageRows,
                emptyGarbageColumn
        });
    }

    /**
     * Sends the current state of the falling tetromino.
     * Protocol: [UPDATE_FALLING_TETROMINO_POSITION] [X] [Y] [Rotation] [LockStatus]
     * @param x x coordinate of the piece.
     * @param y y coordinate of the piece.
     * @param rotationIndex rotation state.
     * @param isFallingTetrominoLocked true if the piece has locked.
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
     * Sends a notification that the player used the hold function.
     * Protocol: [UPDATE_TETROMINO_HOLDER]
     */
    public void sendHoldMessage() { sendMessage(BoardMessageType.UPDATE_TETROMINO_HOLDER, null); }

    /**
     * Sends an update about garbage lines waiting in the buffer.
     * Protocol: [UPDATE_GARBAGE_ROWS] [Lines] [Empty Column]
     * @param numberOfGarbageRows number of pending garbage rows.
     * @param emptyGarbageColumn the empty column index.
     */
    public void sendUpdateGarbageMessage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        sendMessage(BoardMessageType.UPDATE_GARBAGE_ROWS, new byte[] {
                numberOfGarbageRows,
                emptyGarbageColumn
        });
    }

    public void notifyConnectionLost() { this.connectionLost = true; }

    // ---------------------------------------------------------------------------------
    // Auxiliary methods

    /**
     * Thread-safe method to write bytes to the stream.
     * @param action the protocol action byte.
     * @param content the payload bytes (can be null).
     */
    private synchronized void sendMessage(BoardMessageType action, byte[] content) {
        if (connectionLost) return; // Allows for two players NES games to continue once a player has disconnected so that the
                                    // other player can improve its score and win (or lose if it can't reach the points the
                                    // rival had when he lost connection)

        try {
            dataOutputStream.writeByte((byte) action.ordinal());

            if (content != null) {
                for (byte b : content) {
                    dataOutputStream.writeByte(b);
                }
            }

            dataOutputStream.flush();
        }
        catch (IOException ioe) {
            System.out.println("Tengo esperanza de que el hilo del input ha cerrado el socket :D");
            twoPlayersPanel.handleConnectionError();
            System.out.println("Nah, en realidad estoy seguro ;P");
        }
    }
}