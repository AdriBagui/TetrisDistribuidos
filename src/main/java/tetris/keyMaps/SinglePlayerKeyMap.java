package tetris.keyMaps;

import main.MainPanel;
import tetris.boards.BoardWithPhysics;

import java.awt.event.KeyAdapter;

public class SinglePlayerKeyMap extends KeyAdapter {
    protected MainPanel mainPanel;
    protected BoardWithPhysics board;

    public SinglePlayerKeyMap(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void setBoard(BoardWithPhysics board) { this.board = board; }
}
