package client.userInterface.panels.tetris.twoPlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.Board;
import client.userInterface.panels.tetris.TetrisPanel;

import java.awt.*;
import java.awt.event.KeyEvent;

import static client.userInterface.panels.MainPanel.*;

public abstract class TwoPlayersPanel extends TetrisPanel {
    public TwoPlayersPanel(MainPanel mainPanel) {
        super(mainPanel);
        boards = new Board[2];
    }

    public abstract void closeCommunications();
    public abstract void handleConnectionError();
    protected abstract int checkWinner();

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

            g2.setColor(BACKGROUND_COLOR);
            g2.fillRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);
            g2.setColor(TEXT_COLOR);
            g2.drawRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);

            g2.setColor(TEXT_COLOR);
            g2.setFont(GAME_OVER_FONT);
            g2.drawString(gameOverMessage, (PANEL_WIDTH-gameOverWidth)/2, (int) (bannerY + 1.5*FRAME_PADDING));
            g2.drawString(winner, (PANEL_WIDTH-winnerWidth)/2, (int) (bannerY + 2.5*FRAME_PADDING));

            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(GAME_OVER_PRESS_KEY_FONT);
            g2.drawString("Press '" + KeyEvent.getKeyText(keyInputHandler.getGoBackToMenuKey()) + "' to go back to the start menu", (PANEL_WIDTH-endGameWidth)/2, (int) (bannerY + 3.5*FRAME_PADDING));
        }
    }
}
