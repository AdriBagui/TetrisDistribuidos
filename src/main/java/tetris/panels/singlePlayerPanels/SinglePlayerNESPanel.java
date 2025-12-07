package tetris.panels.singlePlayerPanels;

import main.MainPanel;
import tetris.boards.nes.NESBoardWithPhysics;
import tetris.keyMaps.KeyMapFactory;

import static main.MainPanel.*;

public class SinglePlayerNESPanel extends SinglePlayerPanel {
    public SinglePlayerNESPanel(MainPanel mainPanel) {
        super(mainPanel, KeyMapFactory.NES_MODE);
    }

    @Override
    protected void initializeBoards() {
        int boardX = (PANEL_WIDTH - BOARD_WIDTH - TETROMINOES_QUEUE_WIDTH) >> 1;
        boards[0] = new NESBoardWithPhysics(boardX, BOARD1_Y, System.currentTimeMillis());
    }
}
