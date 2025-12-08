package tetris.boards.modernTetris;

import client.userInterface.panels.tetris.TetrisPanel;

public class ModernTetrisConfig {
    // TETROMINOES QUEUE CONFIGURATION
    public static final int TETROMINOES_QUEUE_SIZE = 5;

    // GRAVITY CONFIGURATION
    public static final double INITIAL_GRAVITY = 0.025;
    public static final int LOCK_DELAY_FRAMES = (int) (TetrisPanel.FPS/2);
    public static final int MAX_GRAVITY_LEVEL = 30;
    public static final double GRAVITY_INCREMENT_PER_LEVEL = (1- INITIAL_GRAVITY) / MAX_GRAVITY_LEVEL;
}
