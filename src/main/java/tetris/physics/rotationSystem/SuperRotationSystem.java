package tetris.physics.rotationSystem;

import tetris.physics.CollisionDetector;
import tetris.tetrominoes.Tetromino;

import static tetris.Config.*;

public class SuperRotationSystem extends RotationSystem {
    // J, L, S, T, Z Tetromino Wall Kick Data
    private static final int[][][] ROTATE_RIGHT_KICK_DATA = {
            // 0 -> R
            {{0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2}},
            // R -> 2
            {{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}},
            // 2 -> L
            {{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}},
            // L -> 0
            {{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}}
    };
    private static final int[][][] ROTATE_LEFT_KICK_DATA = {
            // 0 -> L
            {{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}},
            // R -> 0
            {{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}},
            // 2 -> R
            {{0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2}},
            // L -> 2
            {{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}}
    };
    // I Tetromino Wall Kick Data
    private static final int[][][] I_ROTATE_RIGHT_KICK_DATA = {
            // 0 -> R
            {{0, 0}, {-2, 0}, {1, 0}, {-2, -1}, {1, 2}},
            // R -> 2
            {{0, 0}, {-1, 0}, {2, 0}, {-1, 2}, {2, -1}},
            // 2 -> L
            {{0, 0}, {2, 0}, {-1, 0}, {2, 1}, {-1, -2}},
            // L -> 0
            {{0, 0}, {1, 0}, {-2, 0}, {1, -2}, {-2, 1}}
    };
    private static final int[][][] I_ROTATE_LEFT_KICK_DATA = {
            // 0 -> L
            {{0, 0}, {-1, 0}, {2, 0}, {-1, 2}, {2, -1}},
            // R -> 0
            {{0, 0}, {2, 0}, {-1, 0}, {2, 1}, {-1, -2}},
            // 2 -> R
            {{0, 0}, {1, 0}, {-2, 0}, {1, -2}, {-2, 1}},
            // L -> 2
            {{0, 0}, {-2, 0}, {1, 0}, {-2, -1}, {1, 2}}
    };
    // All Tetrominoes Kick Data for flips
    private static final int[][][] FLIP_KICK_DATA = {
            // 0 -> 2
            {{0, 0}, {0, 1}, {1, 1}, {-1, 1}, {1, 0}, {-1, 0}},
            // 1 -> 3
            {{0, 0}, {1, 0}, {1, 2}, {1, 1}, {0, 2}, {0, 1}},
            // 2 -> 0
            {{0, 0}, {0, -1}, {-1, -1}, {1, -1}, {-1, 0}, {1, 0}},
            // 3 -> 1
            {{0, 0}, {-1, 0}, {-1, 2}, {-1, 1}, {0, 2}, {0, 1}}
    };

    public SuperRotationSystem(boolean[][] grid) {
        super(grid);
    }

    @Override
    public void rotateRight(Tetromino tetromino) {
        boolean success = false;
        Tetromino aux = tetromino.createCopy();
        aux.rotateRight();

        if (tetromino.getType() == I) {
            success = tryAndApplyKickData(aux, I_ROTATE_RIGHT_KICK_DATA[tetromino.getRotationIndex()]);
        } else if (tetromino.getType() != O) {
            success = tryAndApplyKickData(aux, ROTATE_RIGHT_KICK_DATA[tetromino.getRotationIndex()]);
        }

        if (success) {
            tetromino.setXYRotationIndex(aux.getX(), aux.getY(), aux.getRotationIndex());
        }
    }

    @Override
    public void rotateLeft(Tetromino tetromino) {
        boolean success = false;
        Tetromino aux = tetromino.createCopy();
        aux.rotateLeft();

        if (tetromino.getType() == I) {
            success = tryAndApplyKickData(aux, I_ROTATE_LEFT_KICK_DATA[tetromino.getRotationIndex()]);
        } else if (tetromino.getType() != O) {
            success = tryAndApplyKickData(aux, ROTATE_LEFT_KICK_DATA[tetromino.getRotationIndex()]);
        }

        if (success) {
            tetromino.setXYRotationIndex(aux.getX(), aux.getY(), aux.getRotationIndex());
        }
    }

    @Override
    public void flip(Tetromino tetromino) {
        boolean success;
        Tetromino aux = tetromino.createCopy();
        aux.flip();

        success = tryAndApplyKickData(aux, FLIP_KICK_DATA[tetromino.getRotationIndex()]);

        if (success) {
            tetromino.setXYRotationIndex(aux.getX(), aux.getY(), aux.getRotationIndex());
        }
    }

    protected boolean tryAndApplyKickData(Tetromino t, int[][] kickData) {
        boolean success = false;

        for (int i = 0; i < kickData.length && !success; i++) {
            success = tryAndApplyKick(t, kickData[i][0], -kickData[i][1]);
        }

        return success;
    }

    protected boolean tryAndApplyKick(Tetromino t, int offsetX, int offsetY) {
        boolean success;

        t.setXY(t.getX() + offsetX, t.getY() + offsetY);

        success = !CollisionDetector.checkCollision(grid, t);

        if (!success) { t.setXY(t.getX() - offsetX, t.getY() - offsetY); }

        return success;
    }
}
