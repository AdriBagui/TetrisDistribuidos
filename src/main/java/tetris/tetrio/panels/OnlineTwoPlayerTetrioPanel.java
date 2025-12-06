package tetris.tetrio.panels;

import distributedServices.ServerConnector;
import main.MainPanel;
import tetris.general.boards.BoardGrid;
import tetris.general.boards.BoardsInputManager;
import tetris.general.boards.BoardWithPhysics;
import tetris.tetrio.boards.TetrioBoardRepresentation;
import tetris.tetrio.boards.TetrioBoardWithPhysics;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;

import static tetris.Config.*;
import static tetris.Config.BOARD2_Y;

public class OnlineTwoPlayerTetrioPanel extends TwoPlayerTetrioPanel {
    private ServerConnector serverConnector;
    private Socket boardsSocket;
    private BoardsInputManager boardsInputManager;
    private long seed;

    public OnlineTwoPlayerTetrioPanel(MainPanel mainPanel) {
        super(mainPanel);

        serverConnector = null;
        boardsSocket = null;
        boardsInputManager = null;
        seed = -1;

        addKeyListener(new KeyInputHandler());
    }

    public void setSocket(Socket socket) {
        this.boardsSocket = socket;
    }
    public void setSeed(long seed) { this.seed = seed;}

    @Override
    public void update() {
        if (board1.isAlive()) board1.update();

        if (!board1.isAlive() || !board2.isAlive()) {
            gameOver = true;
        }
    }

    @Override
    protected void connectPlayers() {
        serverConnector = new ServerConnector();
        boardsSocket = serverConnector.getSocket();
    }

    @Override
    protected void initializeGame() {
        try {
            board1 = new TetrioBoardWithPhysics(BOARD1_X, BOARD1_Y, new BoardGrid(BOARD1_X + TETROMINO_HOLDER_WIDTH, BOARD1_Y, BOARD_ROWS, BOARD_SPAWN_ROWS, BOARD_COLUMNS), seed, boardsSocket.getOutputStream());
            board2 = new TetrioBoardRepresentation(BOARD2_X, BOARD2_Y, new BoardGrid(BOARD2_X + TETROMINO_HOLDER_WIDTH, BOARD2_Y, BOARD_ROWS, BOARD_SPAWN_ROWS, BOARD_COLUMNS), seed);
            boardsInputManager = new BoardsInputManager(boardsSocket.getInputStream(), (TetrioBoardWithPhysics) board1, (TetrioBoardRepresentation) board2);
            boardsInputManager.start();
        }
        catch (IOException ioe) { ioe.printStackTrace(); }

        requestFocusInWindow();
    }

    private class KeyInputHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            BoardWithPhysics localBoard = (BoardWithPhysics) board1;

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
            BoardWithPhysics localBoard = (BoardWithPhysics) board1;

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