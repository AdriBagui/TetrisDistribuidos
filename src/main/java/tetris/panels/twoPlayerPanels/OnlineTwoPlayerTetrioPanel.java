package tetris.panels.twoPlayerPanels;

import main.MainPanel;
import tetris.boards.SenderBoardOutputHandler;
import tetris.boards.tetrio.ReceiverTetrioBoard;
import tetris.boards.tetrio.ReceiverTetrioBoardInputHandler;
import tetris.boards.tetrio.SenderTetrioBoardWithPhysics;
import tetris.keyMaps.KeyMapFactory;

import java.io.IOException;

import static main.MainPanel.*;

public class OnlineTwoPlayerTetrioPanel extends OnlineTwoPlayersPanel {
    public OnlineTwoPlayerTetrioPanel(MainPanel mainPanel) {
        super(mainPanel, KeyMapFactory.TETRIO_MODE);
    }

    @Override
    protected void initializeBoards() {
        try {
            boards[0] = new SenderTetrioBoardWithPhysics(BOARD1_X, BOARD1_Y, seed, new SenderBoardOutputHandler(boardsSocket.getOutputStream()));
            boards[1] = new ReceiverTetrioBoard(BOARD2_X, BOARD2_Y, seed);
            receiverBoardInputHandler = new ReceiverTetrioBoardInputHandler(boardsSocket.getInputStream(), (SenderTetrioBoardWithPhysics) boards[0], (ReceiverTetrioBoard) boards[1]);
            receiverBoardInputHandler.start();
        }
        catch (IOException ioe) { ioe.printStackTrace(); }
    }
}