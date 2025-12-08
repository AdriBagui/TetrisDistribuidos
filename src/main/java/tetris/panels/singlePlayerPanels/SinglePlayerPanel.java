package tetris.panels.singlePlayerPanels;

import main.MainPanel;
import tetris.boards.Board;
import tetris.boards.BoardWithPhysics;
import tetris.keyMaps.KeyInputHandler;
import tetris.panels.TetrisPanel;

import java.awt.*;
import java.awt.event.KeyEvent;

import static main.MainPanel.*;

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

            g2.setColor(new Color(22, 22, 22));
            g2.fillRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);
            g2.setColor(new Color(220, 220, 220));
            g2.drawRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);

            g2.setColor(new Color(220, 220, 220));
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            g2.drawString(gameOverMessage, (PANEL_WIDTH-gameOverWidth)/2, (int) (bannerY + 1.5*FRAME_PADDING));

            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
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
