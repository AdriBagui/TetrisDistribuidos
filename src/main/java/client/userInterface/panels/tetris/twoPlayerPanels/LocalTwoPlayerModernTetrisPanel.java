package client.userInterface.panels.tetris.twoPlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.io.SenderBoardOutputHandler;
import tetris.boards.io.ReceiverBoardInputHandler;
import tetris.boards.io.ReceiverModernTetrisBoardInputHandler;
import tetris.boards.modernTetris.SenderModernTetrisBoardWithPhysics;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static client.userInterface.panels.MainPanel.*;

/**
 * Manages a local 1v1 Modern Tetris game.
 * <p>
 * In this mode, two players play on the same screen. This class simulates network communication
 * using {@link PipedOutputStream} and {@link PipedInputStream} to handle garbage sending mechanics.
 * Even though it is local, the architecture treats the two boards as "Sender" and "Receiver" to
 * reuse the logic defined for online play.
 * </p>
 */
public class LocalTwoPlayerModernTetrisPanel extends LocalTwoPlayersTetrisPanel {
    private PipedOutputStream player1OutputStream;
    private PipedOutputStream player2OutputStream;
    private PipedInputStream player1InputStream;
    private PipedInputStream player2InputStream;
    private ReceiverBoardInputHandler player1ReceiverBoardInputHandler;
    private ReceiverBoardInputHandler player2ReceiverBoardInputHandler;
    private long seed;
    private boolean connectionClosed;

    /**
     * Constructs a LocalTwoPlayerModernTetrisPanel.
     *
     * @param mainPanel The parent container.
     */
    public LocalTwoPlayerModernTetrisPanel(MainPanel mainPanel) {
        super(mainPanel);

        player1OutputStream = null;
        player2OutputStream = null;
        player1InputStream = null;
        player2InputStream = null;
        player1ReceiverBoardInputHandler = null;
        player2ReceiverBoardInputHandler = null;
    }

    /**
     * Closes the piped streams connecting the two local boards.
     * Synchronized to prevent concurrency issues during the game over sequence.
     */
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
     * Handles IO exceptions during local communication (should be rare).
     * Closes all streams to prevent resource leaks.
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

    /**
     * Updates the game state and ensures communications are closed cleanly when the game ends.
     */
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

    /**
     * Checks who won based on survival.
     *
     * @return "Player 1 has won!" if Player 1 is alive, "Player 2 has won!" if Player 2 is alive, "It's a tie!" otherwise.
     */
    @Override
    protected String checkWinner() {
        boolean player1Alive = boards[0].isAlive();
        boolean player2Alive = boards[1].isAlive();

        if (player1Alive && !player2Alive) return "Player 1 has won!";
        else if (!player1Alive && player2Alive) return "Player 2 has won!";
        else return "It's a tie!";
    }

    /**
     * Determines if the game is over (when either player tops out).
     */
    @Override
    protected boolean checkGameOver() {
        return (!boards[0].isAlive() || !boards[1].isAlive());
    }

    /**
     * Resets the game and re-opens communication pipes.
     */
    @Override
    protected void resetGame() {
        super.resetGame();
        setConnectionClosed(false);
    }

    /**
     * Initializes two {@link SenderModernTetrisBoardWithPhysics} connected via pipes.
     * Starts separate threads to handle input reception for each board (processing garbage).
     */
    @Override
    protected void initializeBoards() {
        keyInputHandler.enablePlayer2Controls();
        keyInputHandler.enableModernTetrisControls();

        connectBoards();
        boards[0] = new SenderModernTetrisBoardWithPhysics(BOARD1_X, BOARD1_Y, seed, new SenderBoardOutputHandler(player1OutputStream, this));
        player1ReceiverBoardInputHandler = new ReceiverModernTetrisBoardInputHandler(player1InputStream, (SenderModernTetrisBoardWithPhysics) boards[0], null, this);
        player1ReceiverBoardInputHandler.start();

        boards[1] = new SenderModernTetrisBoardWithPhysics(BOARD2_X, BOARD2_Y, seed, new SenderBoardOutputHandler(player2OutputStream, this));
        player2ReceiverBoardInputHandler = new ReceiverModernTetrisBoardInputHandler(player2InputStream, (SenderModernTetrisBoardWithPhysics) boards[1], null, this);
        player2ReceiverBoardInputHandler.start();
    }

    /**
     * Establishes the PipedInputStream and PipedOutputStream connections.
     */
    protected void connectBoards() {
        seed = System.currentTimeMillis();

        try {
            player1OutputStream = new PipedOutputStream();
            player2InputStream = new PipedInputStream(player1OutputStream);
            player2OutputStream = new PipedOutputStream();
            player1InputStream = new PipedInputStream(player2OutputStream);
        }
        catch (IOException ioe) { System.out.println("FATAL ERROR while trying to connect local boards"); } // This should never happen, if it does your computer is broken sry
    }

    private synchronized boolean isConnectionUp() { return !connectionClosed; }
    private synchronized void setConnectionClosed(boolean connectionClosed) { this.connectionClosed = connectionClosed;}
}