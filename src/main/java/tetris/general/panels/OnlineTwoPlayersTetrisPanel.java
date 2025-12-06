package tetris.general.panels;

import main.MainPanel;
import tetris.general.boards.BoardWithPhysics;
import tetris.general.boards.BoardsInputManager;
import tetris.tetrio.boards.TetrioBoardRepresentation;
import tetris.tetrio.boards.TetrioBoardWithPhysics;
import tetris.tetrio.panels.OnlineTwoPlayerTetrioPanel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;

import static tetris.Config.*;
import static tetris.Config.BOARD2_Y;

public abstract class OnlineTwoPlayersTetrisPanel extends TwoPlayersTetrisPanel {
    private Socket boardsSocket;
    private BoardsInputManager boardsInputManager;
    private long seed;

    public OnlineTwoPlayersTetrisPanel(MainPanel mainPanel) {
        super(mainPanel);

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
        if (boards[0].isAlive()) boards[0].update();

        if (!boards[0].isAlive() || !boards[1].isAlive()) {
            gameOver = true;
        }
    }

    @Override
    protected void initializeGame() {
        try {
            boards[0] = new TetrioBoardWithPhysics(BOARD1_X, BOARD1_Y, seed, boardsSocket.getOutputStream());
            boards[1] = new TetrioBoardRepresentation(BOARD2_X, BOARD2_Y, seed);
            boardsInputManager = new BoardsInputManager(boardsSocket.getInputStream(), (TetrioBoardWithPhysics) boards[0], (TetrioBoardRepresentation) boards[1]);
            boardsInputManager.start();
        }
        catch (IOException ioe) { ioe.printStackTrace(); }
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
