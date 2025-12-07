package tetris.panels.twoPlayerPanels;

import main.MainPanel;
import tetris.boards.BoardOutputHandler;
import tetris.boards.ReceiverBoardInputHandler;
import tetris.boards.tetrio.ReceiverTetrioBoardInputHandler;
import tetris.boards.tetrio.TetrioBoardWithPhysicsSender;
import tetris.keyMaps.KeyMapFactory;

import java.io.IOException;

import static tetris.Config.*;
import static tetris.Config.BOARD2_Y;

public class LocalTwoPlayerTetrioPanel extends LocalTwoPlayersPanel {
    private ReceiverBoardInputHandler board1InputManager;
    private ReceiverBoardInputHandler board2InputManager;

    public LocalTwoPlayerTetrioPanel(MainPanel mainPanel) {
        super(mainPanel, KeyMapFactory.TETRIO_MODE);

        board1InputManager = null;
        board2InputManager = null;
    }

    @Override
    protected void initializeBoards() {
        try {
            connectBoards();
            boards[0] = new TetrioBoardWithPhysicsSender(BOARD1_X, BOARD1_Y, seed, new BoardOutputHandler(board1Socket.getOutputStream()));
            board1InputManager = new ReceiverTetrioBoardInputHandler(board1Socket.getInputStream(), (TetrioBoardWithPhysicsSender) boards[0], null);
            board1InputManager.start();

            boards[1] = new TetrioBoardWithPhysicsSender(BOARD2_X, BOARD2_Y, seed, new BoardOutputHandler(board2Socket.getOutputStream()));
            board2InputManager = new ReceiverTetrioBoardInputHandler(board2Socket.getInputStream(), (TetrioBoardWithPhysicsSender) boards[1], null);
            board2InputManager.start();
        }
        catch (IOException ioe) { ioe.printStackTrace(); }
    }
}
