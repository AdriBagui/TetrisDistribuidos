package tetris.boards.nes;

import client.userInterface.panels.tetris.TetrisPanel;

public class NESConfig {
    // TETROMINOES QUEUE CONFIGURATION
    public static final int TETROMINOES_QUEUE_SIZE = 1;

    // GRAVITY CONFIGURATION
    public static final double SOFT_DROP_GRAVITY_IN_NES_FPS = 0.5;
    public static final int FRAMES_PER_CELL_INITIAL_GRAVITY_IN_NES_FPS = 36;
    public static final double INITIAL_GRAVITY = TetrisPanel.NES_FPS/(FRAMES_PER_CELL_INITIAL_GRAVITY_IN_NES_FPS *TetrisPanel.FPS);
    public static final int LOCK_DELAY_FRAMES = 12;
}
