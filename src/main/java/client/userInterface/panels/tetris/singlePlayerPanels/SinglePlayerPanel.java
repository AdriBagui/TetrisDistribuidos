package client.userInterface.panels.tetris.singlePlayerPanels;

import client.userInterface.panels.MainPanel;
import tetris.boards.Board;
import tetris.boards.BoardWithPhysics;
import client.userInterface.panels.tetris.TetrisPanel;

import java.awt.*;
import java.awt.event.KeyEvent;

import static client.userInterface.panels.MainPanel.*;

public abstract class SinglePlayerPanel extends TetrisPanel {
    public SinglePlayerPanel(MainPanel mainPanel) {
        super(mainPanel);
        boards = new Board[1];
    }

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

            g2.setColor(BACKGROUND_COLOR);
            g2.fillRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);
            g2.setColor(TEXT_COLOR);
            g2.drawRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);

            g2.setColor(TEXT_COLOR);
            g2.setFont(GAME_OVER_FONT);
            g2.drawString("GAME OVER", (PANEL_WIDTH-gameOverWidth)/2, (int) (bannerY + 1.5*FRAME_PADDING));

            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(GAME_OVER_PRESS_KEY_FONT);
            g2.drawString("Press '" + KeyEvent.getKeyText(keyInputHandler.getGoBackToMenuKey()) + "' to go back to the start menu", (PANEL_WIDTH-endGameWidth)/2, (int) (bannerY + 2.5*FRAME_PADDING));
        }
    }

    @Override
    protected void updateBoards() {
        if (boards[0].isAlive()) boards[0].update();
    }

    @Override
    protected boolean checkGameOver() {
        return (!boards[0].isAlive());
    }

    @Override
    protected void resetGame() {
        super.resetGame();
        keyInputHandler.setPlayer1Board((BoardWithPhysics) boards[0]);
    }
}
