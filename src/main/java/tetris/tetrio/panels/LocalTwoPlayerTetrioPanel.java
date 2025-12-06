package tetris.tetrio.panels;

import main.MainPanel;
import tetris.general.boards.BoardGrid;
import tetris.general.boards.BoardsInputManager;
import tetris.general.panels.LocalTwoPlayersTetrisPanel;
import tetris.tetrio.boards.TetrioBoardWithPhysics;
import tetris.general.boards.BoardWithPhysics;

import java.awt.event.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static tetris.Config.*;

public class LocalTwoPlayerTetrioPanel extends LocalTwoPlayersTetrisPanel {
    public LocalTwoPlayerTetrioPanel(MainPanel mainPanel) {
        super(mainPanel);
    }
}
