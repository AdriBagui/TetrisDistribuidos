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
     * @return 1 if Player 1 wins, 2 if Player 2 wins, or 0 for a tie/no winner yet.
     */
    protected abstract int checkWinner();

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
            String winner;
            int winnerWidth = 17*CELL_SIZE;
            int gameOverWidth = 10*CELL_SIZE;
            int endGameWidth = 14*CELL_SIZE;
            int bannerWidth = winnerWidth + 2*FRAME_PADDING;
            int bannerHeight = 5*CELL_SIZE + 2*FRAME_PADDING;
            int bannerY = 7*CELL_SIZE;
            int winnerId = checkWinner();

            if (winnerId == 1) winner = "The winner is player 1!";
            else if (winnerId == 2) winner = "The winner is player 2!";
            else {
                winner = "It's a tie!";
                winnerWidth = 7*CELL_SIZE;
            }

            // Draw Background Banner
            g2.setColor(BACKGROUND_COLOR);
            g2.fillRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);
            g2.setColor(TEXT_COLOR);
            g2.drawRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);

            // Draw Texts
            g2.setColor(TEXT_COLOR);
            g2.setFont(GAME_OVER_FONT);
            g2.drawString(gameOverMessage, (PANEL_WIDTH-gameOverWidth)/2, (int) (bannerY + 1.5*FRAME_PADDING));
            g2.drawString(winner, (PANEL_WIDTH-winnerWidth)/2, (int) (bannerY + 2.5*FRAME_PADDING));

            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(MEDIUM_MESSAGE_FONT);
            g2.drawString("Press '" + KeyEvent.getKeyText(keyInputHandler.getGoBackToMenuKey()) + "' to go back to the start menu", (PANEL_WIDTH-endGameWidth)/2, (int) (bannerY + 3.5*FRAME_PADDING));
        }
    }
}