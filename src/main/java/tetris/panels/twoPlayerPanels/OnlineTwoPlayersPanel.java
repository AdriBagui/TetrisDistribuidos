package tetris.panels.twoPlayerPanels;

import main.MainPanel;
import tetris.boards.BoardWithPhysics;
import tetris.boards.ReceiverBoardInputHandler;
import tetris.boards.SenderBoardOutputHandler;
import tetris.keyMaps.KeyInputHandler;

import java.io.IOException;
import java.net.Socket;

public abstract class OnlineTwoPlayersPanel extends TwoPlayersPanel {
    protected Socket boardsSocket;
    protected ReceiverBoardInputHandler receiverBoardInputHandler;
    protected long seed;
    protected boolean rivalGameOver;
    protected boolean connectionLost;

    public OnlineTwoPlayersPanel(MainPanel mainPanel) {
        super(mainPanel);

        boardsSocket = null;
        receiverBoardInputHandler = null;
        seed = -1;

        rivalGameOver = false;
        connectionLost = false;
    }

    public void setSocket(Socket socket) {
        this.boardsSocket = socket;
    }
    public void setSeed(long seed) { this.seed = seed;}

    /**
     * Called by ReceiverBoardInputHandler when IOException occurs.
     */
    public void handleConnectionError() {
        if (!isGameOver()) {
            setConnectionLost(true);
            try { closeSocket(); }
            catch (IOException e) {
                System.out.println("FATAL ERROR while trying to close socket to opponent boards"); // This should never happen, if it does your computer is broken sry
            }
        }
    }

    @Override
    public void closeCommunications() {
        try {
            setRivalGameOver(true);
            boardsSocket.shutdownInput();
            if (isGameOver()) closeSocket();
        }
        catch (IOException e) { System.out.println("FATAL ERROR while trying to close socket to opponent boards"); } // This should never happen, if it does your computer is broken sry
    }

    @Override
    public void update() {
        boolean gameOverCauseOfRivalWin = isRivalGameOver();
        boolean gameOverCauseOfRival = (gameOverCauseOfRivalWin || isConnectionLost());
        if (gameOverCauseOfRival || isGameOver()) {
            try {
                boardsSocket.shutdownOutput();
                if (gameOverCauseOfRivalWin) closeSocket();
            }
            catch (IOException e) { System.out.println("FATAL ERROR while trying to shutdown output to opponent boards"); } // This should never happen, if it does your computer is broken sry
        }
    }

    @Override
    protected void updateBoards() {
        if (boards[0].isAlive()) boards[0].update();
    }

    @Override
    protected void resetGame() {
        super.resetGame();
        setConnectionLost(false);
        setRivalGameOver(false);
        keyInputHandler.setPlayer1Board((BoardWithPhysics) boards[0]);
    }

    protected synchronized void closeSocket() throws IOException {
        boardsSocket.close();
    }

    @Override
    public synchronized boolean isGameOver() { return super.isGameOver(); }
    @Override
    protected synchronized void setGameOver(boolean gameOver) { super.setGameOver(gameOver);}
    private synchronized boolean isRivalGameOver() { return rivalGameOver; }
    private synchronized void setRivalGameOver(boolean rivalGameOver) { this.rivalGameOver = rivalGameOver;}
    private synchronized boolean isConnectionLost() { return connectionLost; }
    private synchronized void setConnectionLost(boolean connectionLost) { this.connectionLost = connectionLost;}
}
