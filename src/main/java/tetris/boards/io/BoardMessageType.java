package tetris.boards.io;

public enum BoardMessageType {
    /*
     * Protocol Definition:
     * Messages: TetrioBoardWithPhysics messages
     * - SEND_GARBAGE_ROWS: followed by 2 bytes [LINES, EMPTY_COLUMN]
     * Messages: Updates to representations
     * - UPDATE_FALLING_TETROMINO: followed by 4 bytes [X, Y, ROT, LOCK]
     * - UPDATE_TETROMINO_HOLDER: no payload
     * - UPDATE_GARBAGE_ROWS: followed by 2 bytes [LINES, EMPTY_COLUMN]
     */

    SEND_GARBAGE_ROWS,
    UPDATE_FALLING_TETROMINO,
    UPDATE_TETROMINO_HOLDER,
    UPDATE_GARBAGE_ROWS
}
