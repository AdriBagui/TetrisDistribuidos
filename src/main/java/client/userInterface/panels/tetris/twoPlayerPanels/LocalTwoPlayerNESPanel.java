package client.userInterface.panels.tetris.twoPlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.nes.NESBoardWithPhysics;

import static client.userInterface.panels.MainPanel.*;

/**
 * Manages a local 1v1 NES (Classic) Tetris game.
 * <p>
 * This mode is purely score-based. There is no garbage sending or interaction between boards.
 * The winner is determined by who has the higher score when both players top out.
 * </p>
 */
public class LocalTwoPlayerNESPanel extends LocalTwoPlayersTetrisPanel {

    /**
     * Constructs a LocalTwoPlayerNESPanel.
     *
     * @param mainPanel The parent container.
     */
    public LocalTwoPlayerNESPanel(MainPanel mainPanel) {
        super(mainPanel);
    }

    /**
     * No-op implementation as NES mode has no inter-board communication.
     */
    @Override
    public void closeCommunications() {
        // Does nothing because there are no communications
    }

    /**
     * No-op implementation as NES mode has no connections to manage.
     */
    @Override
    public synchronized void handleConnectionError() { // synchronized so that it doesn't interfere with the normal socket closing
        // Does nothing because there are no connections
    }

    /**
     * Determines the winner based on the score.
     *
     * @return "Player 1 has won!" if Player 1 (Local) has a higher score, "Player 2 has won!" if Player 2 (Remote) has a higher score. "It's a tie!" otherwise.
     */
    @Override
    protected String checkWinner() {
        int player1Score = boards[0].getScore();
        int player2Score = boards[1].getScore();

        if (player1Score > player2Score) return "Player 1 has won!";
        else if (player1Score < player2Score) return "Player 2 has won!";
        else return "It's a tie!";
    }

    /**
     * The game ends only when BOTH players have topped out.
     */
    @Override
    protected boolean checkGameOver() {
        return (!boards[0].isAlive() && !boards[1].isAlive());
    }

    /**
     * Initializes two independent {@link NESBoardWithPhysics} instances with the same random seed.
     */
    @Override
    protected void initializeBoards() {
        keyInputHandler.enablePlayer2Controls();
        keyInputHandler.enableNESControls();
        long seed = System.currentTimeMillis();
        boards[0] = new NESBoardWithPhysics(BOARD1_X + 3*TETROMINO_HOLDER_WIDTH/3, BOARD1_Y, seed);
        boards[1] = new NESBoardWithPhysics(BOARD2_X + TETROMINO_HOLDER_WIDTH/4, BOARD2_Y, seed);
    }
}