package client.userInterface.panels.tetris.twoPlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.io.SenderBoardOutputHandler;
import tetris.boards.modernTetris.ReceiverModernTetrisBoard;
import tetris.boards.io.ReceiverModernTetrisBoardInputHandler;
import tetris.boards.modernTetris.SenderModernTetrisBoardWithPhysics;

import java.io.IOException;

import static client.userInterface.panels.MainPanel.*;

/**
 * Manages an online 1v1 Modern Tetris game.
 * <p>
 * This class initializes the boards and the specific I/O handlers required for Modern Tetris
 * (which includes garbage sending and hold piece synchronization).
 * </p>
 */
public class OnlineTwoPlayersModernTetrisPanel extends OnlineTwoPlayersPanel {

    /**
     * Constructs the OnlineTwoPlayersModernTetrisPanel.
     *
     * @param mainPanel The parent container.
     */
    public OnlineTwoPlayersModernTetrisPanel(MainPanel mainPanel) {
        super(mainPanel);
    }

    /**
     * Determines the winner based on who is still alive.
     *
     * @return 1 if Player 1 (Local) is alive, 2 if Player 2 (Remote) is alive, 0 otherwise.
     */
    @Override
    protected int checkWinner() {
        if (!hasLocalLost() && hasOpponentLost()) return 1;
        else if (hasLocalLost() && !hasOpponentLost()) return 2;
        else return 0;
    }

    /**
     * Checks global game over condition.
     * The game ends when the local player dies OR the rival dies.
     *
     * @return {@code true} if either player has topped out.
     */
    @Override
    protected synchronized boolean checkGameOver() { return (hasLocalLost() || hasOpponentLost() || !isConnectionUp()); }

    /**
     * Initializes the local sender board and the remote receiver board.
     * Attaches the {@link SenderBoardOutputHandler} to the socket output stream and the
     * {@link ReceiverModernTetrisBoardInputHandler} to the socket input stream.
     */
    @Override
    protected void initializeBoards() {
        try {
            keyInputHandler.enableModernTetrisControls();

            // Local board: Sends data to socket
            senderBoardOutputHandler = new SenderBoardOutputHandler(boardsSocket.getOutputStream(), this);
            boards[0] = new SenderModernTetrisBoardWithPhysics(BOARD1_X, BOARD1_Y, seed, senderBoardOutputHandler);

            // Remote board: Receives data from socket
            boards[1] = new ReceiverModernTetrisBoard(BOARD2_X, BOARD2_Y, seed);
            receiverBoardInputHandler = new ReceiverModernTetrisBoardInputHandler(boardsSocket.getInputStream(), (SenderModernTetrisBoardWithPhysics) boards[0], (ReceiverModernTetrisBoard) boards[1], this);
            receiverBoardInputHandler.start();
        }
        catch (IOException ioe) { handleConnectionError(); }
    }
}