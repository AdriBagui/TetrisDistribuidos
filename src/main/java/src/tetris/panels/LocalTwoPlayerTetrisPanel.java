package src.tetris.panels;

import src.MainPanel;
import src.tetris.boards.ClassicBoard;
import java.awt.event.*;

import static src.tetris.Config.*;

public class LocalTwoPlayerTetrisPanel extends TwoPlayerTetrisPanel {
    public LocalTwoPlayerTetrisPanel(MainPanel mainPanel) {
        super(mainPanel);
    }

    @Override
    protected KeyAdapter initializeKeyListener() { return new KeyInputHandler(); }

    @Override
    protected void initializeGame() {
        gameOver = false;

        long seed = System.currentTimeMillis();
        board1 = new ClassicBoard(BOARD1_X, BOARD1_Y, seed);
        board2 = new ClassicBoard(BOARD2_X, BOARD2_Y, seed);

        board1.setEnemyBoard(board2);
        board2.setEnemyBoard(board1);
    }

    @Override
    public void update() {
        if (board1.isAlive()) board1.update();
        if (board2.isAlive()) board2.update();

        if (!board1.isAlive() || !board2.isAlive()) {
            gameOver = true;
        }
    }

    private class KeyInputHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            ClassicBoard player1Board = (ClassicBoard) board1;
            ClassicBoard player2Board = (ClassicBoard) board2;

            if (gameOver) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    mainPanel.endGame();
                }
                return;
            }

            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> player1Board.moveLeftPressed();
                case KeyEvent.VK_D -> player1Board.moveRightPressed();
                case KeyEvent.VK_W -> player1Board.hardDropPressed();
                case KeyEvent.VK_S -> player1Board.softDropPressed();
                case KeyEvent.VK_J -> player1Board.rotateLeftPressed();
                case KeyEvent.VK_K -> player1Board.rotateRightPressed();
                case KeyEvent.VK_L -> player1Board.flipPressed();
                case KeyEvent.VK_SHIFT ->  player1Board.hold();
                case KeyEvent.VK_LEFT -> player2Board.moveLeftPressed();
                case KeyEvent.VK_RIGHT -> player2Board.moveRightPressed();
                case KeyEvent.VK_UP -> player2Board.hardDropPressed();
                case KeyEvent.VK_DOWN -> player2Board.softDropPressed();
                case KeyEvent.VK_NUMPAD1 -> player2Board.rotateLeftPressed();
                case KeyEvent.VK_NUMPAD2 -> player2Board.rotateRightPressed();
                case KeyEvent.VK_CONTROL -> player2Board.hold();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            ClassicBoard player1Board = (ClassicBoard) board1;
            ClassicBoard player2Board = (ClassicBoard) board2;

            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> player1Board.moveLeftReleased();
                case KeyEvent.VK_D -> player1Board.moveRightReleased();
                case KeyEvent.VK_W -> player1Board.hardDropReleased();
                case KeyEvent.VK_S -> player1Board.softDropReleased();
                case KeyEvent.VK_J -> player1Board.rotateLeftReleased();
                case KeyEvent.VK_K -> player1Board.rotateRightReleased();
                case KeyEvent.VK_L -> player1Board.flipReleased();
                case KeyEvent.VK_LEFT -> player2Board.moveLeftReleased();
                case KeyEvent.VK_RIGHT -> player2Board.moveRightReleased();
                case KeyEvent.VK_UP -> player2Board.hardDropReleased();
                case KeyEvent.VK_DOWN -> player2Board.softDropReleased();
                case KeyEvent.VK_NUMPAD1 -> player2Board.rotateLeftReleased();
                case KeyEvent.VK_NUMPAD2 -> player2Board.rotateRightReleased();
            }
        }
    }
}
