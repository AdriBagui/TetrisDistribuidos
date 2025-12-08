package tetris.boards.io;

/**
 * Enumeration defining the protocol messages used for communication between boards.
 * <p>
 * This enum maps specific game events to byte codes. The protocol supports both
 * generic Tetris updates (movement, locking) and Modern Tetris features (garbage, hold).
 * </p>
 * <h3>Protocol Structure:</h3>
 * <ul>
 * <li><b>SEND_GARBAGE_ROWS:</b> Followed by 2 bytes {@code [LINES, EMPTY_COLUMN]}.</li>
 * <li><b>UPDATE_FALLING_TETROMINO:</b> Followed by 4 bytes {@code [X, Y, ROTATION, LOCK_STATUS]}.</li>
 * <li><b>UPDATE_TETROMINO_HOLDER:</b> No payload (signal only).</li>
 * <li><b>UPDATE_GARBAGE_ROWS:</b> Followed by 2 bytes {@code [LINES, EMPTY_COLUMN]}.</li>
 * </ul>
 */
public enum BoardMessageType {
    /**
     * Command to add garbage lines to the opponent's board.
     * <p>Payload: {@code [byte lines, byte emptyColumn]}</p>
     */
    SEND_GARBAGE_ROWS,

    /**
     * Update the position and state of the current falling piece.
     * <p>Payload: {@code [byte x, byte y, byte rotationIndex, byte isLocked]}</p>
     */
    UPDATE_FALLING_TETROMINO,

    /**
     * Notification that the player has swapped a piece into the Hold queue.
     * <p>Payload: None</p>
     */
    UPDATE_TETROMINO_HOLDER,

    /**
     * Update regarding the status of pending garbage lines (used for visual feedback).
     * <p>Payload: {@code [byte lines, byte emptyColumn]}</p>
     */
    UPDATE_GARBAGE_ROWS
}