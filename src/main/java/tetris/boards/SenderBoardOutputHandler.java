package tetris.boards;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SenderBoardOutputHandler {
    /*
     * Protocol Definition:
     * Messages 0-3: TetrioBoardWithPhysics messages
     * - 0 (ADD_GARBAGE): followed by 2 bytes [LINES, EMPTY_COLUMN]
     *
     * Messages 4-6: Updates to representations
     * - 4 (MOVE): followed by 4 bytes [X, Y, ROT, LOCK]
     * - 5 (HOLD): no payload
     * - 6 (UPDATE_GARBAGE): followed by 2 bytes [LINES, EMPTY_COLUMN]
     */
    private static final byte ADD_GARBAGE = 0;
    private static final byte MOVE = 4;
    private static final byte HOLD = 5;
    private static final byte UPDATE_GARBAGE = 6;

    protected DataOutputStream dataOutputStream;

    /**
     * Handles sending encoded game events over an output stream (e.g., network or pipe).
     * @param outputStream the destination stream.
     */
    public SenderBoardOutputHandler(OutputStream outputStream) {
        this.dataOutputStream = new DataOutputStream(outputStream);
    }

    /**
     * Sends a command to add garbage lines to the opponent.
     * Protocol: [Action 0] [Lines] [Empty Column]
     * @param numberOfGarbageRows number of lines to add.
     * @param emptyGarbageColumn the column index that is empty in the garbage lines.
     */
    public void sendAddGarbageMessage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        sendMessage(ADD_GARBAGE, new byte[] {
                numberOfGarbageRows,
                emptyGarbageColumn
        });
    }

    /**
     * Sends the current state of the falling tetromino.
     * Protocol: [Action 4] [X] [Y] [Rotation] [LockStatus]
     * @param x x coordinate of the piece.
     * @param y y coordinate of the piece.
     * @param rotationIndex rotation state.
     * @param isFallingTetrominoLocked true if the piece has locked.
     */
    public void sendUpdateMessage(byte x, byte y, byte rotationIndex, boolean isFallingTetrominoLocked) {
        sendMessage((byte) 4, new byte[] {
                x,
                y,
                rotationIndex,
                (byte) (isFallingTetrominoLocked ? 1 : 0)
        });
    }

    /**
     * Sends a notification that the player used the hold function.
     * Protocol: [Action 5]
     */
    public void sendHoldMessage() { sendMessage((byte) 5, null); }

    /**
     * Sends an update about garbage lines waiting in the buffer.
     * Protocol: [Action 6] [Lines] [Empty Column]
     * @param numberOfGarbageRows number of pending garbage rows.
     * @param emptyGarbageColumn the empty column index.
     */
    public void sendUpdateGarbageMessage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        sendMessage((byte) 6, new byte[] {
                numberOfGarbageRows,
                emptyGarbageColumn
        });
    }

    // ---------------------------------------------------------------------------------
    // Auxiliary methods

    /**
     * Thread-safe method to write bytes to the stream.
     * @param action the protocol action byte.
     * @param content the payload bytes (can be null).
     */
    private synchronized void sendMessage(byte action, byte[] content) {
        try {
            dataOutputStream.writeByte(action);

            if (content != null) {
                for (byte b : content) {
                    dataOutputStream.writeByte(b);
                }
            }

            dataOutputStream.flush();
        }
        catch (IOException ioe) {
            System.out.println("Tengo esperanza de que el otro hilo ha cerrado el socket :D"); // This should never happen, if it does your computer is broken sry
        }
    }
}