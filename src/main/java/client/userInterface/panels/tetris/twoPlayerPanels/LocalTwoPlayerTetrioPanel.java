package client.userInterface.panels.tetris.twoPlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.io.SenderBoardOutputHandler;
import tetris.boards.io.ReceiverBoardInputHandler;
import tetris.boards.io.ReceiverTetrioBoardInputHandler;
import tetris.boards.tetrio.SenderTetrioBoardWithPhysics;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static client.userInterface.panels.MainPanel.*;

public class LocalTwoPlayerTetrioPanel extends LocalTwoPlayersPanel {
    private PipedOutputStream player1OutputStream;
    private PipedOutputStream player2OutputStream;
    private PipedInputStream player1InputStream;
    private PipedInputStream player2InputStream;
    private ReceiverBoardInputHandler player1ReceiverBoardInputHandler;
    private ReceiverBoardInputHandler player2ReceiverBoardInputHandler;
    private long seed;
    private boolean connectionClosed;

    public LocalTwoPlayerTetrioPanel(MainPanel mainPanel) {
        super(mainPanel);

        player1OutputStream = null;
        player2OutputStream = null;
        player1InputStream = null;
        player2InputStream = null;
        player1ReceiverBoardInputHandler = null;
        player2ReceiverBoardInputHandler = null;
    }

    @Override
    public synchronized void closeCommunications() {
        // If the code reaches here means the rival has closed his output (and in this case (local) the game is also over
        // cause the rival and local players are running on the same thread, the game loop)
        if (isConnectionUp()) {
            try {
                player1InputStream.close();
                player2InputStream.close();
                setConnectionClosed(true);
            } catch (IOException e) { System.out.println("FATAL ERROR while trying to shutdown inputs from opponent boards"); } // This should never happen, if it does your computer is broken sry
        }
    }

    /**
     * Called by ReceiverBoardInputHandler and SenderBoardsOutputHandler when IOException occurs.
     */
    @Override
    public synchronized void handleConnectionError() { // synchronized so that it doesn't interfere with the normal socket closing
        if (isConnectionUp()) {
            try {
                if (!isGameOver()) {
                    player1OutputStream.close();
                    player2OutputStream.close();
                }
                player1InputStream.close();
                player2InputStream.close();
            }
            catch (IOException e) {
                System.out.println("FATAL ERROR while trying to close socket to opponent boards"); // This should never happen, if it does your computer is broken sry
            }
        }
    }

    @Override
    public void update() {
        super.update();

        synchronized (this) {
            if (isGameOver() && isConnectionUp()) {
                try {
                    player1OutputStream.close();
                    player2OutputStream.close();
                } catch (IOException e) { System.out.println("FATAL ERROR while trying to shutdown outputs to opponent boards"); } // This should never happen, if it does your computer is broken sry
            }
        }
    }

    @Override
    protected int checkWinner() {
        boolean player1Alive = boards[0].isAlive();
        boolean player2Alive = boards[1].isAlive();

        if (player1Alive && !player2Alive) return 1;
        else if (!player1Alive && player2Alive) return 2;
        else return 0;
    }

    @Override
    protected boolean checkGameOver() {
        return (!boards[0].isAlive() || !boards[1].isAlive());
    }

    @Override
    protected void resetGame() {
        super.resetGame();
        setConnectionClosed(false);
    }

    @Override
    protected void initializeBoards() {
        keyInputHandler.enablePlayer2Controls();
        keyInputHandler.enableTetrioControls();

        connectBoards();
        boards[0] = new SenderTetrioBoardWithPhysics(BOARD1_X, BOARD1_Y, seed, new SenderBoardOutputHandler(player1OutputStream, this));
        player1ReceiverBoardInputHandler = new ReceiverTetrioBoardInputHandler(player1InputStream, (SenderTetrioBoardWithPhysics) boards[0], null, this);
        player1ReceiverBoardInputHandler.start();

        boards[1] = new SenderTetrioBoardWithPhysics(BOARD2_X, BOARD2_Y, seed, new SenderBoardOutputHandler(player2OutputStream, this));
        player2ReceiverBoardInputHandler = new ReceiverTetrioBoardInputHandler(player2InputStream, (SenderTetrioBoardWithPhysics) boards[1], null, this);
        player2ReceiverBoardInputHandler.start();
    }

    protected void connectBoards() {
        seed = System.currentTimeMillis();

        try {
            player1OutputStream = new PipedOutputStream();
            player2InputStream = new PipedInputStream(player1OutputStream);
            player2OutputStream = new PipedOutputStream();
            player1InputStream = new PipedInputStream(player2OutputStream);
        }
        catch (IOException ioe) { ioe.printStackTrace(); }
    }

    private synchronized boolean isConnectionUp() { return !connectionClosed; }
    private synchronized void setConnectionClosed(boolean connectionClosed) { this.connectionClosed = connectionClosed;}
}
