package client.userInterface.panels.tetris.twoPlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.BoardWithPhysics;
import tetris.boards.io.ReceiverBoardInputHandler;
import tetris.boards.io.SenderBoardOutputHandler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;

import static client.userInterface.panels.MainPanel.*;
import static client.userInterface.panels.MainPanel.FRAME_PADDING;
import static client.userInterface.panels.MainPanel.GAME_OVER_FONT;
import static client.userInterface.panels.MainPanel.MEDIUM_MESSAGE_FONT;
import static client.userInterface.panels.MainPanel.PANEL_WIDTH;
import static client.userInterface.panels.MainPanel.TEXT_COLOR;

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

    /** Flag indicating if the remote player has topped out (Game Over). */
    protected boolean opponentLost;

    /** Flag indicating if the input is still up. */
    protected boolean outputDown;

    /** Flag indicating if the output is still up. */
    protected boolean inputDown;

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
        opponentLost = false;
        outputDown = false;
        inputDown = false;
        connectionLost = false;
    }

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
        setOpponentLost(true);

        if (isConnectionUp()) {
            try {
                if (!inputDown) {
                    boardsSocket.shutdownInput();
                    inputDown = true;
                }
                if (hasLocalLost()) closeSocket();
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
        if (isConnectionUp() && (!isGameOver())) {
            setConnectionLost(true);
            senderBoardOutputHandler.notifyConnectionLost();

            try { closeSocket(); }
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
                    if (!outputDown) {
                        boardsSocket.shutdownOutput();
                        outputDown = true;
                    }
                    if (hasOpponentLost()) closeSocket();
                }
                catch (IOException e) { System.out.println("FATAL ERROR while trying to shutdown output to opponent boards"); } // This should never happen, if it does your computer is broken sry
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);

        if (!isConnectionUp()) {
            String gameOverMessage = "GAME OVER";
            String message;
            int messageWidth = 17*CELL_SIZE;
            int gameOverWidth = 10*CELL_SIZE;
            int endGameWidth = 14*CELL_SIZE;
            int bannerWidth = messageWidth + 2*FRAME_PADDING;
            int bannerHeight = 5*CELL_SIZE + 2*FRAME_PADDING;
            int bannerY = 7*CELL_SIZE;

            message = "Server connection lost";

            // Draw Background Banner
            g2.setColor(BACKGROUND_COLOR);
            g2.fillRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);
            g2.setColor(TEXT_COLOR);
            g2.drawRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);

            // Draw Texts
            g2.setColor(TEXT_COLOR);
            g2.setFont(GAME_OVER_FONT);
            g2.drawString(gameOverMessage, (PANEL_WIDTH-gameOverWidth)/2, (int) (bannerY + 1.5*FRAME_PADDING));
            g2.drawString(message, (PANEL_WIDTH-messageWidth)/2, (int) (bannerY + 2.5*FRAME_PADDING));

            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(MEDIUM_MESSAGE_FONT);
            g2.drawString("Press '" + KeyEvent.getKeyText(keyInputHandler.getGoBackToMenuKey()) + "' to go back to the start menu", (PANEL_WIDTH-endGameWidth)/2, (int) (bannerY + 3.5*FRAME_PADDING));
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
     * @return {@code true} if the rival has finished.
     */
    @Override
    protected synchronized boolean checkGameOver() {
        return hasOpponentLost();
    }

    /**
     * Resets the game state for a new match.
     */
    @Override
    protected void resetGame() {
        super.resetGame();
        setLocalLost(false);
        setOpponentLost(false);
        outputDown = false;
        inputDown = false;
        setConnectionLost(false);
        keyInputHandler.setPlayer1Board((BoardWithPhysics) boards[0]);
    }

    /**
     * Closes the underlying socket.
     *
     * @throws IOException If an I/O error occurs when closing this socket.
     */
    protected synchronized void closeSocket() throws IOException {
        boardsSocket.close();
    }

    @Override
    public synchronized boolean isGameOver() { return super.isGameOver(); }
    @Override
    protected synchronized void setGameOver(boolean gameOver) { super.setGameOver(gameOver); }

    protected synchronized boolean hasLocalLost() { return localLost; }
    protected synchronized void setLocalLost(boolean localLost) { this.localLost = localLost; }

    protected synchronized boolean hasOpponentLost() { return opponentLost; }
    protected synchronized void setOpponentLost(boolean opponentLost) { this.opponentLost = opponentLost; }

    protected synchronized boolean isConnectionUp() { return !connectionLost; }
    protected synchronized void setConnectionLost(boolean connectionLost) { this.connectionLost = connectionLost; }
}