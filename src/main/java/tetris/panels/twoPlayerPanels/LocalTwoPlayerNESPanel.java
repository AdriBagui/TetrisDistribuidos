package tetris.panels.twoPlayerPanels;

import main.MainPanel;
import tetris.boards.nes.NESBoardWithPhysics;

import static main.MainPanel.*;

public class LocalTwoPlayerNESPanel extends LocalTwoPlayersPanel {
    public LocalTwoPlayerNESPanel(MainPanel mainPanel) {
        super(mainPanel);
    }

    @Override
    public void closeCommunications() {
        // Does nothing because there are no communications
    }

    @Override
    protected void initializeBoards() {
        keyInputHandler.enablePlayer2Controls();
        keyInputHandler.enableNESControls();
        long seed = System.currentTimeMillis();
        boards[0] = new NESBoardWithPhysics(BOARD1_X, BOARD1_Y, seed);
        boards[1] = new NESBoardWithPhysics(BOARD2_X, BOARD2_Y, seed);
    }
}
