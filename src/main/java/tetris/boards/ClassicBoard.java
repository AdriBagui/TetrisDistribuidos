package tetris.boards;

import static tetris.Config.BOARD_COLUMNS;

public class ClassicBoard extends PhysicsCalculatingBoard {
    public ClassicBoard(int x, int y, long seed) {
        super(x, y, seed);
    }

    @Override
    protected void clearLines() {
        int totalClearedLines = this.totalClearedLines;
        super.clearLines();
        int clearedLines = this.totalClearedLines - totalClearedLines;

        switch (clearedLines) {
            case 2:
                enemyBoard.addGarbage(1, garbageRandom.nextInt(BOARD_COLUMNS));
                break;
            case 3:
                enemyBoard.addGarbage(2, garbageRandom.nextInt(BOARD_COLUMNS));
                break;
            case 4:
                enemyBoard.addGarbage(4, garbageRandom.nextInt(BOARD_COLUMNS));
                break;
        }
    }
}
