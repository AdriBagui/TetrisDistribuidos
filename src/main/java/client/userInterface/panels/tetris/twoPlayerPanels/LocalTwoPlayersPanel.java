package client.userInterface.panels.tetris.twoPlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.BoardWithPhysics;

public abstract class LocalTwoPlayersPanel extends TwoPlayersPanel {
    public LocalTwoPlayersPanel(MainPanel mainPanel) {
        super(mainPanel);
    }

    @Override
    protected void updateBoards() {
        if (boards[0].isAlive()) boards[0].update();
        if (boards[1].isAlive()) boards[1].update();
    }

    @Override
    protected void resetGame() {
        super.resetGame();
        keyInputHandler.setPlayer1Board((BoardWithPhysics) boards[0]);
        keyInputHandler.setPlayer2Board((BoardWithPhysics) boards[1]);
    }
}
