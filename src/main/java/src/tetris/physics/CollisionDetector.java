package src.tetris.physics;
import src.tetris.tetrominoes.Tetromino;

import static src.tetris.Config.*;

public class CollisionDetector {
    public static boolean checkCollision(boolean[][] grid, Tetromino t) {
        boolean[][] shape = t.getShape();
        int boardX;
        int boardY;

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c]) {
                    boardX = t.getX() + c;
                    boardY = t.getY() + r;

                    // Check boundaries
                    if (boardX < 0 || boardX >= BOARD_COLUMNS || boardY >= BOARD_ROWS + BOARD_SPAWN_ROWS)
                        return true;

                    // Check grid occupied (ignore above board)
                    if (boardY >= 0 && grid[boardY][boardX])
                        return true;
                }
            }
        }
        return false;
    }
}
