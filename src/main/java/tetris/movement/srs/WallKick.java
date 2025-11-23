package tetris.movement.srs;

import tetris.movement.CollisionDetector;
import tetris.generators.TetrominoFactory;
import tetris.tetrominoes.Tetromino;

public class WallKick extends SuperRotationSystem {
    public WallKick(CollisionDetector collisionDetector) {
        super(collisionDetector);
    }

    @Override
    public void rotateRight(Tetromino tetromino) {
        if (tetromino.getType() == TetrominoFactory.O) return;

        int rotationIndex = tetromino.getRotationIndex();
        int x = tetromino.getX();
        int y = tetromino.getY();

        tetromino.rotateRight();

        // Test 1 (0,0) - Basic Rotation
        if (!collisionDetector.checkCollision(tetromino))
            return;

        // If basic rotation failed, try Wall Kicks
        if (tetromino.getType() == TetrominoFactory.I) {
            // I-Piece Kick Data
            switch (rotationIndex) {
                case 0: // 0 -> R
                    if (tryKick(tetromino, x, y, -2, 0)) return;
                    if (tryKick(tetromino, x, y, 1, 0)) return;
                    if (tryKick(tetromino, x, y, -2, 1)) return;
                    if (tryKick(tetromino, x, y, 1, -2)) return;
                    break;
                case 1: // R -> 2
                    if (tryKick(tetromino, x, y, -1, 0)) return;
                    if (tryKick(tetromino, x, y, 2, 0)) return;
                    if (tryKick(tetromino, x, y, -1, -2)) return;
                    if (tryKick(tetromino, x, y, 2, 1)) return;
                    break;
                case 2: // 2 -> L
                    if (tryKick(tetromino, x, y, 2, 0)) return;
                    if (tryKick(tetromino, x, y, -1, 0)) return;
                    if (tryKick(tetromino, x, y, 2, -1)) return;
                    if (tryKick(tetromino, x, y, -1, 2)) return;
                    break;
                case 3: // L -> 0
                    if (tryKick(tetromino, x, y, 1, 0)) return;
                    if (tryKick(tetromino, x, y, -2, 0)) return;
                    if (tryKick(tetromino, x, y, 1, 2)) return;
                    if (tryKick(tetromino, x, y, -2, -1)) return;
                    break;
            }
        } else {
            // J, L, S, T, Z Kick Data
            switch (rotationIndex) {
                case 0: // 0 -> R
                    if (tryKick(tetromino, x, y, -1, 0)) return;
                    if (tryKick(tetromino, x, y, -1, -1)) return;
                    if (tryKick(tetromino, x, y, 0, 2)) return;
                    if (tryKick(tetromino, x, y, -1, 2)) return;
                    break;
                case 1: // R -> 2
                    if (tryKick(tetromino, x, y, 1, 0)) return;
                    if (tryKick(tetromino, x, y, 1, 1)) return;
                    if (tryKick(tetromino, x, y, 0, -2)) return;
                    if (tryKick(tetromino, x, y, 1, -2)) return;
                    break;
                case 2: // 2 -> L
                    if (tryKick(tetromino, x, y, 1, 0)) return;
                    if (tryKick(tetromino, x, y, 1, -1)) return;
                    if (tryKick(tetromino, x, y, 0, 2)) return;
                    if (tryKick(tetromino, x, y, 1, 2)) return;
                    break;
                case 3: // L -> 0
                    if (tryKick(tetromino, x, y, -1, 0)) return;
                    if (tryKick(tetromino, x, y, -1, 1)) return;
                    if (tryKick(tetromino, x, y, 0, -2)) return;
                    if (tryKick(tetromino, x, y, -1, -2)) return;
                    break;
            }
        }

        // If all kicks failed, revert rotation
        tetromino.setXY(x, y);
        tetromino.rotateLeft();
    }

    @Override
    public void rotateLeft(Tetromino tetromino) {
        if (tetromino.getType() == TetrominoFactory.O) return;

        int rotationIndex = tetromino.getRotationIndex();
        int x = tetromino.getX();
        int y = tetromino.getY();

        tetromino.rotateLeft();

        if (!collisionDetector.checkCollision(tetromino))
            return;

        if (tetromino.getType() == TetrominoFactory.I) {
            switch (rotationIndex) {
                case 0: // 0 -> L
                    if (tryKick(tetromino, x, y, -1, 0)) return;
                    if (tryKick(tetromino, x, y, 2, 0)) return;
                    if (tryKick(tetromino, x, y, -1, -2)) return;
                    if (tryKick(tetromino, x, y, 2, 1)) return;
                    break;
                case 1: // R -> 0
                    if (tryKick(tetromino, x, y, 2, 0)) return;
                    if (tryKick(tetromino, x, y, -1, 0)) return;
                    if (tryKick(tetromino, x, y, 2, -1)) return;
                    if (tryKick(tetromino, x, y, -1, 2)) return;
                    break;
                case 2: // 2 -> R
                    if (tryKick(tetromino, x, y, 1, 0)) return;
                    if (tryKick(tetromino, x, y, -2, 0)) return;
                    if (tryKick(tetromino, x, y, 1, 2)) return;
                    if (tryKick(tetromino, x, y, -2, -1)) return;
                    break;
                case 3: // L -> 2
                    if (tryKick(tetromino, x, y, -2, 0)) return;
                    if (tryKick(tetromino, x, y, 1, 0)) return;
                    if (tryKick(tetromino, x, y, -2, 1)) return;
                    if (tryKick(tetromino, x, y, 1, -2)) return;
                    break;
            }
        } else {
            // J, L, S, T, Z Kick Data
            switch (rotationIndex) {
                case 0: // 0 -> L
                    if (tryKick(tetromino, x, y, 1, 0)) return;
                    if (tryKick(tetromino, x, y, 1, -1)) return;
                    if (tryKick(tetromino, x, y, 0, 2)) return;
                    if (tryKick(tetromino, x, y, 1, 2)) return;
                    break;
                case 1: // R -> 0
                    if (tryKick(tetromino, x, y, 1, 0)) return;
                    if (tryKick(tetromino, x, y, 1, 1)) return;
                    if (tryKick(tetromino, x, y, 0, -2)) return;
                    if (tryKick(tetromino, x, y, 1, -2)) return;
                    break;
                case 2: // 2 -> R
                    if (tryKick(tetromino, x, y, -1, 0)) return;
                    if (tryKick(tetromino, x, y, -1, -1)) return;
                    if (tryKick(tetromino, x, y, 0, 2)) return;
                    if (tryKick(tetromino, x, y, -1, 2)) return;
                    break;
                case 3: // L -> 2
                    if (tryKick(tetromino, x, y, -1, 0)) return;
                    if (tryKick(tetromino, x, y, -1, 1)) return;
                    if (tryKick(tetromino, x, y, 0, -2)) return;
                    if (tryKick(tetromino, x, y, -1, -2)) return;
                    break;
            }
        }

        // If all kicks failed, revert rotation
        tetromino.setXY(x, y);
        tetromino.rotateRight();
    }

    @Override
    public void flip(Tetromino tetromino) {
        // Do nothing
        return;
    }
}
