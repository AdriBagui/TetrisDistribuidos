package main;

import distributedServices.ConnectionMode;
import menus.StartMenuPanel;
import menus.WaitingOpponentPanel;
import tetris.boards.components.BoardGrid;
import tetris.keyMaps.KeyInputHandler;
import tetris.panels.singlePlayerPanels.SinglePlayerNESPanel;
import tetris.panels.singlePlayerPanels.SinglePlayerTetrioPanel;
import tetris.panels.twoPlayerPanels.LocalTwoPlayerNESPanel;
import tetris.panels.twoPlayerPanels.OnlineTwoPlayerNESPanel;
import tetris.panels.twoPlayerPanels.OnlineTwoPlayerTetrioPanel;
import tetris.panels.twoPlayerPanels.LocalTwoPlayerTetrioPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

// El MainPanel es el panel (contenido del frame (ventana)). Contiene el resto de paneles en un CardLayout y estos se
// pintan sobre el
public class MainPanel extends JPanel {
    // GAME DRAW CONFIG
    public static final int CELL_SIZE = 25;
    public static final int TETROMINO_BORDER_WIDTH = CELL_SIZE/6;
    public static final int FRAME_PADDING = 2*CELL_SIZE;
    public static final int TETROMINOES_QUEUE_WIDTH = 5*CELL_SIZE;
    public static final int TETROMINO_HOLDER_WIDTH = 5*CELL_SIZE;
    public static final int TETROMINO_HOLDER_HEIGHT = 5*CELL_SIZE;
    public static final int BOARD_WIDTH = BoardGrid.COLUMNS*CELL_SIZE;
    public static final int BOARD_HEIGHT = BoardGrid.ROWS*CELL_SIZE;
    public static final int BOARD_SPAWN_HEIGHT = BoardGrid.SPAWN_ROWS*CELL_SIZE;
    public static final int BOARD1_X = FRAME_PADDING;
    public static final int BOARD1_Y = FRAME_PADDING;
    public static final int BOARD2_X = FRAME_PADDING + TETROMINO_HOLDER_WIDTH + BOARD_WIDTH + TETROMINOES_QUEUE_WIDTH + FRAME_PADDING;
    public static final int BOARD2_Y = BOARD1_Y;
    public static final int PANEL_WIDTH = 3*FRAME_PADDING + 2*TETROMINO_HOLDER_WIDTH + 2*BOARD_WIDTH + 2* TETROMINOES_QUEUE_WIDTH;
    public static final int PANEL_HEIGHT = 3*FRAME_PADDING + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT;

    private static final String START_MENU_PANEL = "0";
    private static final String WAITING_OPPONENT_PANEL = "1";
    private static final String SINGLE_PLAYER_TETRIO_PANEL = "2";
    private static final String LOCAL_TWO_PLAYERS_TETRIO_PANEL = "3";
    private static final String SINGLE_PLAYER_NES_PANEL = "4";
    private static final String LOCAL_TWO_PLAYERS_NES_PANEL = "5";
    private static final String ONLINE_TETRIO_PANEL = "6";
    private static final String ONLINE_NES_PANEL = "7";

    private final WaitingOpponentPanel waitingOpponentPanel;
    private final SinglePlayerTetrioPanel singlePlayerTetrioPanel;
    private final LocalTwoPlayerTetrioPanel localTwoPlayerTetrioPanel;
    private final SinglePlayerNESPanel singlePlayerNESPanel;
    private final LocalTwoPlayerNESPanel localTwoPlayerNESPanel;
    private final OnlineTwoPlayerTetrioPanel onlineTetrioPanel;
    private final OnlineTwoPlayerNESPanel onlineNESPanel;

    private final byte endGameByte = (byte)127;

    private CardLayout cardLayout;

    private Image backgroundImage;

