package tetris.tetrio.panels;

import distributedServices.ServerConnector;
import main.MainPanel;
import tetris.general.boards.BoardGrid;
import tetris.general.boards.BoardsInputManager;
import tetris.general.boards.BoardWithPhysics;
import tetris.general.panels.OnlineTwoPlayersTetrisPanel;
import tetris.general.panels.TwoPlayersTetrisPanel;
import tetris.tetrio.boards.TetrioBoardRepresentation;
import tetris.tetrio.boards.TetrioBoardWithPhysics;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import static tetris.Config.*;
import static tetris.Config.BOARD2_Y;

public class OnlineTwoPlayerTetrioPanel extends OnlineTwoPlayersTetrisPanel {
    public OnlineTwoPlayerTetrioPanel(MainPanel mainPanel) {
        super(mainPanel);
    }
}