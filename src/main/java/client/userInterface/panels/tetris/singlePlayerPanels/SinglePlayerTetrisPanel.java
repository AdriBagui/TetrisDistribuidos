package client.userInterface.panels.tetris.singlePlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.Board;
import tetris.boards.BoardWithPhysics;
import client.userInterface.panels.tetris.TetrisPanel;

import java.awt.*;
import java.awt.event.KeyEvent;

import static client.userInterface.panels.MainPanel.*;

/**
 * An abstract base class for single-player Tetris game modes.
 * <p>
 * This class specializes {@link TetrisPanel} for a single board configuration.
 * It handles the rendering of the Game Over screen specific to single-player
 * sessions and manages the connection between the input handler and the single player's board.
 * </p>
 */
public abstract class SinglePlayerTetrisPanel extends TetrisPanel {

    /**
     * Constructs a new SinglePlayerTetrisPanel.
     * Initializes the boards array with a capacity of 1.
     *
     * @param mainPanel The parent container.
     */
    public SinglePlayerTetrisPanel(MainPanel mainPanel) {
        super(mainPanel);
        boards = new Board[1];
    }

    /**
     * Renders the game components and overlays.
     * If the game is over, it draws a "GAME OVER" banner with instructions to return to the menu.
     *
     * @param g2 The {@link Graphics2D} context.
     */
    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);

        if (isGameOver()) {
            String gameOverMessage = "GAME OVER";
            int gameOverWidth = 10*CELL_SIZE;
            int endGameWidth = 14*CELL_SIZE;
            int bannerWidth = gameOverWidth + 4*FRAME_PADDING;
            int bannerHeight = 3*CELL_SIZE + 2*FRAME_PADDING;
            int bannerY = 8*CELL_SIZE;

            // Draw semi-transparent background for the banner
            g2.setColor(BACKGROUND_COLOR);
            g2.fillRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);
            g2.setColor(TEXT_COLOR);
            g2.drawRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);

            // Draw Game Over Title
            g2.setColor(TEXT_COLOR);
            g2.setFont(GAME_OVER_FONT);
            g2.drawString(gameOverMessage, (PANEL_WIDTH-gameOverWidth)/2, (int) (bannerY + 1.5*FRAME_PADDING));

            // Draw Instruction Text
            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(MEDIUM_MESSAGE_FONT);
            g2.drawString("Press '" + KeyEvent.getKeyText(keyInputHandler.getGoBackToMenuKey()) + "' to go back to the start menu", (PANEL_WIDTH-endGameWidth)/2, (int) (bannerY + 2.5*FRAME_PADDING));
        }
    }

    /**
     * Updates the single player board if it is still alive.
     */
    @Override
    protected void updateBoards() {
        if (boards[0].isAlive()) boards[0].update();
    }

    /**
     * Checks if the single player has lost.
     *
     * @return {@code true} if the board is no longer alive, {@code false} otherwise.
     */
    @Override
    protected boolean checkGameOver() {
        return (!boards[0].isAlive());
    }

    /**
     * Resets the game and links the input handler to the new Player 1 board.
     */
    @Override
    protected void resetGame() {
        super.resetGame();
        keyInputHandler.setPlayer1Board((BoardWithPhysics) boards[0]);
    }
}