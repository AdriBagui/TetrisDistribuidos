package tetris.panels.twoPlayerPanels;

import main.MainPanel;
import tetris.boards.BoardOutputHandler;
import tetris.boards.ReceiverBoardInputHandler;
import tetris.boards.tetrio.ReceiverTetrioBoardInputHandler;
import tetris.boards.tetrio.TetrioBoardWithPhysicsSender;
import tetris.keyMaps.KeyMapFactory;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static tetris.Config.*;
import static tetris.Config.BOARD2_Y;

public class LocalTwoPlayerTetrioPanel extends LocalTwoPlayersPanel {
    private PipedOutputStream player1OutputStream;
    private PipedOutputStream player2OutputStream;
    private PipedInputStream player1InputStream;
    private PipedInputStream player2InputStream;
    private ReceiverBoardInputHandler player1ReceiverBoardInputHandler;
    private ReceiverBoardInputHandler player2ReceiverBoardInputHandler;
    private long seed;

    public LocalTwoPlayerTetrioPanel(MainPanel mainPanel) {
        super(mainPanel, KeyMapFactory.TETRIO_MODE);

        player1OutputStream = null;
        player2OutputStream = null;
        player1InputStream = null;
        player2InputStream = null;
        player1ReceiverBoardInputHandler = null;
        player2ReceiverBoardInputHandler = null;
    }

    @Override
    protected void initializeBoards() {
        connectBoards();
        boards[0] = new TetrioBoardWithPhysicsSender(BOARD1_X, BOARD1_Y, seed, new BoardOutputHandler(player1OutputStream));
        player1ReceiverBoardInputHandler = new ReceiverTetrioBoardInputHandler(player1InputStream, (TetrioBoardWithPhysicsSender) boards[0], null);
        player1ReceiverBoardInputHandler.start();

        boards[1] = new TetrioBoardWithPhysicsSender(BOARD2_X, BOARD2_Y, seed, new BoardOutputHandler(player2OutputStream));
        player2ReceiverBoardInputHandler = new ReceiverTetrioBoardInputHandler(player2InputStream, (TetrioBoardWithPhysicsSender) boards[1], null);
        player2ReceiverBoardInputHandler.start();
    }

    @Override
    public void update() {
        super.update();

        if (gameOver) {
            player1ReceiverBoardInputHandler.interrupt();
            player2ReceiverBoardInputHandler.interrupt();
        }
    }

    protected void connectBoards() {
        seed = System.currentTimeMillis();

        try {
            player1OutputStream = new PipedOutputStream();
            player2InputStream = new PipedInputStream(player1OutputStream);
            player2OutputStream = new PipedOutputStream();
            player1InputStream = new PipedInputStream(player2OutputStream);
        }
        catch (IOException ioe) { ioe.printStackTrace(); }
    }
}
