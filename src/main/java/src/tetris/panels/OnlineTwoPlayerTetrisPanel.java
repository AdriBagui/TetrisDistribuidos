package src.tetris.panels;

import src.MainPanel;
import src.tetris.boards.ClassicBoard;
import src.tetris.boards.LocalBoard;
import src.tetris.boards.OnlineBoard;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.Socket;

import static src.tetris.Config.*;
import static src.tetris.Config.BOARD2_Y;

public class OnlineTwoPlayerTetrisPanel extends TwoPlayerTetrisPanel {
    private Socket socket;
    private RivalInput rivalInput;
    private long seed;

    public OnlineTwoPlayerTetrisPanel(MainPanel mainPanel) {
        super(mainPanel);

        rivalInput = new RivalInput();
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    public void setSeed(long seed){this.seed = seed;}

    @Override
    public void startGame() {
        super.startGame();
        rivalInput.start();
    }

    @Override
    protected KeyAdapter initializeKeyListener() { return new KeyInputHandler(); }

    @Override
    protected void initializeGame() {
        gameOver = false;

        // TODO: Obtener la seed para generación de tetrominoes de alguna manera
        // TODO: Probeblemte tmb haya que pasar un output stream al local board y un input stream al online board
        long seed = 0;

        board1 = new LocalBoard(BOARD1_X, BOARD1_Y, seed);
        board2 = new OnlineBoard(BOARD2_X, BOARD2_Y, seed);

        board1.setEnemyBoard(board2);
        board2.setEnemyBoard(board1);
    }

    @Override
    public void update() {
        if (board1.isAlive()) board1.update();

        if (!board1.isAlive() || !board2.isAlive()) {
            gameOver = true;
        }
    }

    private class RivalInput extends Thread {
        @Override
        public void run() {
            while (board2.isAlive()) board2.update();

            // TODO: Dormir hasta que se vuelva a empezar una partida
        }
    }


    private class KeyInputHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            ClassicBoard localBoard = (ClassicBoard) board1;

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
            ClassicBoard localBoard = (ClassicBoard) board1;

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