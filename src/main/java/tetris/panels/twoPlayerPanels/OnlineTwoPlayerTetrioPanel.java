package tetris.panels.twoPlayerPanels;

import main.MainPanel;
import tetris.boards.BoardOutputHandler;
import tetris.boards.tetrio.ReceiverTetrioBoard;
import tetris.boards.tetrio.ReceiverTetrioBoardInputHandler;
import tetris.boards.tetrio.TetrioBoardWithPhysicsSender;
import tetris.keyMaps.KeyMapFactory;

import java.io.IOException;

import static tetris.Config.*;
import static tetris.Config.BOARD2_Y;

public class OnlineTwoPlayerTetrioPanel extends OnlineTwoPlayersPanel {
    public OnlineTwoPlayerTetrioPanel(MainPanel mainPanel) {
        super(mainPanel, KeyMapFactory.TETRIO_MODE);
    }

    @Override
    protected void initializeBoards() {
        try {
            boards[0] = new TetrioBoardWithPhysicsSender(BOARD1_X, BOARD1_Y, seed, new BoardOutputHandler(boardsSocket.getOutputStream()));
            boards[1] = new ReceiverTetrioBoard(BOARD2_X, BOARD2_Y, seed);
            receiverBoardInputHandler = new ReceiverTetrioBoardInputHandler(boardsSocket.getInputStream(), (TetrioBoardWithPhysicsSender) boards[0], (ReceiverTetrioBoard) boards[1]);
            receiverBoardInputHandler.start();
        }
        catch (IOException ioe) { ioe.printStackTrace(); }
    }
}