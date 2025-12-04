package tetris;

public class Config {
    // FACTS
    public static final int NUMBER_TETROMINOES = 7;
    public static final int I = 0;
    public static final int O = 1;
    public static final int T = 2;
    public static final int J = 3;
    public static final int L = 4;
    public static final int S = 5;
    public static final int Z = 6;

    // GAME UPDATE CONFIG
    public static final double FPS = 62.5;
    public static final int MILLISECONDS_PER_FRAME = (int) (1000/ FPS);
    public static final double NES_FPS = 60.0988;

    // BOARD CONFIGURATION
    public static final int BOARD_ROWS = 20;
    public static final int BOARD_COLUMNS = 10;
    public static final int BOARD_SPAWN_ROWS = 3;

    // QUEUE CONFIGURATION
    public static final int TETROMINOES_QUEUE_SIZE = 5;

    // GRAVITY CONFIGURATION
    public static final int SOFT_DROP_FACTOR = 6;
    public static final int SOFT_DROP_DELAY_QUOTIENT = SOFT_DROP_FACTOR/6;
    public static final int NES_FRAMES_PER_CELL_INITIAL_GRAVITY = 48;
    public static final double INITIAL_GRAVITY = ((1./ NES_FRAMES_PER_CELL_INITIAL_GRAVITY)*NES_FPS)/ FPS;

    // INPUT CONFIGURATION
    public static final int AUTOMATIC_REPEAT_RATE_FRAMES = ((int) FPS) / 30;
    public static final int DELAYED_AUTO_SHIFT_FRAMES = ((int) FPS) / 6;

    // SHADOW CONFIGURATION
    public static final int SHADOW_TRANSPARENCY_PERCENTAGE = 25;
    public static final int SHADOW_TRANSPARENCY = (SHADOW_TRANSPARENCY_PERCENTAGE *255/100) * 33554432;

    // GAME DRAW CONFIG
    public static final int CELL_SIZE = 25;
    public static final int TETROMINO_BORDER_WIDTH = CELL_SIZE/6;
    public static final int FRAME_PADDING = 2*CELL_SIZE;
    public static final int TETROMINOES_QUEUE_WIDTH = 5*CELL_SIZE;
    public static final int TETROMINO_HOLDER_WIDTH = 5*CELL_SIZE;
    public static final int TETROMINO_HOLDER_HEIGHT = 5*CELL_SIZE;
    public static final int BOARD_WIDTH = BOARD_COLUMNS*CELL_SIZE;
    public static final int BOARD_HEIGHT = BOARD_ROWS*CELL_SIZE;
    public static final int BOARD_SPAWN_HEIGHT = BOARD_SPAWN_ROWS*CELL_SIZE;
    public static final int BOARD1_X = FRAME_PADDING + TETROMINO_HOLDER_WIDTH;
    public static final int BOARD1_Y = FRAME_PADDING;
    public static final int BOARD2_X = FRAME_PADDING + TETROMINO_HOLDER_WIDTH + BOARD_WIDTH + TETROMINOES_QUEUE_WIDTH + FRAME_PADDING + TETROMINO_HOLDER_WIDTH;
    public static final int BOARD2_Y = BOARD1_Y;
    public static final int PANEL_WIDTH = 3*FRAME_PADDING + 2* TETROMINO_HOLDER_WIDTH + 2*BOARD_WIDTH + 2* TETROMINOES_QUEUE_WIDTH;
    public static final int PANEL_HEIGHT = 3*FRAME_PADDING + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT;
}
