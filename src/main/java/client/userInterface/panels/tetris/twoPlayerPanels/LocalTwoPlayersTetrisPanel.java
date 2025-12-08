package client.userInterface.panels.tetris.twoPlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.BoardWithPhysics;

/**
 * An abstract base class for local multiplayer games (same machine).
 * <p>
 * This class manages the update loop for two distinct boards running on the same client.
 * It ensures that inputs for both Player 1 and Player 2 are correctly routed to their
 * respective boards via the input handler.
 * </p>
 */
public abstract class LocalTwoPlayersTetrisPanel extends TwoPlayersTetrisPanel {

    /**
     * Constructs a LocalTwoPlayersTetrisPanel.
     *
     * @param mainPanel The parent container.
     */
    public LocalTwoPlayersTetrisPanel(MainPanel mainPanel) {
        super(mainPanel);
    }

    /**
     * Updates the state of both boards if they are still active.
     */
    @Override
    protected void updateBoards() {
        if (boards[0].isAlive()) boards[0].update();
        if (boards[1].isAlive()) boards[1].update();
    }

    /**
     * Resets the game and maps input controls to both local boards.
     */
    @Override
    protected void resetGame() {
        super.resetGame();
        keyInputHandler.setPlayer1Board((BoardWithPhysics) boards[0]);
        keyInputHandler.setPlayer2Board((BoardWithPhysics) boards[1]);
    }
}