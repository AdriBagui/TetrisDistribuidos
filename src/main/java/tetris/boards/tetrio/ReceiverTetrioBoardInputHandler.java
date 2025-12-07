package tetris.boards.tetrio;

import tetris.boards.ReceiverBoardInputHandler;

import java.io.IOException;
import java.io.InputStream;

public class ReceiverTetrioBoardInputHandler extends ReceiverBoardInputHandler {
    private final SenderTetrioBoardWithPhysics garbageReceiverBoard;
    private final ReceiverTetrioBoard updatesReceiverTetrioBoard;

    public ReceiverTetrioBoardInputHandler(InputStream boardsInputReceiver, SenderTetrioBoardWithPhysics garbageReceiverBoard, ReceiverTetrioBoard updatesReceiverTetrioBoard) {
        super(boardsInputReceiver, updatesReceiverTetrioBoard);
        this.updatesReceiverTetrioBoard = updatesReceiverTetrioBoard;
        this.garbageReceiverBoard = garbageReceiverBoard;
    }

    @Override
    protected void executeAction(byte action) throws IOException {
        byte numberOfGarbageRowsToAdd, emptyGarbageColumnToAdd;
        byte numberOfGarbageRowsToUpdate, emptyGarbageColumnToUpdate;

        super.executeAction(action);

        switch (action) {
            case 0:
                // Acción 0 (ADD_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
                numberOfGarbageRowsToAdd = boardsInputReceiver.readByte();
                emptyGarbageColumnToAdd = boardsInputReceiver.readByte();
                garbageReceiverBoard.addGarbage(numberOfGarbageRowsToAdd, emptyGarbageColumnToAdd);
                break;
            // case 4: Acción 4 (MOVE) cubierto por super.executeAction();
            case 5:
                // Acción 5 (HOLD): no le sigue nada
                if (updatesReceiverTetrioBoard != null) updatesReceiverTetrioBoard.hold();
                break;
            case 6:
                // Acción 6 (UPDATE_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
                numberOfGarbageRowsToUpdate = boardsInputReceiver.readByte();
                emptyGarbageColumnToUpdate = boardsInputReceiver.readByte();
                if (updatesReceiverTetrioBoard != null)
                    updatesReceiverTetrioBoard.addGarbage(numberOfGarbageRowsToUpdate, emptyGarbageColumnToUpdate);
                break;
        }
    }
}
