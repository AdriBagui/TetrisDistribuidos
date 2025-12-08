package client.userInterface.panels.tetris.singlePlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.nes.NESBoardWithPhysics;

import static client.userInterface.panels.MainPanel.*;

public class SinglePlayerNESPanel extends SinglePlayerPanel {
    public SinglePlayerNESPanel(MainPanel mainPanel) {
        super(mainPanel);
    }

    @Override
    protected void initializeBoards() {
        keyInputHandler.enableNESControls();
        int boardX = (PANEL_WIDTH - BOARD_WIDTH - TETROMINOES_QUEUE_WIDTH) >> 1;
        boards[0] = new NESBoardWithPhysics(boardX, BOARD1_Y, System.currentTimeMillis());
    }
}