    public MainPanel() {
        super();

        setBackground(new Color(22, 22, 22));

        // Este layout será el que usa el MainPanel. Es como una baraja de paneles (en vez de de cartas) y puedes
        // cambiar de uno a otro fácilmente
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Crear los paneles para que estén cargados en memoria
        StartMenuPanel startMenuPanel = new StartMenuPanel(this);
        waitingOpponentPanel = new WaitingOpponentPanel(this);
        singlePlayerTetrioPanel = new SinglePlayerTetrioPanel(this);
        localTwoPlayerTetrioPanel = new LocalTwoPlayerTetrioPanel(this);
        singlePlayerNESPanel = new SinglePlayerNESPanel(this);
        localTwoPlayerNESPanel = new LocalTwoPlayerNESPanel(this);
        onlineTetrioPanel = new OnlineTwoPlayerTetrioPanel(this);
        onlineNESPanel = new OnlineTwoPlayerNESPanel(this);

        // Añadirlos al panel principal para poder cambiar entre ellos con el CardLayout
        add(startMenuPanel, START_MENU_PANEL);
        add(waitingOpponentPanel, WAITING_OPPONENT_PANEL);
        add(singlePlayerTetrioPanel, SINGLE_PLAYER_TETRIO_PANEL);
        add(localTwoPlayerTetrioPanel, LOCAL_TWO_PLAYERS_TETRIO_PANEL);
        add(singlePlayerNESPanel, SINGLE_PLAYER_NES_PANEL);
        add(localTwoPlayerNESPanel, LOCAL_TWO_PLAYERS_NES_PANEL);
        add(onlineTetrioPanel, ONLINE_TETRIO_PANEL);
        add(onlineNESPanel, ONLINE_NES_PANEL);

        // Cargar imagen de fondo
        try {
            // Asegúrate de que la ruta de la imagen es correcta.
            // Es mejor usar ImageIO.read para evitar problemas de ruta.
            backgroundImage = ImageIO.read(new File("src/main/resources/images/background.png"));
        } catch (IOException e) {
            System.out.println("No se pudo cargar la imagen: " + "src/main/resources/images/background.png");
        }

        // Mostrar el menú principal
        cardLayout.show(this, START_MENU_PANEL);
    }

    public void setKeyInputHandler(KeyInputHandler keyInputHandler) {
        singlePlayerTetrioPanel.setKeyInputHandler(keyInputHandler);
        localTwoPlayerTetrioPanel.setKeyInputHandler(keyInputHandler);
        singlePlayerNESPanel.setKeyInputHandler(keyInputHandler);
        localTwoPlayerNESPanel.setKeyInputHandler(keyInputHandler);
        onlineTetrioPanel.setKeyInputHandler(keyInputHandler);
        onlineNESPanel.setKeyInputHandler(keyInputHandler);
    }

    public void startSinglePlayerTetrioGame() {
        cardLayout.show(this, SINGLE_PLAYER_TETRIO_PANEL);
        singlePlayerTetrioPanel.startGame();
    }
    public void startTwoPlayersTetrioGame() {
        cardLayout.show(this, LOCAL_TWO_PLAYERS_TETRIO_PANEL);
        localTwoPlayerTetrioPanel.startGame();
    }
    public void startSinglePlayerNESGame() {
        cardLayout.show(this, SINGLE_PLAYER_NES_PANEL);
        singlePlayerNESPanel.startGame();
    }
    public void startTwoPlayersNESGame() {
        cardLayout.show(this, LOCAL_TWO_PLAYERS_NES_PANEL);
        localTwoPlayerNESPanel.startGame();
    }

    // PRE: Requiere de que se haya creado un socket para la partida
    // Muestra el panel de partida online (no sé si empezar las partidas aquí o no, darle una vuelta.
    // Si no empiezo las partidas aquí cambiar el nombre de los métodos a show).
    public void connectToOnlineGame(ConnectionMode gameMode) {
        if(!gameMode.equals(ConnectionMode.JOIN_GAME_MODE)){ // If we are joining we don't want to change the panel
            cardLayout.show(this, WAITING_OPPONENT_PANEL);
        }
        waitingOpponentPanel.connect(gameMode);
    }

    /**
     * Starts a modern tetris online game
     * @param seed Seed for the game
     * @param socket Socket received from the server
     */
    public void startOnlineGame(long seed, Socket socket) {
        onlineTetrioPanel.setSeed(seed);
        onlineTetrioPanel.setSocket(socket);
        cardLayout.show(this, ONLINE_TETRIO_PANEL);
        onlineTetrioPanel.startGame();
    }

    /**
     * Starts a classic tetris online game
     * @param seed Seed for the game
     * @param socket Socket received from the server
     */
    public void startOnlineNESGame(long seed, Socket socket) {
        onlineNESPanel.setSeed(seed);
        onlineNESPanel.setSocket(socket);
        cardLayout.show(this, ONLINE_NES_PANEL);
        onlineNESPanel.startGame();
    }

    public void backToStartMenu() {
        cardLayout.show(this, START_MENU_PANEL);
    }

    private void endGame(Socket socket) {
        try{
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeByte(endGameByte); // Byte that indicates end of game
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

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
