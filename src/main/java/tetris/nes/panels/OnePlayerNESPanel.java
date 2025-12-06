package tetris.nes.panels;

import main.MainPanel;
import tetris.general.panels.OnePlayerTetrisPanel;
import tetris.nes.boards.NESBoardWithPhysics;
import static tetris.Config.*;

public class OnePlayerNESPanel extends OnePlayerTetrisPanel {
    public OnePlayerNESPanel(MainPanel mainPanel) {
        super(mainPanel);
    }

    @Override
    protected void initializeGame() {
        boards[0] = new NESBoardWithPhysics((PANEL_WIDTH - BOARD_WIDTH - TETROMINOES_QUEUE_WIDTH) >> 1, BOARD1_Y, System.currentTimeMillis());
    }
}
