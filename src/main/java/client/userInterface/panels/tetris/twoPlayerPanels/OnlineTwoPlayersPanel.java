package client.userInterface.panels.tetris.twoPlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.BoardWithPhysics;
import tetris.boards.io.ReceiverBoardInputHandler;
import tetris.boards.io.SenderBoardOutputHandler;

import java.io.IOException;
import java.net.Socket;

/**
 * An abstract base class for all online multiplayer Tetris game modes.
 * <p>
 * This class extends {@link TwoPlayersTetrisPanel} to handle the specific complexities of networked gameplay.
 * It manages the TCP socket connection, synchronization between the local game loop and network threads,
 * and the state of the opponent (rival).
 * </p>
 * <p>
 * <b>Synchronization Strategy:</b><br>
 * Methods that modify connection state ({@code closeCommunications}, {@code handleConnectionError}) or
 * check game-over conditions are synchronized. This prevents race conditions where the socket might be
 * closed by one thread (e.g., input receiver) while another (e.g., game loop) tries to write to it.
 * </p>
 */
public abstract class OnlineTwoPlayersPanel extends TwoPlayersTetrisPanel {
    /** The active socket connection to the server (or proxied to the opponent). */
    protected Socket boardsSocket;

    /** Handler responsible for encoding and sending local events to the network. */
    protected SenderBoardOutputHandler senderBoardOutputHandler;

    /** Handler responsible for decoding network messages and applying them to the remote board representation. */
    protected ReceiverBoardInputHandler receiverBoardInputHandler;

    /** The random seed used to initialize both boards deterministically. */
    protected long seed;

    /** Flag indicating if the local player has topped out (Game Over). */
    protected boolean localLost;

    /** Flag indicating if the remote player has closed its output (because the game is over for him, either by topping out or by winning the game). */
    protected boolean opponentClosedOutput;

    /** Flag indicating if the network connection has been unexpectedly dropped. */
    protected boolean connectionLost;

    /**
     * Constructs the online panel.
     *
     * @param mainPanel The parent container.
     */
    public OnlineTwoPlayersPanel(MainPanel mainPanel) {
        super(mainPanel);

        boardsSocket = null;
        receiverBoardInputHandler = null;
        seed = -1;

        localLost = false;
        opponentClosedOutput = false;
        connectionLost = false;
    }

    /**
     * Returns whether the game should is finished according to game rules (that is, for modern tetris one of the players
     * has lost and for NES tetris both players have lost). In online games is distinct from game over because in online
     * games the local player doesn't have all the information to know the winner until the opponent finishes, so the game
     * is not over until both players finish. However, the local player should indicate when his game finishes somehow without
     * setting is game as game over, this is what this method is for.
     *
     * @return a boolean indicating if the game should close
     */
    protected abstract boolean hasLocalGameFinished();

    /**
     * Sets the socket used for communication.
     *
     * @param socket The connected socket instance.
     */
    public void setSocket(Socket socket) {
        this.boardsSocket = socket;
    }

    /**
     * Sets the shared random seed for the game.
     *
     * @param seed The long value seed.
     */
    public void setSeed(long seed) { this.seed = seed;}

    /**
     * Closes communications gracefully when the opponent finishes or disconnects.
     * <p>
     * This method is synchronized to ensure it doesn't conflict with the update loop trying to write data.
     * It marks the rival as "Game Over" and shuts down the input stream.
     * </p>
     */
    @Override
    public synchronized void closeCommunications() { // synchronized so that update and closeCommunications don't close the socket at the same time
        // If the code reaches here means the rival has closed his output
        setOpponentClosedOutput(true);

        if (isConnectionUp()) {
            try {
                boardsSocket.shutdownInput();

                // In modern tetris the game will be over and if local has lost the output will be down, if opponent has lost the output will be up
                // In NES tetris the game won't be over unless the local has lost (in that case the output will be down)
                if (hasLocalGameFinished()) {
                    if (!hasLocalLost()) boardsSocket.shutdownOutput();
                    boardsSocket.close();
                }
            }
            catch (IOException e) { System.out.println("FATAL ERROR while trying to close socket to opponent boards"); } // This should never happen, if it does your computer is broken sry
        }
    }

    /**
     * Handles unexpected network errors (e.g., IOException during read/write).
     * Marks the connection as lost and attempts to close the socket.
     */
    @Override
    public synchronized void handleConnectionError() { // synchronized so that it doesn't interfere with the normal socket closing
        if (isConnectionUp() && (!checkGameOver())) {
            setConnectionLost(true);
            senderBoardOutputHandler.notifyConnectionLost();

            try { boardsSocket.close(); }
            catch (IOException e) {
                System.out.println("FATAL ERROR while trying to close socket to opponent boards"); // This should never happen, if it does your computer is broken sry
            }
        }
    }

    /**
     * Main update loop.
     * <p>
     * Synchronized block ensures that if the local player finishes (Game Over), the socket output is
     * shut down safely without conflicting with other threads closing the socket.
     * </p>
     */
    @Override
    public void update() {
        super.update();

        synchronized (this) { // synchronized so that closeCommunications and update don't close the socket at the same time
            if (hasLocalLost() && isConnectionUp()) {
                try {
                    boardsSocket.shutdownOutput();
                    if (hasOpponentClosedOutput()) boardsSocket.close();
                }
                catch (IOException e) { System.out.println("FATAL ERROR while trying to shutdown output to opponent boards"); } // This should never happen, if it does your computer is broken sry
            }
        }
    }

    /**
     * Updates the local board logic.
     * Only the local player's board (index 0) is updated via physics/logic.
     * The remote board (index 1) is updated via network messages.
     */
    @Override
    protected void updateBoards() {
        if (boards[0].isAlive()) boards[0].update();
        else setLocalLost(true);
    }

    /**
     * Checks if the game is over.
     * In online play, the local game loop primarily cares if the <i>rival</i> is over to determine global state,
     * or if the local player is over.
     *
     * @return {@code true} if the localhost and rival have finished or if the connection is no longer up.
     */
    @Override
    protected synchronized boolean checkGameOver() {
        return (hasLocalGameFinished() && hasOpponentClosedOutput()) || !isConnectionUp();
    }

    /**
     * Resets the game state for a new match.
     */
    @Override
    protected void resetGame() {
        super.resetGame();
        setLocalLost(false);
        setOpponentClosedOutput(false);
        setConnectionLost(false);
        keyInputHandler.setPlayer1Board((BoardWithPhysics) boards[0]);
    }

    @Override
    public synchronized boolean isGameOver() { return super.isGameOver(); }
    @Override
    protected synchronized void setGameOver(boolean gameOver) { super.setGameOver(gameOver); }

    protected synchronized boolean hasLocalLost() { return localLost; }
    protected synchronized void setLocalLost(boolean localLost) { this.localLost = localLost; }

    protected synchronized boolean hasOpponentClosedOutput() { return opponentClosedOutput; }
    protected synchronized void setOpponentClosedOutput(boolean opponentClosedOutput) { this.opponentClosedOutput = opponentClosedOutput; }

    protected synchronized boolean isConnectionUp() { return !connectionLost; }
    protected synchronized void setConnectionLost(boolean connectionLost) { this.connectionLost = connectionLost; }
}