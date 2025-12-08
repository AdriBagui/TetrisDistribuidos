package client.userInterface.panels.tetris.twoPlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.io.SenderBoardOutputHandler;
import tetris.boards.ReceiverBoard;
import tetris.boards.io.ReceiverBoardInputHandler;
import tetris.boards.nes.ReceiverNESBoard;
import tetris.boards.nes.SenderNESBoardWithPhysics;

import java.io.IOException;

import static client.userInterface.panels.MainPanel.*;

public class OnlineTwoPlayersNESPanel extends OnlineTwoPlayersPanel {
    public OnlineTwoPlayersNESPanel(MainPanel mainPanel) {
        super(mainPanel);
    }

    @Override
    protected int checkWinner() {
        int player1Score = boards[0].getScore();
        int player2Score = boards[1].getScore();

        if (player1Score > player2Score) return 1;
        else if (player1Score < player2Score) return 2;
        else return 0;
    }

    @Override
    protected synchronized boolean checkGameOver() {
        return (!boards[0].isAlive() && (isRivalGameOver() || !isConnectionUp()));
    }

    @Override
    protected void initializeBoards() {
        try {
            keyInputHandler.enableNESControls();

            senderBoardOutputHandler = new SenderBoardOutputHandler(boardsSocket.getOutputStream(), this);
            boards[0] = new SenderNESBoardWithPhysics(BOARD1_X + 3*TETROMINO_HOLDER_WIDTH/4, BOARD1_Y, seed, senderBoardOutputHandler);

            boards[1] = new ReceiverNESBoard(BOARD2_X + TETROMINO_HOLDER_WIDTH/4, BOARD2_Y, seed);
            receiverBoardInputHandler = new ReceiverBoardInputHandler(boardsSocket.getInputStream(), (ReceiverBoard) boards[1], this);
            receiverBoardInputHandler.start();
        }
        catch (IOException ioe) { ioe.printStackTrace(); }
    }
}