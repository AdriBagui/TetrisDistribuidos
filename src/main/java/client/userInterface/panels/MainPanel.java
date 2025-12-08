package client.userInterface.panels;

import client.ServerConnector;
import client.userInterface.panels.tetris.singlePlayerPanels.SinglePlayerModernTetrisPanel;
import client.userInterface.panels.tetris.singlePlayerPanels.SinglePlayerNESPanel;
import client.userInterface.panels.tetris.twoPlayerPanels.*;
import server.ConnectionMode;
import tetris.boards.components.BoardGrid;
import client.keyMaps.KeyInputHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

import static server.Server.SERVER_IP;
import static server.Server.SERVER_PORT;

/**
 * The primary container panel for the application's user interface.
 * <p>
 * This class uses a {@link CardLayout} to manage navigation between the different screens
 * (Start Menu, Settings, Game Boards, etc.). It acts as the central controller for the client-side UI,
 * holding references to all sub-panels and the server connector.
 * </p>
 */
public class MainPanel extends JPanel {
    // --- UI Constants & Configuration ---

    // Colors
    public static final Color BACKGROUND_COLOR = new Color(22, 22, 22);
    public static final Color TEXT_COLOR = new Color(220, 220, 220);
    public static final Color ERROR_COLOR = new Color(255, 80, 80);
    public static final Color WARNING_COLOR = new Color(255, 200, 0);
    public static final Color INFO_COLOR = new Color(60, 130, 255);

    // Fonts
    public static final String FONT_NAME = "Segoe UI";
    public static final Font DEFAULT_FONT = new Font(FONT_NAME, Font.PLAIN, 14);
    public static final Font BOLD_DEFAULT_FONT = new Font(FONT_NAME, Font.BOLD, 14);
    public static final Font TITLE_FONT = new Font(FONT_NAME, Font.BOLD, 48);
    public static final Font SUBTITLE_FONT = new Font(FONT_NAME, Font.BOLD, 24);
    public static final Font HEADER_FONT = new Font(FONT_NAME, Font.BOLD, 18);
    public static final Font GAME_OVER_FONT = new Font(FONT_NAME, Font.BOLD, 40);
    public static final Font MEDIUM_MESSAGE_FONT = new Font(FONT_NAME, Font.PLAIN, 20);

    // Dimensions
    public static final int CELL_SIZE = 25;
    public static final int TETROMINO_BORDER_WIDTH = CELL_SIZE/6;
    public static final int FRAME_PADDING = 2*CELL_SIZE;
    public static final int TETROMINOES_QUEUE_WIDTH = 5*CELL_SIZE;
    public static final int TETROMINO_HOLDER_WIDTH = 5*CELL_SIZE;
    public static final int TETROMINO_HOLDER_HEIGHT = 5*CELL_SIZE;
    public static final int BOARD_WIDTH = BoardGrid.COLUMNS*CELL_SIZE;
    public static final int BOARD_HEIGHT = BoardGrid.ROWS*CELL_SIZE;
    public static final int BOARD_SPAWN_HEIGHT = BoardGrid.SPAWN_ROWS*CELL_SIZE;

    // Layout Calculation for Boards
    public static final int BOARD1_X = FRAME_PADDING;
    public static final int BOARD1_Y = FRAME_PADDING;
    public static final int BOARD2_X = FRAME_PADDING + TETROMINO_HOLDER_WIDTH + BOARD_WIDTH + TETROMINOES_QUEUE_WIDTH + FRAME_PADDING;
    public static final int BOARD2_Y = BOARD1_Y;
    public static final int PANEL_WIDTH = 3*FRAME_PADDING + 2*TETROMINO_HOLDER_WIDTH + 2*BOARD_WIDTH + 2* TETROMINOES_QUEUE_WIDTH;
    public static final int PANEL_HEIGHT = 3*FRAME_PADDING + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT;

