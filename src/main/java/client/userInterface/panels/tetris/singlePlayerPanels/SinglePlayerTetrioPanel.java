package client.userInterface.panels.tetris.singlePlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.tetrio.TetrioBoardWithPhysics;

import static client.userInterface.panels.MainPanel.*;

public class SinglePlayerTetrioPanel extends SinglePlayerPanel {
    public SinglePlayerTetrioPanel(MainPanel mainPanel) {
        super(mainPanel);
    }

    @Override
    protected void initializeBoards() {
        keyInputHandler.enableTetrioControls();
        int boardX = (PANEL_WIDTH - BOARD_WIDTH - TETROMINOES_QUEUE_WIDTH - TETROMINO_HOLDER_WIDTH) >> 1;
        boards[0] = new TetrioBoardWithPhysics(boardX, BOARD1_Y, System.currentTimeMillis());
    }
}
