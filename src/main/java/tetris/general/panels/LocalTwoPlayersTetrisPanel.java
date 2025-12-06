package tetris.general.panels;

import main.MainPanel;
import tetris.general.boards.BoardWithPhysics;
import tetris.general.boards.BoardsInputManager;
import tetris.tetrio.boards.TetrioBoardWithPhysics;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static tetris.Config.*;
import static tetris.Config.BOARD2_Y;

public abstract class LocalTwoPlayersTetrisPanel extends TwoPlayersTetrisPanel {
    private Socket board1Socket;
    private Socket board2Socket;
    private BoardsInputManager board1InputManager;
    private BoardsInputManager board2InputManager;
    private long seed;

    public LocalTwoPlayersTetrisPanel(MainPanel mainPanel) {
        super(mainPanel);

        addKeyListener(new KeyInputHandler());
    }

    @Override
    public void update() {
        if (boards[0].isAlive()) boards[0].update();
        if (boards[1].isAlive()) boards[1].update();

        if (!boards[0].isAlive() || !boards[1].isAlive())
            gameOver = true;
    }

    protected void connectBoards() {
        seed = System.currentTimeMillis();

        Thread board1Thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (ServerSocket tempServerSocket = new ServerSocket(7776)) {
                    board1Socket = tempServerSocket.accept();
                }
                catch (IOException ioe) { ioe.printStackTrace(); }
            }
        });

        Thread board2Thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                    board2Socket = new Socket("localhost", 7776);
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
    protected void initializeGame() {
        try {
            connectBoards();
            boards[0] = new TetrioBoardWithPhysics(BOARD1_X, BOARD1_Y, seed, board1Socket.getOutputStream());
            board1InputManager = new BoardsInputManager(board1Socket.getInputStream(), (TetrioBoardWithPhysics) boards[0], null);
            board1InputManager.start();

            boards[1] = new TetrioBoardWithPhysics(BOARD2_X, BOARD2_Y, seed, board2Socket.getOutputStream());
            board2InputManager = new BoardsInputManager(board2Socket.getInputStream(), (TetrioBoardWithPhysics) boards[1], null);
            board2InputManager.start();
        }
        catch (IOException ioe) { ioe.printStackTrace(); }
    }

    private class KeyInputHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            BoardWithPhysics player1Board = (BoardWithPhysics) boards[0];
            BoardWithPhysics player2Board = (BoardWithPhysics) boards[1];

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
            BoardWithPhysics player1Board = (BoardWithPhysics) boards[0];
            BoardWithPhysics player2Board = (BoardWithPhysics) boards[1];

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
