package main;

import menus.StartMenuPanel;
import menus.WaitingOpponentPanel;
import tetris.nes.panels.OnePlayerNESPanel;
import tetris.tetrio.panels.OnePlayerTetrioPanel;
import tetris.tetrio.panels.OnlineTwoPlayerTetrioPanel;
import tetris.tetrio.panels.LocalTwoPlayerTetrioPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

// El MainPanel es el panel (contenido del frame (ventana)). Contiene el resto de paneles en un CardLayout y estos se
// pintan sobre el
public class MainPanel extends JPanel {
    private static final String START_MENU_PANEL = "0";
    private static final String WAITING_OPPONENT_PANEL = "1";
    private static final String ONE_PLAYER_TETRIO_PANEL = "2";
    private static final String ONE_PLAYER_NES_PANEL = "3";
    private static final String LOCAL_TETRIS_PANEL = "4";
    private static final String ONLINE_TETRIS_PANEL = "5";

    private StartMenuPanel startMenuPanel;
    private WaitingOpponentPanel waitingOpponentPanel;
    private OnePlayerTetrioPanel onePlayerTetrioPanel;
    private OnePlayerNESPanel onePlayerNESPanel;
    private LocalTwoPlayerTetrioPanel localTwoPlayerTetrioPanel;
    private OnlineTwoPlayerTetrioPanel onlineTwoPlayerTetrioPanel;

    private CardLayout cardLayout;

    private Image backgroundImage;

    public MainPanel() {
        super();

        // Este layout será el que usa el MainPanel. Es como una baraja de paneles (en vez de de cartas) y puedes
        // cambiar de uno a otro fácilmente
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Crear los paneles para que estén cargados en memoria
        startMenuPanel = new StartMenuPanel(this);
        onePlayerTetrioPanel = new OnePlayerTetrioPanel(this);
        onePlayerNESPanel = new OnePlayerNESPanel(this);
        localTwoPlayerTetrioPanel = new LocalTwoPlayerTetrioPanel(this);
        onlineTwoPlayerTetrioPanel = new OnlineTwoPlayerTetrioPanel(this);
        waitingOpponentPanel = new WaitingOpponentPanel(this);

        // Añadirlos al panel principal para poder cambiar entre ellos con el CardLayout
        add(startMenuPanel, START_MENU_PANEL);
        add(waitingOpponentPanel, WAITING_OPPONENT_PANEL);
        add(onePlayerTetrioPanel, ONE_PLAYER_TETRIO_PANEL);
        add(onePlayerNESPanel, ONE_PLAYER_NES_PANEL);
        add(localTwoPlayerTetrioPanel, LOCAL_TETRIS_PANEL);
        add(onlineTwoPlayerTetrioPanel, ONLINE_TETRIS_PANEL);

        // Cargar imagen de fondo
        try {
            // Asegúrate de que la ruta de la imagen es correcta.
            // Es mejor usar ImageIO.read para evitar problemas de ruta.
            backgroundImage = ImageIO.read(new File("src/main/resources/images/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("No se pudo cargar la imagen: " + "src/main/resources/images/background.png");
        }

        // Mostrar el menú principal
        cardLayout.show(this, START_MENU_PANEL);
    }

    // Muestra el panel de partida local de un jugador y empieza el juego
    public void startOnePlayerGame() {
        cardLayout.show(this, ONE_PLAYER_TETRIO_PANEL);
        onePlayerTetrioPanel.startGame();
    }

    public void startOnePlayerNESGame() {
        cardLayout.show(this, ONE_PLAYER_NES_PANEL);
        onePlayerNESPanel.startGame();
    }

    // Muestra el panel de partida local y empieza el juego
    public void start1vs1LocalGame() {
        cardLayout.show(this, LOCAL_TETRIS_PANEL);
        localTwoPlayerTetrioPanel.startGame();
    }

    // PRE: Requiere de que se haya creado un socket para la partida
    // Muestra el panel de partida online (no sé si empezar las partidas aquí o no, darle una vuelta.
    // Si no empiezo las partidas aquí cambiar el nombre de los métodos a show).
    public void connectToOnlineGame() {
        cardLayout.show(this, WAITING_OPPONENT_PANEL);
        waitingOpponentPanel.connect();
    }

    /**
     * Starts an online game
     * @param seed Seed for the game
     * @param socket Socket received from the server
     */
    public void startOnlineGame(long seed, Socket socket){
        onlineTwoPlayerTetrioPanel.setSeed(seed);
        onlineTwoPlayerTetrioPanel.setSocket(socket);
        cardLayout.show(this, ONLINE_TETRIS_PANEL);
        onlineTwoPlayerTetrioPanel.startGame();
    }

    /**
     * Connects to online game showing the host's room id
     */
    public void startOnlineGameAsHost() {

    }

    public void startOnlineGameAsClient() {

    }

    public void endGame() {
        cardLayout.show(this, START_MENU_PANEL);
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
