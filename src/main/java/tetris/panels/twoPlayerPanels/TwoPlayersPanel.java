package tetris.panels.twoPlayerPanels;

import main.MainPanel;
import tetris.boards.Board;
import tetris.panels.TetrisPanel;

import java.awt.*;
import java.awt.event.KeyListener;

import static main.MainPanel.*;

public abstract class TwoPlayersPanel extends TetrisPanel {
    public TwoPlayersPanel(MainPanel mainPanel, KeyListener keyListener) {
        super(mainPanel, keyListener);
        boards = new Board[2];
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);

        if (gameOver) {
            String gameOverMessage = "GAME OVER";
            String winner;
            int winnerWidth = 18*CELL_SIZE;
            int gameOverWidth = 10*CELL_SIZE;
            int endGameWidth = 13*CELL_SIZE;
            int bannerWidth = winnerWidth + 2*FRAME_PADDING;
            int bannerHeight = 5*CELL_SIZE + 2*FRAME_PADDING;
            int bannerY = 7*CELL_SIZE;

            if (boards[0].isAlive()) winner = "Ha ganado el jugador 1!";
            else if (boards[1].isAlive()) winner = "Ha ganado el jugador 2!";
            else {
                winner = "Empate!";
                winnerWidth = 6*CELL_SIZE;
            }

            g2.setColor(Color.DARK_GRAY);
            g2.fillRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);
            g2.setColor(Color.WHITE);
            g2.drawRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            g2.drawString(gameOverMessage, (PANEL_WIDTH-gameOverWidth)/2, (int) (bannerY + 1.5*FRAME_PADDING));
            g2.drawString(winner, (PANEL_WIDTH-winnerWidth)/2, (int) (bannerY + 2.5*FRAME_PADDING));

            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.drawString("Presiona 'Enter' para acabar la partida", (PANEL_WIDTH-endGameWidth)/2, (int) (bannerY + 3.5*FRAME_PADDING));
        }
    }
}
