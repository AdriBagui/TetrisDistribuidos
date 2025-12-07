package tetris.panels.twoPlayerPanels;

import main.MainPanel;
import tetris.boards.BoardWithPhysics;
import tetris.boards.ReceiverBoardInputHandler;
import tetris.keyMaps.KeyMapFactory;
import tetris.keyMaps.SinglePlayerKeyMap;

import java.net.Socket;

public abstract class OnlineTwoPlayersPanel extends TwoPlayersPanel {
    protected Socket boardsSocket;
    protected ReceiverBoardInputHandler receiverBoardInputHandler;
    protected long seed;

    public OnlineTwoPlayersPanel(MainPanel mainPanel, int tetrisMode) {
        super(mainPanel, KeyMapFactory.createSinglePlayerKeyMap(tetrisMode, mainPanel));

        boardsSocket = null;
        receiverBoardInputHandler = null;
        seed = -1;
    }

    public void setSocket(Socket socket) {
        this.boardsSocket = socket;
    }
    public void setSeed(long seed) { this.seed = seed;}

    @Override
    public void update() {
        if (boards[0].isAlive()) boards[0].update();

        if (!boards[0].isAlive() || !boards[1].isAlive()) {
            gameOver = true;
            receiverBoardInputHandler.interrupt();
        }
    }

    @Override
    public void resetGame() {
        super.resetGame();
        ((SinglePlayerKeyMap) keyMap).setBoard((BoardWithPhysics) boards[0]);
    }
}
