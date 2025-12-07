package tetris.panels.singlePlayerPanels;

import main.MainPanel;
import tetris.boards.tetrio.TetrioBoardWithPhysics;
import tetris.keyMaps.KeyMapFactory;

import static main.MainPanel.*;

public class SinglePlayerTetrioPanel extends SinglePlayerPanel {
    public SinglePlayerTetrioPanel(MainPanel mainPanel) {
        super(mainPanel, KeyMapFactory.TETRIO_MODE);
    }

    @Override
    protected void initializeBoards() {
        int boardX = (PANEL_WIDTH - BOARD_WIDTH - TETROMINOES_QUEUE_WIDTH - TETROMINO_HOLDER_WIDTH) >> 1;
        boards[0] = new TetrioBoardWithPhysics(boardX, BOARD1_Y, System.currentTimeMillis());
    }
}
