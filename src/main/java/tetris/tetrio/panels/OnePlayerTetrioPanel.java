package tetris.tetrio.panels;

import main.MainPanel;
import tetris.general.panels.OnePlayerTetrisPanel;
import tetris.nes.boards.NESBoardWithPhysics;
import tetris.tetrio.boards.TetrioBoardWithPhysics;

import static tetris.Config.*;
import static tetris.Config.BOARD1_Y;

public class OnePlayerTetrioPanel extends OnePlayerTetrisPanel {
    public OnePlayerTetrioPanel(MainPanel mainPanel) {
        super(mainPanel);
    }

    @Override
    protected void initializeGame() {
        boards[0] = new TetrioBoardWithPhysics((PANEL_WIDTH - BOARD_WIDTH - TETROMINOES_QUEUE_WIDTH - TETROMINO_HOLDER_WIDTH) >> 1, BOARD1_Y, System.currentTimeMillis(), null);
    }
}
