package tetris.panels.twoPlayerPanels;

import main.MainPanel;
import tetris.boards.BoardWithPhysics;
import tetris.keyMaps.KeyMapFactory;
import tetris.keyMaps.TwoPlayersKeyMap;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class LocalTwoPlayersPanel extends TwoPlayersPanel {
    protected Socket board1Socket;
    protected Socket board2Socket;
    protected long seed;

    public LocalTwoPlayersPanel(MainPanel mainPanel, int tetrisMode) {
        super(mainPanel, KeyMapFactory.createTwoPlayersKeyMap(tetrisMode, mainPanel));

        board1Socket = null;
        board2Socket = null;
        seed = -1;
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

    protected void connectBoards() {
        seed = System.currentTimeMillis();

        Thread board1Thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (ServerSocket tempServerSocket = new ServerSocket(7776)) {
                    board1Socket = tempServerSocket.accept();
                }
                catch (IOException ioe) { ioe.printStackTrace(); }
            }
        });

        Thread board2Thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                    board2Socket = new Socket("localhost", 7776);
                }
                catch (IOException ioe) { ioe.printStackTrace(); }
                catch (InterruptedException ie) { ie.printStackTrace(); }
            }
        });

        board1Thread.start();
        board2Thread.start();

        try {
            board1Thread.join();
            board2Thread.join();
        }
        catch (InterruptedException ie) { ie.printStackTrace(); }
    }
}
