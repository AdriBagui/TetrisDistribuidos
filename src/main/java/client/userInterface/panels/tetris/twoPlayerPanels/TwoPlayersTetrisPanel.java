package client.userInterface.panels.tetris.twoPlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.Board;
import client.userInterface.panels.tetris.TetrisPanel;

import java.awt.*;
import java.awt.event.KeyEvent;

import static client.userInterface.panels.MainPanel.*;

/**
 * An abstract base class for all two-player Tetris game modes.
 * <p>
 * This class extends {@link TetrisPanel} to support two simultaneous boards.
 * It handles the rendering of the winner declaration overlay and defines the
 * contract for checking victory conditions and managing communications between
 * the two players (whether local or online).
 * </p>
 */
public abstract class TwoPlayersTetrisPanel extends TetrisPanel {

    /**
     * Constructs a TwoPlayersTetrisPanel.
     * Initializes the boards array to hold two instances.
     *
     * @param mainPanel The parent container.
     */
    public TwoPlayersTetrisPanel(MainPanel mainPanel) {
        super(mainPanel);
        boards = new Board[2];
    }

    /**
     * Closes any active communication channels used by the game.
     * Use this to safely shut down sockets or piped streams when the game ends.
     */
    public abstract void closeCommunications();

    /**
     * Handles errors occurring during data transmission between boards.
     * This is typically invoked when a stream failure or socket disconnection occurs.
     */
    public abstract void handleConnectionError();

    /**
     * Determines the winner of the match based on the current game state.
     *
     * @return the string to with the winner message to be shown.
     */
    protected abstract String checkWinner();

    /**
     * Renders the game boards and the "Game Over / Winner" overlay.
     *
     * @param g2 The {@link Graphics2D} context.
     */
    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);

        if (isGameOver()) {
            String gameOverMessage = "GAME OVER";
            int gameOverMessageWidth = 9*CELL_SIZE;
            String winnerMessage;
            int winnerMessageWidth;
            String backToMenuMessage = "Press '" + KeyEvent.getKeyText(keyInputHandler.getGoBackToMenuKey()) + "' to go back to the start menu";
            int backToMenuMessageWith = 14*CELL_SIZE;

            int bannerWidth;
            int bannerHeight = 5*CELL_SIZE + 2*FRAME_PADDING;
            int bannerX;
            int bannerY = 7*CELL_SIZE;


            winnerMessage = checkWinner();

            if (winnerMessage.equals("Victory") || winnerMessage.equals("Defeat")) {
                winnerMessageWidth = 5 * CELL_SIZE;
            } else if (winnerMessage.equals("It's a tie!")) {
                winnerMessageWidth = 7 * CELL_SIZE;
            } else if (winnerMessage.equals("Connection to opponent lost")) {
                winnerMessageWidth = (int) 21.5 * CELL_SIZE;
            } else {
                winnerMessageWidth = 13*CELL_SIZE;
            }

            bannerWidth = Math.max(backToMenuMessageWith, winnerMessageWidth) + 2*FRAME_PADDING;
            bannerX = (PANEL_WIDTH-bannerWidth)/2;

            // Draw Background Banner
            g2.setColor(BACKGROUND_COLOR);
            g2.fillRect(bannerX, bannerY, bannerWidth, bannerHeight);
            g2.setColor(TEXT_COLOR);
            g2.drawRect(bannerX, bannerY, bannerWidth, bannerHeight);

            // Draw Texts
            g2.setColor(TEXT_COLOR);
            g2.setFont(GAME_OVER_FONT);
            g2.drawString(gameOverMessage, (PANEL_WIDTH-gameOverMessageWidth)/2, (int) (bannerY + 1.5*FRAME_PADDING));
            g2.drawString(winnerMessage, (PANEL_WIDTH-winnerMessageWidth)/2, (int) (bannerY + 2.5*FRAME_PADDING));

            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(MEDIUM_MESSAGE_FONT);
            g2.drawString(backToMenuMessage, (PANEL_WIDTH-backToMenuMessageWith)/2, (int) (bannerY + 3.5*FRAME_PADDING));
        }
    }
}