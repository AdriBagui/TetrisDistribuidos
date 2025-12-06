package tetris.general.panels;

import main.MainPanel;
import tetris.general.boards.Board;
import tetris.general.boards.BoardWithPhysics;
import tetris.general.boards.BoardsInputManager;
import tetris.tetrio.boards.TetrioBoardWithPhysics;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import static tetris.Config.*;
import static tetris.Config.FRAME_PADDING;
import static tetris.Config.PANEL_WIDTH;

public abstract class OnePlayerTetrisPanel extends TetrisPanel {
    public OnePlayerTetrisPanel(MainPanel mainPanel) {
        super(mainPanel);
        boards = new Board[1];

        addKeyListener(new KeyInputHandler());
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
            int bannerWidth = gameOverWidth + 2*FRAME_PADDING;
            int bannerHeight = 3*CELL_SIZE + 2*FRAME_PADDING;
            int bannerY = 7*CELL_SIZE;

            g2.setColor(Color.DARK_GRAY);
            g2.fillRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);
            g2.setColor(Color.WHITE);
            g2.drawRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);

            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.drawString("Presiona 'Enter' para acabar la partida", (PANEL_WIDTH-endGameWidth)/2, (int) (bannerY + 2.5*FRAME_PADDING));
        }
    }

    private class KeyInputHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            BoardWithPhysics localBoard = (BoardWithPhysics) boards[0];

            if (gameOver) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    mainPanel.endGame();
                }
                return;
            }

            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> localBoard.moveLeftPressed();
                case KeyEvent.VK_D -> localBoard.moveRightPressed();
                case KeyEvent.VK_W -> localBoard.hardDropPressed();
                case KeyEvent.VK_S -> localBoard.softDropPressed();
                case KeyEvent.VK_J -> localBoard.rotateLeftPressed();
                case KeyEvent.VK_K -> localBoard.rotateRightPressed();
                case KeyEvent.VK_L -> localBoard.flipPressed();
                case KeyEvent.VK_SHIFT ->  localBoard.hold();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            BoardWithPhysics localBoard = (BoardWithPhysics) boards[0];

            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> localBoard.moveLeftReleased();
                case KeyEvent.VK_D -> localBoard.moveRightReleased();
                case KeyEvent.VK_W -> localBoard.hardDropReleased();
                case KeyEvent.VK_S -> localBoard.softDropReleased();
                case KeyEvent.VK_J -> localBoard.rotateLeftReleased();
                case KeyEvent.VK_K -> localBoard.rotateRightReleased();
                case KeyEvent.VK_L -> localBoard.flipReleased();
            }
        }
    }
}
