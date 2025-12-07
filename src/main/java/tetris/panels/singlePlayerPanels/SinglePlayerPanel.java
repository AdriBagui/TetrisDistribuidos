package tetris.panels.singlePlayerPanels;

import main.MainPanel;
import tetris.boards.Board;
import tetris.boards.BoardWithPhysics;
import tetris.keyMaps.KeyMapFactory;
import tetris.keyMaps.SinglePlayerKeyMap;
import tetris.panels.TetrisPanel;

import java.awt.*;

import static main.MainPanel.*;

public abstract class SinglePlayerPanel extends TetrisPanel {
    public SinglePlayerPanel(MainPanel mainPanel, int tetrisMode) {
        super(mainPanel, KeyMapFactory.createSinglePlayerKeyMap(tetrisMode, mainPanel));
        boards = new Board[1];
    }

    @Override
    public void update() {
        if (boards[0].isAlive()) boards[0].update();
        else gameOver = true;
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);

        if (gameOver) {
            String gameOverMessage = "GAME OVER";
            int gameOverWidth = 10*CELL_SIZE;
            int endGameWidth = 13*CELL_SIZE;
            int bannerWidth = gameOverWidth + 4*FRAME_PADDING;
            int bannerHeight = 3*CELL_SIZE + 2*FRAME_PADDING;
            int bannerY = 8*CELL_SIZE;

            g2.setColor(Color.DARK_GRAY);
            g2.fillRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);
            g2.setColor(Color.WHITE);
            g2.drawRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            g2.drawString(gameOverMessage, (PANEL_WIDTH-gameOverWidth)/2, (int) (bannerY + 1.5*FRAME_PADDING));

            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.drawString("Presiona 'Enter' para acabar la partida", (PANEL_WIDTH-endGameWidth)/2, (int) (bannerY + 2.5*FRAME_PADDING));
        }
    }

    @Override
    protected void resetGame() {
        super.resetGame();
        ((SinglePlayerKeyMap) keyMap).setBoard((BoardWithPhysics) boards[0]);
    }
}
