package tetris.panels.twoPlayerPanels;

import main.MainPanel;
import tetris.boards.SenderBoardOutputHandler;
import tetris.boards.ReceiverBoard;
import tetris.boards.ReceiverBoardInputHandler;
import tetris.boards.nes.ReceiverNESBoard;
import tetris.boards.nes.SenderNESBoardWithPhysicsSender;
import tetris.keyMaps.KeyMapFactory;

import java.io.IOException;

import static main.MainPanel.*;

public class OnlineTwoPlayerNESPanel extends OnlineTwoPlayersPanel {
    public OnlineTwoPlayerNESPanel(MainPanel mainPanel) {
        super(mainPanel, KeyMapFactory.TETRIO_MODE);
    }

    @Override
    protected void initializeBoards() {
        try {
            boards[0] = new SenderNESBoardWithPhysicsSender(BOARD1_X, BOARD1_Y, seed, new SenderBoardOutputHandler(boardsSocket.getOutputStream()));
            boards[1] = new ReceiverNESBoard(BOARD2_X, BOARD2_Y, seed);
            receiverBoardInputHandler = new ReceiverBoardInputHandler(boardsSocket.getInputStream(), (ReceiverBoard) boards[1]);
            receiverBoardInputHandler.start();
        }
        catch (IOException ioe) { ioe.printStackTrace(); }
    }
}