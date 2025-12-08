package client.userInterface.panels.tetris.twoPlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.BoardWithPhysics;
import tetris.boards.io.ReceiverBoardInputHandler;
import tetris.boards.io.SenderBoardOutputHandler;

import java.io.IOException;
import java.net.Socket;

public abstract class OnlineTwoPlayersPanel extends TwoPlayersPanel {
    protected Socket boardsSocket;
    protected SenderBoardOutputHandler senderBoardOutputHandler;
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

    @Override
    public synchronized void closeCommunications() { // synchronized so that update and closeCommunications don't close the socket at the same time
        // If the code reaches here means the rival has closed his output
        setRivalGameOver(true);

        if (isConnectionUp()) {
            try {
                boardsSocket.shutdownInput();
                if (isGameOver()) closeSocket();
            }
            catch (IOException e) { System.out.println("FATAL ERROR while trying to close socket to opponent boards"); } // This should never happen, if it does your computer is broken sry
        }
    }

    /**
     * Called by ReceiverBoardInputHandler and SenderBoardsOutputHandler when IOException occurs.
     */
    @Override
    public synchronized void handleConnectionError() { // synchronized so that it doesn't interfere with the normal socket closing
        if (isConnectionUp() && (!isGameOver() || !isRivalGameOver())) {
            setConnectionLost(true);
            senderBoardOutputHandler.notifyConnectionLost();

            try { closeSocket(); }
            catch (IOException e) {
                System.out.println("FATAL ERROR while trying to close socket to opponent boards"); // This should never happen, if it does your computer is broken sry
            }
        }
    }

    @Override
    public void update() {
        super.update();

        synchronized (this) { // synchronized so that closeCommunications and update don't close the socket at the same time
            if (isGameOver() && isConnectionUp()) {
                try {
                    boardsSocket.shutdownOutput();
                    if (isRivalGameOver()) closeSocket();
                }
                catch (IOException e) { System.out.println("FATAL ERROR while trying to shutdown output to opponent boards"); } // This should never happen, if it does your computer is broken sry
            }
        }
    }

    @Override
    protected void updateBoards() {
        if (boards[0].isAlive()) boards[0].update();
    }

    @Override
    protected synchronized boolean checkGameOver() {
        return isRivalGameOver();
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
    protected synchronized boolean isRivalGameOver() { return rivalGameOver; }
    protected synchronized void setRivalGameOver(boolean rivalGameOver) { this.rivalGameOver = rivalGameOver;}
    protected synchronized boolean isConnectionUp() { return !connectionLost; }
    protected synchronized void setConnectionLost(boolean connectionLost) { this.connectionLost = connectionLost;}
}
