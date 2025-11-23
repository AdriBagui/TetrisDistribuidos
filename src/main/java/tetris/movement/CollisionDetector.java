package tetris.movement;
import tetris.Board;
import tetris.tetrominoes.Tetromino;

public class CollisionDetector {
    private boolean[][] grid;

    public CollisionDetector(boolean[][] grid) {
        this.grid = grid;
    }

    public boolean checkCollision(Tetromino t) {
        boolean[][] shape = t.getShape();
        int boardX;
        int boardY;

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c]) {
                    boardX = t.getX() + c;
                    boardY = t.getY() + r;

                    // Check boundaries
                    if (boardX < 0 || boardX >= Board.COLUMNS || boardY >= Board.ROWS + Board.SPAWN_ROWS)
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
