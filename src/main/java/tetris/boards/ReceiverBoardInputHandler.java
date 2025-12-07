package tetris.boards;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReceiverBoardInputHandler extends Thread {
    /*
     * Protocol Definition:
     * First byte is the Action.
     * 0 (ADD_GARBAGE): + 2 bytes [LINES, EMPTY_COLUMN] -> For garbageReceiverBoard
     * 4 (MOVE): + 4 bytes [X, Y, ROT, LOCK] -> For updatesReceiverBoard
     * 5 (HOLD): 0 bytes -> For updatesReceiverBoard
     * 6 (UPDATE_GARBAGE): + 2 bytes [LINES, EMPTY_COLUMN] -> For updatesReceiverBoard
     */
    protected final DataInputStream boardsInputReceiver;
    protected final ReceiverBoard updatesReceiverBoard;

    /**
     * Creates a thread that listens for game updates from a remote source (player 2 or server).
     * @param boardsInputReceiver the input stream to read from.
     * @param updatesReceiverBoard the board to update based on received messages.
     */
    public ReceiverBoardInputHandler(InputStream boardsInputReceiver, ReceiverBoard updatesReceiverBoard) {
        this.boardsInputReceiver = new DataInputStream(boardsInputReceiver);
        this.updatesReceiverBoard = updatesReceiverBoard;
    }

    /**
     * Main loop that reads actions until the thread is interrupted.
     */
    @Override
    public void run() {
        byte action;

        // When interrupted, the thread finishes its task and dies
        while (!isInterrupted()) {
            try {
                action = readAction();
                executeAction(action);
            }
            catch (IOException ioe){
                // Entered if the output socket on the other side is closed
                // Or if this input socket is closed
                ioe.printStackTrace();
            }
        }
    }

    /**
     * Reads a single byte representing the action code.
     * @return the action byte.
     * @throws IOException if reading fails.
     */
    protected byte readAction() throws IOException {
        return boardsInputReceiver.readByte();
    }

    /**
     * Decodes the action and updates the board state.
     * Default implementation handles movement updates (Action 4).
     * Subclasses can override to handle garbage and hold actions.
     * @param action the action code to execute.
     * @throws IOException if reading the payload fails.
     */
    protected void executeAction(byte action) throws IOException {
        byte x, y, rotationIndex;
        boolean isLocked;

        if (action == 4) {
            // Action 4 (MOVE): followed by 4 bytes "X Y ROT LOCK"
            x = boardsInputReceiver.readByte();
            y = boardsInputReceiver.readByte();
            rotationIndex = boardsInputReceiver.readByte();
            isLocked = (boardsInputReceiver.readByte() == 1);

            if (updatesReceiverBoard != null)  {
                updatesReceiverBoard.setFallingTetrominoXYRotationIndex(x,y,rotationIndex);
                if (isLocked) updatesReceiverBoard.lockFallingTetromino();
                updatesReceiverBoard.update();
            }
        }
    }
}