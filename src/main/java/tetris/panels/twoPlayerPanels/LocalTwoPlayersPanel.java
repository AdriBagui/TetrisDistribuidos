package tetris.panels.twoPlayerPanels;

import main.MainPanel;
import tetris.boards.BoardWithPhysics;

public abstract class LocalTwoPlayersPanel extends TwoPlayersPanel {
    public LocalTwoPlayersPanel(MainPanel mainPanel) {
        super(mainPanel);
    }

    @Override
    public void update() {
        if (boards[0].isAlive()) boards[0].update();
        if (boards[1].isAlive()) boards[1].update();

        if (!boards[0].isAlive() || !boards[1].isAlive())
            setGameOver(true);
    }

    @Override
    public void resetGame() {
        super.resetGame();
        keyInputHandler.setPlayer1Board((BoardWithPhysics) boards[0]);
        keyInputHandler.setPlayer2Board((BoardWithPhysics) boards[1]);
    }
}
