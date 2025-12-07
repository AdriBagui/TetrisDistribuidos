package tetris.keyMaps;

import main.MainPanel;
import tetris.boards.Board;
import tetris.boards.BoardWithPhysics;

import java.awt.event.KeyAdapter;

public class TwoPlayersKeyMap extends KeyAdapter {
    protected MainPanel mainPanel;
    protected Board player1Board;
    protected Board player2Board;

    public TwoPlayersKeyMap(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void setPlayer1Board(Board player1Board) { this.player1Board = player1Board; }
    public void setPlayer2Board(Board player2Board) { this.player2Board = player2Board; }
    public void setBoards(Board player1Board, Board player2Board) {
        setPlayer1Board(player1Board);
        setPlayer2Board(player2Board);
    }
}
