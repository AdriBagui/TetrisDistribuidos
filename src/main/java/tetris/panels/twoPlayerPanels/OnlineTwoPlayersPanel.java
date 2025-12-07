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

    public OnlineTwoPlayersPanel(MainPanel mainPanel) {
        super(mainPanel);

        boardsSocket = null;
        receiverBoardInputHandler = null;
        seed = -1;

        rivalGameOver = false;
    }

    public void setSocket(Socket socket) {
        this.boardsSocket = socket;
    }
    public void setSeed(long seed) { this.seed = seed;}

    @Override
    public void closeCommunications() {
        try {
            rivalGameOver = true;
            boardsSocket.shutdownInput();
            if (gameOver) closeSocket();
        }
        catch (IOException e) { System.out.println("FATAL ERROR while trying to close socket to opponent boards"); } // This should never happen, if it does your computer is broken sry
    }

    @Override
    public void update() {
        if (boards[0].isAlive()) boards[0].update();

        if (!boards[0].isAlive() || rivalGameOver) {
            gameOver = true;
            try {
                boardsSocket.shutdownOutput();
                if (rivalGameOver) closeSocket();
            }
            catch (IOException e) { System.out.println("FATAL ERROR while trying to shutdown output to opponent boards"); } // This should never happen, if it does your computer is broken sry
        }
    }

    @Override
    protected void resetGame() {
        super.resetGame();
        keyInputHandler.setPlayer1Board((BoardWithPhysics) boards[0]);
    }

    protected synchronized void closeSocket() throws IOException {
        boardsSocket.close();
    }
}
