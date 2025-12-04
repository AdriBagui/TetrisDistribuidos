package tetris.boards;

import java.awt.*;

import static tetris.Config.*;
import static tetris.Config.BOARD_COLUMNS;
import static tetris.Config.BOARD_ROWS;
import static tetris.Config.BOARD_SPAWN_ROWS;

public class BoardGrid {
    // GRID POSITION IN PANEL
    private int x, y;
    // GRID REPRESENTATION
    protected boolean[][] grid; // 20 rows, 10 cols
    protected Color[][] gridColor;

    public BoardGrid(int x, int y) {
        this.x = x;
        this.y = y;

        grid = new boolean[BOARD_ROWS + BOARD_SPAWN_ROWS][BOARD_COLUMNS];
        gridColor = new Color[BOARD_ROWS + BOARD_SPAWN_ROWS][BOARD_COLUMNS];
    }
}
