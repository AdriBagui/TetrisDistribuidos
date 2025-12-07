package tetris.panels.twoPlayerPanels;

import main.MainPanel;
import tetris.boards.nes.NESBoardWithPhysics;
import tetris.keyMaps.KeyMapFactory;

import static tetris.Config.*;

public class LocalTwoPlayerNESPanel extends LocalTwoPlayersPanel {
    public LocalTwoPlayerNESPanel(MainPanel mainPanel) {
        super(mainPanel, KeyMapFactory.TETRIO_MODE);
    }

    @Override
    protected void initializeBoards() {
        long seed = System.currentTimeMillis();
        boards[0] = new NESBoardWithPhysics(BOARD1_X, BOARD1_Y, seed);
        boards[1] = new NESBoardWithPhysics(BOARD2_X, BOARD2_Y, seed);
    }
}