    // CardLayout Keys
    private static final String START_MENU_PANEL = "0";
    private static final String SETTINGS_PANEL = "1";
    private static final String WAITING_OPPONENT_PANEL = "2";
    private static final String SINGLE_PLAYER_MODERN_TETRIS_PANEL = "3";
    private static final String LOCAL_TWO_PLAYERS_MODERN_TETRIS_PANEL = "4";
    private static final String SINGLE_PLAYER_NES_PANEL = "5";
    private static final String LOCAL_TWO_PLAYERS_NES_PANEL = "6";
    private static final String ONLINE_MODERN_TETRIS_PANEL = "7";
    private static final String ONLINE_NES_PANEL = "8";

    // Sub-Panels
    private final SettingsPanel settingsPanel;
    private final SinglePlayerModernTetrisPanel singlePlayerModernTetrisPanel;
    private final LocalTwoPlayerModernTetrisPanel localTwoPlayerModernTetrisPanel;
    private final SinglePlayerNESPanel singlePlayerNESPanel;
    private final LocalTwoPlayerNESPanel localTwoPlayerNESPanel;
    private final OnlineTwoPlayersModernTetrisPanel onlineModernTetrisPanel;
    private final OnlineTwoPlayersNESPanel onlineNESPanel;

    private final ServerConnector serverConnector;

    private final CardLayout cardLayout;
    private Image backgroundImage;

    /**
     * Constructs the MainPanel, initializes all sub-panels, sets up the CardLayout,
     * and loads UI resources.
     * @param serverIp the server ip to connect to
     * @param serverPort the server port to connect to
     */
    public MainPanel(String serverIp, int serverPort) {
        super();

        setBackground(BACKGROUND_COLOR);

        // This layout allows swapping between different views (panels) like a deck of cards
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Preload all panels into memory
        settingsPanel = new SettingsPanel(this);
        StartMenuPanel startMenuPanel = new StartMenuPanel(this);
        WaitingOpponentPanel waitingOpponentPanel = new WaitingOpponentPanel();
        singlePlayerModernTetrisPanel = new SinglePlayerModernTetrisPanel(this);
        localTwoPlayerModernTetrisPanel = new LocalTwoPlayerModernTetrisPanel(this);
        singlePlayerNESPanel = new SinglePlayerNESPanel(this);
        localTwoPlayerNESPanel = new LocalTwoPlayerNESPanel(this);
        onlineModernTetrisPanel = new OnlineTwoPlayersModernTetrisPanel(this);
        onlineNESPanel = new OnlineTwoPlayersNESPanel(this);

        serverConnector = new ServerConnector(this, waitingOpponentPanel, serverIp, serverPort);

        // Add panels to the CardLayout
        add(startMenuPanel, START_MENU_PANEL);
        add(settingsPanel, SETTINGS_PANEL);
        add(waitingOpponentPanel, WAITING_OPPONENT_PANEL);
        add(singlePlayerModernTetrisPanel, SINGLE_PLAYER_MODERN_TETRIS_PANEL);
        add(localTwoPlayerModernTetrisPanel, LOCAL_TWO_PLAYERS_MODERN_TETRIS_PANEL);
        add(singlePlayerNESPanel, SINGLE_PLAYER_NES_PANEL);
        add(localTwoPlayerNESPanel, LOCAL_TWO_PLAYERS_NES_PANEL);
        add(onlineModernTetrisPanel, ONLINE_MODERN_TETRIS_PANEL);
        add(onlineNESPanel, ONLINE_NES_PANEL);

        // Load background image
        try {
            backgroundImage = ImageIO.read(new File("src/main/resources/images/background.png"));
        } catch (IOException e) {
            System.out.println("Could not load background image: " + "src/main/resources/images/background.png");
        }

        // Show start menu by default
        cardLayout.show(this, START_MENU_PANEL);
    }

    public MainPanel() { this(SERVER_IP, SERVER_PORT); }

