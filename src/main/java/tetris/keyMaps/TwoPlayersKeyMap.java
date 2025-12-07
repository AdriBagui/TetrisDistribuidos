package tetris.keyMaps;

import main.MainPanel;
import tetris.boards.BoardWithPhysics;

import java.awt.event.KeyAdapter;

public class TwoPlayersKeyMap extends KeyAdapter {
    protected MainPanel mainPanel;
    protected BoardWithPhysics player1Board;
    protected BoardWithPhysics player2Board;

    public TwoPlayersKeyMap(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void setPlayer1Board(BoardWithPhysics player1Board) { this.player1Board = player1Board; }
    public void setPlayer2Board(BoardWithPhysics player2Board) { this.player2Board = player2Board; }
    public void setBoards(BoardWithPhysics player1Board, BoardWithPhysics player2Board) {
        setPlayer1Board(player1Board);
        setPlayer2Board(player2Board);
    }
}
