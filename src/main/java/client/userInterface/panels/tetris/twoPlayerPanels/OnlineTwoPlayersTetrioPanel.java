package client.userInterface.panels.tetris.twoPlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.io.SenderBoardOutputHandler;
import tetris.boards.tetrio.ReceiverTetrioBoard;
import tetris.boards.io.ReceiverTetrioBoardInputHandler;
import tetris.boards.tetrio.SenderTetrioBoardWithPhysics;

import java.io.IOException;

import static client.userInterface.panels.MainPanel.*;

public class OnlineTwoPlayersTetrioPanel extends OnlineTwoPlayersPanel {
    public OnlineTwoPlayersTetrioPanel(MainPanel mainPanel) {
        super(mainPanel);
    }

    @Override
    protected int checkWinner() {
        boolean player1Alive = boards[0].isAlive();
        boolean player2Alive = boards[1].isAlive();

        if (player1Alive && !player2Alive) return 1;
        else if (!player1Alive && player2Alive) return 2;
        else return 0;
    }

    @Override
    protected synchronized boolean checkGameOver() {
        return (!boards[0].isAlive() || isRivalGameOver());
    }

    @Override
    protected void initializeBoards() {
        try {
            keyInputHandler.enableTetrioControls();

            senderBoardOutputHandler = new SenderBoardOutputHandler(boardsSocket.getOutputStream(), this);
            boards[0] = new SenderTetrioBoardWithPhysics(BOARD1_X, BOARD1_Y, seed, senderBoardOutputHandler);

            boards[1] = new ReceiverTetrioBoard(BOARD2_X, BOARD2_Y, seed);
            receiverBoardInputHandler = new ReceiverTetrioBoardInputHandler(boardsSocket.getInputStream(), (SenderTetrioBoardWithPhysics) boards[0], (ReceiverTetrioBoard) boards[1], this);
            receiverBoardInputHandler.start();
        }
        catch (IOException ioe) { ioe.printStackTrace(); }
    }
}