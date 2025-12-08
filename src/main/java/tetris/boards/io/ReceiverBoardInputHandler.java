package tetris.boards.io;

import tetris.boards.ReceiverBoard;
import client.userInterface.panels.tetris.twoPlayerPanels.TwoPlayersPanel;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class ReceiverBoardInputHandler extends Thread {
    /*
     * Protocol Definition:
     * First byte is the Action.
     * UPDATE_FALLING_TETROMINO: + 4 bytes [X, Y, ROT, LOCK] -> For updatesReceiverBoard
     */
    protected final DataInputStream boardsInputReceiver;
    protected final ReceiverBoard updatesReceiverBoard;
    protected final TwoPlayersPanel twoPlayersPanel;

    /**
     * Creates a thread that listens for game updates from a remote source (player 2 or server).
     * @param boardsInputReceiver the input stream to read from.
     * @param updatesReceiverBoard the board to update based on received messages.
     */
    public ReceiverBoardInputHandler(InputStream boardsInputReceiver, ReceiverBoard updatesReceiverBoard, TwoPlayersPanel twoPlayersPanel) {
        this.boardsInputReceiver = new DataInputStream(boardsInputReceiver);
        this.updatesReceiverBoard = updatesReceiverBoard;
        this.twoPlayersPanel = twoPlayersPanel;
    }

    /**
     * Main loop that reads actions until the thread is interrupted.
     */
    @Override
    public void run() {
        BoardMessageType action;

        // When interrupted, the thread finishes its task and dies
        try {
            while (true) { // This finishes when the socket closes because readMessageType() throws an EOFException
                action = readMessageType();
                sendMessageToBoard(action);
            }
        }
        catch (EOFException eofe) {
            twoPlayersPanel.closeCommunications();
        }
        catch (IOException ioe) {
            twoPlayersPanel.handleConnectionError();
        }
    }

    /**
     * Reads a single byte representing the board message type and parses it to a board message type.
     * @return the board message type.
     * @throws IOException if reading fails.
     */
    protected BoardMessageType readMessageType() throws IOException {
        return BoardMessageType.values()[boardsInputReceiver.readByte()];
    }

    /**
     * Decodes the action and updates the board state.
     * Default implementation handles movement updates (Action 4).
     * Subclasses can override to handle garbage and hold actions.
     * @param action the message type code to interpret and send to board.
     * @throws IOException if reading the payload fails.
     */
    protected void sendMessageToBoard(BoardMessageType action) throws IOException {
        byte x, y, rotationIndex;
        boolean isLocked;

        if (action == BoardMessageType.UPDATE_FALLING_TETROMINO) {
            // UPDATE_FALLING_TETROMINO: followed by 4 bytes "X Y ROT LOCK"
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