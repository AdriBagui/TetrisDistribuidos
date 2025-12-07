package tetris.panels.twoPlayerPanels;

import main.MainPanel;
import tetris.boards.BoardWithPhysics;
import tetris.keyMaps.KeyMapFactory;
import tetris.keyMaps.TwoPlayersKeyMap;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class LocalTwoPlayersPanel extends TwoPlayersPanel {
    public LocalTwoPlayersPanel(MainPanel mainPanel, int tetrisMode) {
        super(mainPanel, KeyMapFactory.createTwoPlayersKeyMap(tetrisMode, mainPanel));
    }

    @Override
    public void update() {
        if (boards[0].isAlive()) boards[0].update();
        if (boards[1].isAlive()) boards[1].update();

        if (!boards[0].isAlive() || !boards[1].isAlive())
            gameOver = true;
    }

    @Override
    public void resetGame() {
        super.resetGame();
        ((TwoPlayersKeyMap) keyMap).setBoards((BoardWithPhysics) boards[0], (BoardWithPhysics) boards[1]);
    }
}
