package client.userInterface.panels.tetris.singlePlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.modernTetris.ModernTetrisBoardWithPhysics;

import static client.userInterface.panels.MainPanel.*;

/**
 * A concrete panel implementation for the Single Player Modern Tetris mode.
 * <p>
 * This class configures the game for modern mechanics (e.g., Hold piece, Hard Drop, Super Rotation System)
 * and centers the board within the panel view.
 * </p>
 */
public class SinglePlayerModernTetrisPanel extends SinglePlayerTetrisPanel {

    /**
     * Constructs the Modern Tetris panel.
     *
     * @param mainPanel The parent container.
     */
    public SinglePlayerModernTetrisPanel(MainPanel mainPanel) {
        super(mainPanel);
    }

    /**
     * Initializes the single player board with modern Tetris physics and layout.
     * Calculates the horizontal position to center the board, considering the queue and hold container widths.
     */
    @Override
    protected void initializeBoards() {
        keyInputHandler.enableModernTetrisControls();
        int boardX = (PANEL_WIDTH - BOARD_WIDTH - TETROMINOES_QUEUE_WIDTH - TETROMINO_HOLDER_WIDTH) >> 1;
        boards[0] = new ModernTetrisBoardWithPhysics(boardX, BOARD1_Y, System.currentTimeMillis());
    }
}