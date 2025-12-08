package tetris.boards.nes;

import client.userInterface.panels.tetris.TetrisPanel;

/**
 * Configuration constants for NES (Classic) Tetris emulation.
 * <p>
 * This class translates the original game's frame-based values (running at approx 60.09 Hz)
 * into values compatible with this application's update loop.
 * </p>
 */
public class NESConfig {
    // --- UI CONFIGURATION ---
    /** NES Tetris only shows one next piece. */
    public static final int TETROMINOES_QUEUE_SIZE = 1;

    // --- PHYSICS CONFIGURATION ---

    /**
     * The speed of soft dropping (1/2 G in NES terms).
     * Converted to ensure consistent speed across different frame rates.
     */
    public static final double SOFT_DROP_GRAVITY_IN_NES_FPS = 0.5;

    /**
     * The initial speed at Level 0 (1 cell every 48 frames, represented here as ~36 for scaling).
     */
    public static final int FRAMES_PER_CELL_INITIAL_GRAVITY_IN_NES_FPS = 36;

    /**
     * Calculates the initial gravity (cells per frame) normalized for the current application FPS.
     */
    public static final double INITIAL_GRAVITY = TetrisPanel.NES_FPS / (FRAMES_PER_CELL_INITIAL_GRAVITY_IN_NES_FPS * TetrisPanel.FPS);

    /**
     * Standard entry delay / lock delay.
     * While NES doesn't have a modern "lock delay" (it locks on impact), it has entry delays.
     * This value approximates the feeling of the piece settling.
     */
    public static final int LOCK_DELAY_FRAMES = 12;
}