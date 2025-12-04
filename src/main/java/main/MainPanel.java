package main;

import menus.StartMenuPanel;
import tetris.panels.OnlineTwoPlayerTetrisPanel;
import tetris.panels.LocalTwoPlayerTetrisPanel;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// El MainPanel es el panel (contenido del frame (ventana)). Contiene el resto de paneles en un CardLayout y estos se
// pintan sobre el
public class MainPanel extends JPanel {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 7777;

    private static final String START_MENU_PANEL = "0";
    private static final String LOCAL_TETRIS_PANEL = "1";
    private static final String ONLINE_TETRIS_PANEL = "2";

    private StartMenuPanel startMenuPanel;
    private LocalTwoPlayerTetrisPanel localTwoPlayerTetrisPanel;
    private OnlineTwoPlayerTetrisPanel onlineTwoPlayerTetrisPanel;
    private CardLayout cardLayout;

    public MainPanel() {
        super();

        // Este layout será el que usa el MainPanel. Es como una baraja de paneles (en vez de de cartas) y puedes
        // cambiar de uno a otro fácilmente
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Crear los paneles para que estén cargados en memoria
        startMenuPanel = new StartMenuPanel(this);
        // TODO: Hay que añadir más menús para crear sala y conectarse a sala (igual son el mismo panel)
        localTwoPlayerTetrisPanel = new LocalTwoPlayerTetrisPanel(this);
        onlineTwoPlayerTetrisPanel = new OnlineTwoPlayerTetrisPanel(this);

        // Añadirlos al panel principal para poder cambiar entre ellos con el CardLayout
        add(startMenuPanel, START_MENU_PANEL);
        add(localTwoPlayerTetrisPanel, LOCAL_TETRIS_PANEL);
        add(onlineTwoPlayerTetrisPanel, ONLINE_TETRIS_PANEL);

        // Mostrar el menú principal
        cardLayout.show(this, START_MENU_PANEL);
    }

    // Muestra el panel de partida local de un jugador y empieza el juego
    public void startSoloLocalGame() {

    }

    // Muestra el panel de partida local y empieza el juego
    public void start1vs1LocalGame() {
        cardLayout.show(this, LOCAL_TETRIS_PANEL);
        localTwoPlayerTetrisPanel.startGame();
    }

    // PRE: Requiere de que se haya creado un socket para la partida
    // Muestra el panel de partida online (no sé si empezar las partidas aquí o no, darle una vuelta.
    // Si no empiezo las partidas aquí cambiar el nombre de los métodos a show).
    public void startOnlineGame() {

    }

    // Espera a que otro cliente se conecte a él y cuando se conecta empieza la partida
    // (cosas a pensar: enviar mensaje de empieza la partida? o simplemente cuando llega el seed
    // se considera que empieza la partida y ambos jugadores empiezan más o menos a la vez;
    // también hay que pensar en cómo acaba la partida, no me acuerdo de como lo implemente)
    // TODO: Este método creo que se pude mover a otra clase cliente que se conecte a un servidor
    // TODO: este servidor lo conecte con otro cliente (o si es creando una sala, el otro cliente
    // TODO: decida conectarse conectarse con él) y se cree un socket entre los clientes para jugar
    // TODO: la partida
    // Problema de redes del que no nos preocupamos porque solo podemos jugar en LAN, pero si se jugara
    // en WAN toda la información tendría que pasar por el servidor y el servidor tendría que repartir
    // la información entre los clientes.
    public void startOnlineGameAsHost() {
        try(ServerSocket server = new ServerSocket(SERVER_PORT)){
            try {
                Socket client = server.accept();
                onlineTwoPlayerTetrisPanel.setSocket(client);
                // Creating and sending seed
                long seed = System.currentTimeMillis();
                onlineTwoPlayerTetrisPanel.setSeed(System.currentTimeMillis());
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                dos.writeLong(seed);

                cardLayout.show(this, ONLINE_TETRIS_PANEL);
                onlineTwoPlayerTetrisPanel.startGame();
            }
            catch(IOException ioe) { ioe.printStackTrace(); }
        }
        catch(IOException ioe) { ioe.printStackTrace(); }
    }

    // Parecido al anterior, este es el que se conecta al anterior (si el anterior no se ha ejecutado salta la excepción)
    public void startOnlineGameAsClient() {
        try {
            Socket host = new Socket(SERVER_IP, SERVER_PORT);
            onlineTwoPlayerTetrisPanel.setSocket(host);
            // Reading and setting the seed
            DataInputStream dis = new DataInputStream(host.getInputStream());
            onlineTwoPlayerTetrisPanel.setSeed(dis.readLong());

            cardLayout.show(this, ONLINE_TETRIS_PANEL);
            onlineTwoPlayerTetrisPanel.startGame();
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void endGame() {
        cardLayout.show(this, START_MENU_PANEL);
    }
}