    /**
     * Distributes the KeyInputHandler to all panels that require keyboard interaction.
     *
     * @param keyInputHandler The handler responsible for processing keyboard events.
     */
    public void setKeyInputHandler(KeyInputHandler keyInputHandler) {
        settingsPanel.setKeyInputHandler(keyInputHandler);
        singlePlayerModernTetrisPanel.setKeyInputHandler(keyInputHandler);
        localTwoPlayerModernTetrisPanel.setKeyInputHandler(keyInputHandler);
        singlePlayerNESPanel.setKeyInputHandler(keyInputHandler);
        localTwoPlayerNESPanel.setKeyInputHandler(keyInputHandler);
        onlineModernTetrisPanel.setKeyInputHandler(keyInputHandler);
        onlineNESPanel.setKeyInputHandler(keyInputHandler);
    }

    /**
     * Switches the view to the Settings Panel.
     * Refreshes the settings UI to display the most current key bindings.
     */
    public void openSettings() {
        settingsPanel.refresh();
        cardLayout.show(this, SETTINGS_PANEL);
        settingsPanel.requestFocusInWindow();
    }

    /**
     * Switches to and starts a Single Player Modern Tetris (Modern) game.
     */
    public void startSinglePlayerModernTetrisGame() {
        cardLayout.show(this, SINGLE_PLAYER_MODERN_TETRIS_PANEL);
        singlePlayerModernTetrisPanel.startGame();
    }

    /**
     * Switches to and starts a Local Two Player Modern Tetris (Modern) game.
     */
    public void startTwoPlayersModernTetrisGame() {
        cardLayout.show(this, LOCAL_TWO_PLAYERS_MODERN_TETRIS_PANEL);
        localTwoPlayerModernTetrisPanel.startGame();
    }

    /**
     * Switches to and starts a Single Player NES (Classic) game.
     */
    public void startSinglePlayerNESGame() {
        cardLayout.show(this, SINGLE_PLAYER_NES_PANEL);
        singlePlayerNESPanel.startGame();
    }

    /**
     * Switches to and starts a Local Two Player NES (Classic) game.
     */
    public void startTwoPlayersNESGame() {
        cardLayout.show(this, LOCAL_TWO_PLAYERS_NES_PANEL);
        localTwoPlayerNESPanel.startGame();
    }

    /**
     * Initiates the connection process for an online game.
     * Displays the waiting screen while the {@link ServerConnector} attempts to connect.
     *
     * @param gameMode The specific online mode (Quick Play, Host, Join).
     */
    public void connectToOnlineGame(ConnectionMode gameMode) {
        cardLayout.show(this, WAITING_OPPONENT_PANEL);
        serverConnector.connect(gameMode);
    }

    /**
     * Starts an online game session after a successful connection.
     *
     * @param seed     The random seed shared by the server to ensure deterministic gameplay.
     * @param socket   The active socket connection to the server/opponent.
     * @param gameMode The game mode being played (Modern or NES).
     */
    public void startOnlineGame(long seed, Socket socket, ConnectionMode gameMode) {
        OnlineTwoPlayersPanel onlineTwoPlayersPanel;

        String onlineTwoPlayersPanelName = switch (gameMode) {
            case MODERN_TETRIS_QUICK_PLAY, HOST_GAME, JOIN_GAME -> {
                onlineTwoPlayersPanel = onlineModernTetrisPanel;
                yield ONLINE_MODERN_TETRIS_PANEL;
            }
            case NES_QUICK_PLAY -> {
                onlineTwoPlayersPanel = onlineNESPanel;
                yield ONLINE_NES_PANEL;
            }
        };

        onlineTwoPlayersPanel.setSeed(seed);
        onlineTwoPlayersPanel.setSocket(socket);
        cardLayout.show(this, onlineTwoPlayersPanelName);
        onlineTwoPlayersPanel.startGame();
    }

    /**
     * Returns the view to the Start Menu.
     */
    public void backToStartMenu() {
        cardLayout.show(this, START_MENU_PANEL);
    }

    /**
     * Custom painting to render the background image across the entire panel.
     *
     * @param g The {@link Graphics} context.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            // Convert to Graphics2D for better control
            Graphics2D g2d = (Graphics2D) g;

            // Optional: This makes the image look smoother when stretched
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            // Draw the image stretching it to the current width and height of the panel
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}