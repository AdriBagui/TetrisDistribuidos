package client.userInterface;

import client.userInterface.panels.MainPanel;
import client.keyMaps.InputAction;
import client.keyMaps.KeyBindingsManager;
import client.keyMaps.KeyInputHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static client.ServerConnector.SERVER_IP;
import static server.Server.SERVER_PORT;

/**
 * The main application window (JFrame).
 * <p>
 * This class initializes the top-level window, loads the application icon, sets up the
 * main content panel ({@link MainPanel}), and initializes the keyboard input handling system.
 * It also manages the initial loading and creation of key bindings configuration files.
 * </p>
 */
public class MainFrame extends JFrame {
    private final KeyInputHandler keyInputHandler;

    /**
     * Constructs the MainFrame, configuring window properties and components.
     * @param serverIp the server ip to connect to
     * @param serverPort the server port to connect to
     */
    public MainFrame(String serverIp, int serverPort) {
        // Sets the title shown in the title bar
        setTitle("Tetris Distribuidos");

        // Ensure the application exits when the window is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Prevent resizing to maintain layout integrity
        setResizable(false);

        // Load Application Icon
        try {
            BufferedImage icon = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/icon.png")));
            setIconImage(icon);
        } catch (IOException | NullPointerException e) {
            System.out.println("Could not load application icon.");
        }

        MainPanel mainPanel = new MainPanel(serverIp, serverPort);
        add(mainPanel);

        // Resize frame to fit the preferred size of the mainPanel
        pack();

        // Center the frame on the screen
        setLocationRelativeTo(null);

        // Initialize Input Handler
        keyInputHandler = new KeyInputHandler(mainPanel);

        loadBindingsFromFile();

        mainPanel.setKeyInputHandler(keyInputHandler);
        setFocusable(true);
        addKeyListener(keyInputHandler);
    }

    public MainFrame() { this(SERVER_IP, SERVER_PORT); }

    /**
     * Attempts to load custom key bindings from the XML configuration file.
     * If loading fails (e.g., file doesn't exist), it applies default bindings and saves them.
     */
    private void loadBindingsFromFile() {
        // Try to load from XML
        boolean loaded = KeyBindingsManager.loadBindings(keyInputHandler);

        // If file didn't exist or failed to load, set defaults and create the file
        if (!loaded) {
            setDefaultBindings();
            KeyBindingsManager.saveBindings(keyInputHandler);
        }
    }

    /**
     * Sets the hardcoded default key bindings.
     * <p>
     * Player 1 uses WASD + JKL (Action keys).
     * Player 2 uses Arrow Keys + Numpad.
     * </p>
     */
    private void setDefaultBindings() {
        keyInputHandler.bindGoBackToMenuKey(KeyEvent.VK_ENTER);

        // Player 1 Defaults
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_A, InputAction.MOVE_LEFT, true);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_D, InputAction.MOVE_RIGHT, true);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_S, InputAction.SOFT_DROP, true);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_W, InputAction.HARD_DROP, true);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_J, InputAction.ROTATE_LEFT, true);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_K, InputAction.ROTATE_RIGHT, true);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_L, InputAction.FLIP, true);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_SHIFT, InputAction.HOLD, true);

        // Player 2 Defaults
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_LEFT, InputAction.MOVE_LEFT, false);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_RIGHT, InputAction.MOVE_RIGHT, false);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_DOWN, InputAction.SOFT_DROP, false);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_UP, InputAction.HARD_DROP, false);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_NUMPAD1, InputAction.ROTATE_LEFT, false);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_NUMPAD2, InputAction.ROTATE_RIGHT, false);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_NUMPAD3, InputAction.FLIP, false);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_CONTROL, InputAction.HOLD, false);
    }
}