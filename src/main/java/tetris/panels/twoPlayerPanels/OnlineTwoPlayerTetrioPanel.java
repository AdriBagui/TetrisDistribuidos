package tetris.panels.twoPlayerPanels;

import main.MainPanel;
import tetris.boards.SenderBoardOutputHandler;
import tetris.boards.tetrio.ReceiverTetrioBoard;
import tetris.boards.tetrio.ReceiverTetrioBoardInputHandler;
import tetris.boards.tetrio.SenderTetrioBoardWithPhysics;

import java.io.IOException;

import static main.MainPanel.*;

public class OnlineTwoPlayerTetrioPanel extends OnlineTwoPlayersPanel {
    public OnlineTwoPlayerTetrioPanel(MainPanel mainPanel) {
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
    protected boolean checkGameOver() {
        return (!boards[0].isAlive() || !boards[1].isAlive());
    }

    @Override
    protected void initializeBoards() {
        try {
            keyInputHandler.enableTetrioControls();
            boards[0] = new SenderTetrioBoardWithPhysics(BOARD1_X, BOARD1_Y, seed, new SenderBoardOutputHandler(boardsSocket.getOutputStream()));
            boards[1] = new ReceiverTetrioBoard(BOARD2_X, BOARD2_Y, seed);
            receiverBoardInputHandler = new ReceiverTetrioBoardInputHandler(boardsSocket.getInputStream(), (SenderTetrioBoardWithPhysics) boards[0], (ReceiverTetrioBoard) boards[1], this);
            receiverBoardInputHandler.start();
        }
        catch (IOException ioe) { ioe.printStackTrace(); }
    }
}