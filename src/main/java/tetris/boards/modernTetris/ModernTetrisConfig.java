package tetris.boards.modernTetris;

import client.userInterface.panels.tetris.TetrisPanel;

/**
 * Configuration constants for the Modern Tetris game mode.
 * <p>
 * This class centralizes tuning parameters such as gravity speed, lock delays,
 * and queue sizes to ensure consistent gameplay behavior.
 * </p>
 */
public class ModernTetrisConfig {
    // --- UI CONFIGURATION ---
    /** Number of pieces visible in the "Next" queue. */
    public static final int TETROMINOES_QUEUE_SIZE = 5;

    // --- PHYSICS CONFIGURATION ---
    /** Initial gravity (G) in cells per frame. */
    public static final double INITIAL_GRAVITY = 0.025;

    /**
     * Number of frames a piece can sit on the ground before locking.
     * Set to half a second (approx 30 frames at 60 FPS).
     */
    public static final int LOCK_DELAY_FRAMES = (int) (TetrisPanel.FPS/2);

    /** The level at which gravity stops increasing (max speed). */
    public static final int MAX_GRAVITY_LEVEL = 30;

    /** Amount to increase gravity per level up to MAX_GRAVITY_LEVEL. */
    public static final double GRAVITY_INCREMENT_PER_LEVEL = (1 - INITIAL_GRAVITY) / MAX_GRAVITY_LEVEL;
}