package main;

import menus.StartMenuPanel;
import menus.WaitingOpponentPanel;
import tetris.tetrio.panels.OnlineTwoPlayerTetrioPanel;
import tetris.tetrio.panels.LocalTwoPlayerTetrioPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// El MainPanel es el panel (contenido del frame (ventana)). Contiene el resto de paneles en un CardLayout y estos se
// pintan sobre el
public class MainPanel extends JPanel {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 7777;

    private static final String START_MENU_PANEL = "0";
    private static final String WAITING_OPPONENT_PANEL = "2";
    private static final String LOCAL_TETRIS_PANEL = "3";
    private static final String ONLINE_TETRIS_PANEL = "4";


    private StartMenuPanel startMenuPanel;
    private WaitingOpponentPanel waitingOpponentPanel;
    private LocalTwoPlayerTetrioPanel localTwoPlayerTetrioPanel;
    private OnlineTwoPlayerTetrioPanel onlineTwoPlayerTetrioPanel;
    private CardLayout cardLayout;

    private Image imagenFondo;

    public MainPanel() {
        super();

        // Este layout será el que usa el MainPanel. Es como una baraja de paneles (en vez de de cartas) y puedes
        // cambiar de uno a otro fácilmente
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Crear los paneles para que estén cargados en memoria
        startMenuPanel = new StartMenuPanel(this);
        // TODO: Hay que añadir más menús para crear sala y conectarse a sala (igual son el mismo panel)
        localTwoPlayerTetrioPanel = new LocalTwoPlayerTetrioPanel(this);
        onlineTwoPlayerTetrioPanel = new OnlineTwoPlayerTetrioPanel(this);
        waitingOpponentPanel = new WaitingOpponentPanel(this);

        // Añadirlos al panel principal para poder cambiar entre ellos con el CardLayout
        add(startMenuPanel, START_MENU_PANEL);
        add(waitingOpponentPanel, WAITING_OPPONENT_PANEL);
        add(localTwoPlayerTetrioPanel, LOCAL_TETRIS_PANEL);
        add(onlineTwoPlayerTetrioPanel, ONLINE_TETRIS_PANEL);

        // Cargar imagen de fonde
        try {
            // Asegúrate de que la ruta de la imagen es correcta.
            // Es mejor usar ImageIO.read para evitar problemas de ruta.
            imagenFondo = ImageIO.read(new File("src/main/resources/images/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("No se pudo cargar la imagen: " + "src/main/resources/images/background.png");
        }

        // Mostrar el menú principal
        cardLayout.show(this, START_MENU_PANEL);
    }

    // Muestra el panel de partida local de un jugador y empieza el juego
    public void startSoloLocalGame() {

    }

    // Muestra el panel de partida local y empieza el juego
    public void start1vs1LocalGame() {
        cardLayout.show(this, LOCAL_TETRIS_PANEL);
        localTwoPlayerTetrioPanel.connectPlayersAndStartGame();
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
        onlineTwoPlayerTetrioPanel.connectPlayersAndStartGame();
    }

    /**
     * Connects to online game showing the host's room id
     */
    public void startOnlineGameAsHost() {

    }

    // Parecido al anterior, este es el que se conecta al anterior (si el anterior no se ha ejecutado salta la excepción)
    public void startOnlineGameAsClient() {
        try {
            Socket host = new Socket(SERVER_IP, SERVER_PORT);
            onlineTwoPlayerTetrioPanel.setSocket(host);
            // Reading and setting the seed
            DataInputStream dis = new DataInputStream(host.getInputStream());
            onlineTwoPlayerTetrioPanel.setSeed(dis.readLong());

            cardLayout.show(this, ONLINE_TETRIS_PANEL);
            onlineTwoPlayerTetrioPanel.connectPlayersAndStartGame();
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void endGame() {
        cardLayout.show(this, START_MENU_PANEL);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imagenFondo != null) {
            // Convert to Graphics2D for better control
            Graphics2D g2d = (Graphics2D) g;

            // Optional: This makes the image look smoother when stretched
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            // Draw the image stretching it to the current width and height of the panel
            g2d.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
