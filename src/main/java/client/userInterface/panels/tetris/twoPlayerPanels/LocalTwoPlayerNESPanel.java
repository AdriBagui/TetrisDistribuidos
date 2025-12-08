package client.userInterface.panels.tetris.twoPlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.nes.NESBoardWithPhysics;

import static client.userInterface.panels.MainPanel.*;

public class LocalTwoPlayerNESPanel extends LocalTwoPlayersPanel {
    public LocalTwoPlayerNESPanel(MainPanel mainPanel) {
        super(mainPanel);
    }

    @Override
    public void closeCommunications() {
        // Does nothing because there are no communications
    }

    /**
     * Called by ReceiverBoardInputHandler and SenderBoardsOutputHandler when IOException occurs.
     */
    @Override
    public synchronized void handleConnectionError() { // synchronized so that it doesn't interfere with the normal socket closing
        // Does nothing because there are no connections
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
        keyInputHandler.enablePlayer2Controls();
        keyInputHandler.enableNESControls();
        long seed = System.currentTimeMillis();
        boards[0] = new NESBoardWithPhysics(BOARD1_X + 3*TETROMINO_HOLDER_WIDTH/3, BOARD1_Y, seed);
        boards[1] = new NESBoardWithPhysics(BOARD2_X + TETROMINO_HOLDER_WIDTH/4, BOARD2_Y, seed);
    }
}
