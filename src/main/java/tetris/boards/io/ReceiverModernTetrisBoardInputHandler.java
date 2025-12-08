package tetris.boards.io;

import tetris.boards.modernTetris.ReceiverModernTetrisBoard;
import tetris.boards.modernTetris.SenderModernTetrisBoardWithPhysics;
import client.userInterface.panels.tetris.twoPlayerPanels.TwoPlayersTetrisPanel;

import java.io.IOException;
import java.io.InputStream;

public class ReceiverModernTetrisBoardInputHandler extends ReceiverBoardInputHandler {
    /*
     * Protocol Definition:
     * First byte is the Action.
     * SEND_GARBAGE_ROWS: + 2 bytes [LINES, EMPTY_COLUMN] -> For garbageReceiverBoard
     * UPDATE_TETROMINO_HOLDER: 0 bytes -> For updatesReceiverBoard
     * UPDATE_GARBAGE_ROWS: + 2 bytes [LINES, EMPTY_COLUMN] -> For updatesReceiverBoard
     */
    private final SenderModernTetrisBoardWithPhysics garbageReceiverBoard;
    private final ReceiverModernTetrisBoard updatesReceiverModernTetrisBoard;

    public ReceiverModernTetrisBoardInputHandler(InputStream boardsInputReceiver, SenderModernTetrisBoardWithPhysics garbageReceiverBoard, ReceiverModernTetrisBoard updatesReceiverModernTetrisBoard, TwoPlayersTetrisPanel twoPlayersTetrisPanel) {
        super(boardsInputReceiver, updatesReceiverModernTetrisBoard, twoPlayersTetrisPanel);
        this.updatesReceiverModernTetrisBoard = updatesReceiverModernTetrisBoard;
        this.garbageReceiverBoard = garbageReceiverBoard;
    }

    @Override
    protected void sendMessageToBoard(BoardMessageType action) throws IOException {
        byte numberOfGarbageRowsToAdd, emptyGarbageColumnToAdd;
        byte numberOfGarbageRowsToUpdate, emptyGarbageColumnToUpdate;

        super.sendMessageToBoard(action);

        switch (action) {
            case SEND_GARBAGE_ROWS:
                // SEND_GARBAGE_ROWS: + 2 bytes [LINES, EMPTY_COLUMN] -> For garbageReceiverBoard
                numberOfGarbageRowsToAdd = boardsInputReceiver.readByte();
                emptyGarbageColumnToAdd = boardsInputReceiver.readByte();
                garbageReceiverBoard.addGarbage(numberOfGarbageRowsToAdd, emptyGarbageColumnToAdd);
                break;
            case UPDATE_TETROMINO_HOLDER:
                // UPDATE_TETROMINO_HOLDER: 0 bytes -> For updatesReceiverBoard (if there is one)
                if (updatesReceiverModernTetrisBoard != null) updatesReceiverModernTetrisBoard.hold();
                break;
            case UPDATE_GARBAGE_ROWS:
                // UPDATE_GARBAGE_ROWS: + 2 bytes [LINES, EMPTY_COLUMN] -> For updatesReceiverBoard (if there is one)
                numberOfGarbageRowsToUpdate = boardsInputReceiver.readByte();
                emptyGarbageColumnToUpdate = boardsInputReceiver.readByte();
                if (updatesReceiverModernTetrisBoard != null)
                    updatesReceiverModernTetrisBoard.addGarbage(numberOfGarbageRowsToUpdate, emptyGarbageColumnToUpdate);
                break;
        }
    }
}
