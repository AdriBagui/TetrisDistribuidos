package tetris.panels.twoPlayerPanels;

import main.MainPanel;
import tetris.boards.SenderBoardOutputHandler;
import tetris.boards.ReceiverBoard;
import tetris.boards.ReceiverBoardInputHandler;
import tetris.boards.nes.ReceiverNESBoard;
import tetris.boards.nes.SenderNESBoardWithPhysicsSender;

import java.io.IOException;

import static main.MainPanel.*;

public class OnlineTwoPlayerNESPanel extends OnlineTwoPlayersPanel {
    public OnlineTwoPlayerNESPanel(MainPanel mainPanel) {
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
    protected boolean checkGameOver() {
        return (!boards[0].isAlive() && !boards[1].isAlive());
    }

    @Override
    protected void initializeBoards() {
        try {
            keyInputHandler.enableNESControls();
            boards[0] = new SenderNESBoardWithPhysicsSender(BOARD1_X, BOARD1_Y, seed, new SenderBoardOutputHandler(boardsSocket.getOutputStream()));
            boards[1] = new ReceiverNESBoard(BOARD2_X, BOARD2_Y, seed);
            receiverBoardInputHandler = new ReceiverBoardInputHandler(boardsSocket.getInputStream(), (ReceiverBoard) boards[1], this);
            receiverBoardInputHandler.start();
        }
        catch (IOException ioe) { ioe.printStackTrace(); }
    }
}