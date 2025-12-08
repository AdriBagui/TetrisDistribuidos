package tetris.boards.io;

import tetris.boards.tetrio.ReceiverTetrioBoard;
import tetris.boards.tetrio.SenderTetrioBoardWithPhysics;
import client.userInterface.panels.tetris.twoPlayerPanels.TwoPlayersPanel;

import java.io.IOException;
import java.io.InputStream;

public class ReceiverTetrioBoardInputHandler extends ReceiverBoardInputHandler {
    /*
     * Protocol Definition:
     * First byte is the Action.
     * SEND_GARBAGE_ROWS: + 2 bytes [LINES, EMPTY_COLUMN] -> For garbageReceiverBoard
     * UPDATE_TETROMINO_HOLDER: 0 bytes -> For updatesReceiverBoard
     * UPDATE_GARBAGE_ROWS: + 2 bytes [LINES, EMPTY_COLUMN] -> For updatesReceiverBoard
     */
    private final SenderTetrioBoardWithPhysics garbageReceiverBoard;
    private final ReceiverTetrioBoard updatesReceiverTetrioBoard;

    public ReceiverTetrioBoardInputHandler(InputStream boardsInputReceiver, SenderTetrioBoardWithPhysics garbageReceiverBoard, ReceiverTetrioBoard updatesReceiverTetrioBoard, TwoPlayersPanel twoPlayersPanel) {
        super(boardsInputReceiver, updatesReceiverTetrioBoard, twoPlayersPanel);
        this.updatesReceiverTetrioBoard = updatesReceiverTetrioBoard;
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
                if (updatesReceiverTetrioBoard != null) updatesReceiverTetrioBoard.hold();
                break;
            case UPDATE_GARBAGE_ROWS:
                // UPDATE_GARBAGE_ROWS: + 2 bytes [LINES, EMPTY_COLUMN] -> For updatesReceiverBoard (if there is one)
                numberOfGarbageRowsToUpdate = boardsInputReceiver.readByte();
                emptyGarbageColumnToUpdate = boardsInputReceiver.readByte();
                if (updatesReceiverTetrioBoard != null)
                    updatesReceiverTetrioBoard.addGarbage(numberOfGarbageRowsToUpdate, emptyGarbageColumnToUpdate);
                break;
        }
    }
}
