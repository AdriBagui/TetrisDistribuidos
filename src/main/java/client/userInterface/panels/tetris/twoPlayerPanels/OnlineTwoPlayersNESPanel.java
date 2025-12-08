package client.userInterface.panels.tetris.twoPlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.io.SenderBoardOutputHandler;
import tetris.boards.ReceiverBoard;
import tetris.boards.io.ReceiverBoardInputHandler;
import tetris.boards.nes.ReceiverNESBoard;
import tetris.boards.nes.SenderNESBoardWithPhysics;

import java.io.IOException;

import static client.userInterface.panels.MainPanel.*;

/**
 * Manages an online 1v1 NES (Classic) Tetris game.
 * <p>
 * This class initializes the boards and the specific I/O handlers required for NES Tetris.
 * Unlike Modern Tetris, this mode does not send garbage, so the handlers are simpler.
 * </p>
 */
public class OnlineTwoPlayersNESPanel extends OnlineTwoPlayersPanel {

    /**
     * Constructs the OnlineTwoPlayersNESPanel.
     *
     * @param mainPanel The parent container.
     */
    public OnlineTwoPlayersNESPanel(MainPanel mainPanel) {
        super(mainPanel);
    }

    /**
     * Determines the winner based on the score.
     *
     * @return 1 if Player 1 (Local) has a higher score, 2 if Player 2 (Remote) has a higher score.
     */
    @Override
    protected int checkWinner() {
        int player1Score = boards[0].getScore();
        int player2Score = boards[1].getScore();

        if (player1Score > player2Score) return 1;
        else if (player1Score < player2Score) return 2;
        else return 0;
    }

    /**
     * Checks global game over condition.
     * The game is only over when the local player dies AND (the rival is dead OR the connection is dropped).
     *
     * @return {@code true} if the game session is effectively over.
     */
    @Override
    protected synchronized boolean checkGameOver() {
        return (!boards[0].isAlive() && (!boards[1].isAlive() || !isConnectionUp()));
    }

    /**
     * Initializes the local sender board and the remote receiver board.
     * Attaches handlers to the socket streams to synchronize piece movement and score.
     */
    @Override
    protected void initializeBoards() {
        try {
            keyInputHandler.enableNESControls();

            // Local board: Sends data
            senderBoardOutputHandler = new SenderBoardOutputHandler(boardsSocket.getOutputStream(), this);
            boards[0] = new SenderNESBoardWithPhysics(BOARD1_X + 3*TETROMINO_HOLDER_WIDTH/4, BOARD1_Y, seed, senderBoardOutputHandler);

            // Remote board: Receives data
            boards[1] = new ReceiverNESBoard(BOARD2_X + TETROMINO_HOLDER_WIDTH/4, BOARD2_Y, seed);
            receiverBoardInputHandler = new ReceiverBoardInputHandler(boardsSocket.getInputStream(), (ReceiverBoard) boards[1], this);
            receiverBoardInputHandler.start();
        }
        catch (IOException ioe) { handleConnectionError(); }
    }
}