package tetris.tetrio.boards.physics.rotationSystems;

import tetris.general.boards.BoardGrid;
import tetris.general.boards.physics.rotationSystems.SuperRotationSystem;
import tetris.general.tetrominoes.Tetromino;

import static tetris.Config.*;

public class SuperRotationSystemPlus extends SuperRotationSystem {
    // I Tetromino Wall Kick Data (Updated for TETR.IO SRS+)
    private static final int[][][] I_ROTATE_RIGHT_KICK_DATA = {
            // 0 -> R (Spawn -> Right)
            {{0, 0}, {1, 0}, {-2, 0}, {-2, -1}, {1, 2}},
            // R -> 2 (Right -> 180)
            {{0, 0}, {-1, 0}, {2, 0}, {-1, 2}, {2, -1}},
            // 2 -> L (180 -> Left)
            {{0, 0}, {2, 0}, {-1, 0}, {2, 1}, {-1, -2}},
            // L -> 0 (Left -> Spawn)
            {{0, 0}, {1, 0}, {-2, 0}, {1, -2}, {-2, 1}}
    };
    private static final int[][][] I_ROTATE_LEFT_KICK_DATA = {
            // 0 -> L (Spawn -> Left)
            {{0, 0}, {-1, 0}, {2, 0}, {2, -1}, {-1, 2}},
            // R -> 0 (Right -> 0)
            {{0, 0}, {-1, 0}, {2, 0}, {-1, -2}, {2, 1}},
            // 2 -> R (180 -> Right)
            {{0, 0}, {-2, 0}, {1, 0}, {-2, 1}, {1, -2}},
            // L -> 2 (Left -> 180)
            {{0, 0}, {1, 0}, {-2, 0}, {1, 2}, {-2, -1}}
    };

    public SuperRotationSystemPlus(BoardGrid grid) {
        super(grid);
    }

    @Override
    public void rotateRight(Tetromino tetromino) {
        boolean success;
        Tetromino aux;

        if (tetromino.getType() != I) {
            super.rotateRight(tetromino);
        } else  {
            aux = tetromino.createCopy();
            aux.rotateRight();

            success = tryAndApplyKickData(aux, I_ROTATE_RIGHT_KICK_DATA[tetromino.getRotationIndex()]);

            if (success) {
                tetromino.setXYRotationIndex(aux.getX(), aux.getY(), aux.getRotationIndex());
            }
        }
    }

    @Override
    public void rotateLeft(Tetromino tetromino) {
        boolean success;
        Tetromino aux;


        if (tetromino.getType() != I) {
            super.rotateLeft(tetromino);
        } else  {
            aux = tetromino.createCopy();
            aux.rotateLeft();

            success = tryAndApplyKickData(aux, I_ROTATE_LEFT_KICK_DATA[tetromino.getRotationIndex()]);

            if (success) {
                tetromino.setXYRotationIndex(aux.getX(), aux.getY(), aux.getRotationIndex());
            }
        }
    }
}
