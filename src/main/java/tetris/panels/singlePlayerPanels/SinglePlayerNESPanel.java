package tetris.panels.singlePlayerPanels;

import main.MainPanel;
import tetris.boards.BoardWithPhysics;
import tetris.boards.nes.NESBoardWithPhysics;
import tetris.keyMaps.KeyMapFactory;
import tetris.keyMaps.SinglePlayerKeyMap;

import static tetris.Config.*;

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
