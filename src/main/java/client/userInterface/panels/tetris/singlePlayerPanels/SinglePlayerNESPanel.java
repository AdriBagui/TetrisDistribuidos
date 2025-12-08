package client.userInterface.panels.tetris.singlePlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.nes.NESBoardWithPhysics;

import static client.userInterface.panels.MainPanel.*;

/**
 * A concrete panel implementation for the Single Player NES (Classic) Tetris mode.
 * <p>
 * This class configures the game for classic mechanics (classic rotation, no hold piece)
 * and centers the board within the panel view.
 * </p>
 */
public class SinglePlayerNESPanel extends SinglePlayerTetrisPanel{

    /**
     * Constructs the NES Tetris panel.
     *
     * @param mainPanel The parent container.
     */
    public SinglePlayerNESPanel(MainPanel mainPanel) {
        super(mainPanel);
    }

    /**
     * Initializes the single player board with NES-style physics and layout.
     * Calculates the horizontal position to center the board, considering only the board width and queue
     * (since NES Tetris typically lacks a hold piece container).
     */
    @Override
    protected void initializeBoards() {
        keyInputHandler.enableNESControls();
        int boardX = (PANEL_WIDTH - BOARD_WIDTH - TETROMINOES_QUEUE_WIDTH) >> 1;
        boards[0] = new NESBoardWithPhysics(boardX, BOARD1_Y, System.currentTimeMillis());
    }
}