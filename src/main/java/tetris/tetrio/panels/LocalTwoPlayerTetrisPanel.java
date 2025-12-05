package tetris.tetrio.panels;

import main.MainPanel;
import tetris.general.boards.BoardGrid;
import tetris.general.boards.BoardInputManager;
import tetris.tetrio.boards.TetrioBoardWithPhysics;
import tetris.general.boards.BoardWithPhysics;

import java.awt.event.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static tetris.Config.*;

public class LocalTwoPlayerTetrisPanel extends TwoPlayerTetrisPanel {
    private Socket board1Socket;
    private Socket board2Socket;
    private BoardInputManager board1InputManager;
    private BoardInputManager board2InputManager;

    public LocalTwoPlayerTetrisPanel(MainPanel mainPanel) {
        super(mainPanel);
    }

    @Override
    protected KeyAdapter initializeKeyListener() { return new KeyInputHandler(); }

    @Override
    protected void initializeGame() {
        gameOver = false;
        long seed = System.currentTimeMillis();

        Thread board1Thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (ServerSocket tempServerSocket = new ServerSocket(7777)) {
                    board1Socket = tempServerSocket.accept();
                    board1 = new TetrioBoardWithPhysics(BOARD1_X, BOARD1_Y, new BoardGrid(BOARD1_X + TETROMINO_HOLDER_WIDTH, BOARD1_Y, BOARD_ROWS, BOARD_SPAWN_ROWS, BOARD_COLUMNS), seed, board1Socket.getOutputStream());
                    board1InputManager = new BoardInputManager(board1Socket.getInputStream(), (TetrioBoardWithPhysics) board1, null);
                }
                catch (IOException ioe) { ioe.printStackTrace(); }
            }
        });

        Thread board2Thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                    board2Socket = new Socket("localhost", 7777);
                    board2 = new TetrioBoardWithPhysics(BOARD2_X, BOARD2_Y, new BoardGrid(BOARD2_X + TETROMINO_HOLDER_WIDTH, BOARD2_Y, BOARD_ROWS, BOARD_SPAWN_ROWS, BOARD_COLUMNS), seed, board2Socket.getOutputStream());
                    board2InputManager = new BoardInputManager(board2Socket.getInputStream(), (TetrioBoardWithPhysics) board2, null);
                }
                catch (IOException ioe) { ioe.printStackTrace(); }
                catch (InterruptedException ie) { ie.printStackTrace(); }
            }
        });

        board1Thread.start();
        board2Thread.start();

        try {
            board1Thread.join();
            board2Thread.join();
        }
        catch (InterruptedException ie) { ie.printStackTrace(); }


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
            BoardWithPhysics player1Board = (BoardWithPhysics) board1;
            BoardWithPhysics player2Board = (BoardWithPhysics) board2;

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
            BoardWithPhysics player1Board = (BoardWithPhysics) board1;
            BoardWithPhysics player2Board = (BoardWithPhysics) board2;

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
