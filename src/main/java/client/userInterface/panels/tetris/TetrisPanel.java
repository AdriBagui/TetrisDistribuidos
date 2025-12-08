package client.userInterface.panels.tetris;

import client.userInterface.panels.MainPanel;
import tetris.boards.Board;
import client.keyMaps.KeyInputHandler;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import static client.userInterface.panels.MainPanel.*;

/**
 * An abstract base class representing a generic Tetris game panel.
 * <p>
 * This class handles the core game loop, rendering cycle, and state management
 * common to all game modes (Single Player, Multiplayer, NES, Modern).
 * It manages the game timer, updates the game state at a fixed frame rate, and triggers repaints.
 * </p>
 */
public abstract class TetrisPanel extends JPanel {
    /** Target frames per second for the modern game loop (approx 60 Hz). */
    public static final double FPS = 62.5;

    /** The duration of a single frame in milliseconds. */
    public static final int MILLISECONDS_PER_FRAME = (int) (1000/ FPS);

    /** The specific FPS used for NES emulation accuracy (approx 60.09 Hz for NTSC, but logic often uses 50-60). */
    public static final double NES_FPS = 50.0070;

    /** Handles keyboard input events for controlling the game. */
    protected KeyInputHandler keyInputHandler;

    /** Reference to the main application panel for navigation and resource access. */
    protected MainPanel mainPanel;

    /** Array of game boards active in this panel (e.g., 1 for single player, 2 for multiplayer). */
    protected Board[] boards;

    /** Timer responsible for executing the game loop. */
    private Timer gameLoopTimer;

    /** Flag indicating if the game has ended. */
    protected boolean gameOver;

    /**
     * Constructs a new TetrisPanel.
     * Sets the panel size and background color.
     *
     * @param mainPanel The parent container managing navigation.
     */
    public TetrisPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;

        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(BACKGROUND_COLOR);

        boards = null;
        gameOver = false;
    }

    /**
     * Updates the logic for all active boards.
     * This method must be implemented by subclasses to define specific update behavior.
     */
    protected abstract void updateBoards();

    /**
     * Checks the game state to determine if the game is over.
     *
     * @return {@code true} if the game condition is met (e.g., player topped out), {@code false} otherwise.
     */
    protected abstract boolean checkGameOver();

    /**
     * Initializes the specific board instances required for the game mode.
     * Subclasses must instantiate the {@code boards} array here.
     */
    protected abstract void initializeBoards();

    /**
     * Sets the input handler for this panel.
     *
     * @param keyInputHandler The handler to process key events.
     */
    public void setKeyInputHandler(KeyInputHandler keyInputHandler) { this.keyInputHandler = keyInputHandler; }

    /**
     * Checks if the game is currently in a "Game Over" state.
     *
     * @return {@code true} if the game is over, {@code false} otherwise.
     */
    public boolean isGameOver() { return gameOver; }

    /**
     * Starts a new game session.
     * Resets the game state and schedules the game loop timer.
     */
    public void startGame() {
        resetGame();
        gameLoopTimer = new Timer();
        gameLoopTimer.scheduleAtFixedRate(new GameLoop(), 0, MILLISECONDS_PER_FRAME);
    }

    /**
     * Performs a single update cycle.
     * Updates board logic and checks for game over conditions.
     */
    public void update() {
        updateBoards();
        if (checkGameOver()) setGameOver(true);
    }

    /**
     * Renders the game state.
     * Draws all active boards onto the provided graphics context.
     *
     * @param g2 The {@link Graphics2D} context to draw on.
     */
    public void draw(Graphics2D g2) {
        for (Board board: boards) {
            if (board != null) board.draw(g2);
        }
    }

    /**
     * Sets the game over state.
     *
     * @param gameOver {@code true} to mark the game as ended.
     */
    protected void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    /**
     * Resets the game to its initial state.
     * Clears input mappings, resets the game over flag, and re-initializes boards.
     */
    protected void resetGame() {
        keyInputHandler.disableGoBackToStartMenuControls();
        keyInputHandler.setPlayer1Board(null);
        keyInputHandler.setPlayer2Board(null);
        setGameOver(false);
        initializeBoards();
    }

    /**
     * Custom painting component for Swing.
     * Delegates the actual drawing logic to {@link #draw(Graphics2D)}.
     *
     * @param g The {@link Graphics} context.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        draw(g2);
    }

    /**
     * Inner class representing the main game loop task.
     * Executed periodically by the {@link Timer}.
     */
    private class GameLoop extends TimerTask {
        /**
         * Runs the game loop cycle: updates logic, repaints the screen, and handles game over cleanup.
         */
        @Override
        public void run() {
            update();
            repaint();

            if (isGameOver()) {
                gameLoopTimer.cancel();
                // Enable navigation back to menu and disable gameplay controls
                keyInputHandler.enableGoBackToStartMenuControls();
                keyInputHandler.disablePlayer2Controls();
                keyInputHandler.disableNESControls();
                keyInputHandler.disableModernTetrisControls();
            }
        }
    }
}