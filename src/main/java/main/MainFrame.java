package main;

import tetris.keyMaps.InputAction;
import tetris.keyMaps.KeyBindingsManager;
import tetris.keyMaps.KeyInputHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

// El MainFrame es el frame (la ventana) de la aplicación
public class MainFrame extends JFrame {
    private MainPanel mainPanel;
    private KeyInputHandler keyInputHandler;

    public MainFrame() {
        // Cambia el título que se ve en la barra de arriba de la ventana
        setTitle("Tetris Distribuidos");
        // Cambia el comportamiento de presionar la x de la ventana para que se cierre la aplicación
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Impide que se pueda modificar el tamaño de la ventana
        setResizable(false);

        try {
            BufferedImage icon = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/icon.png")));
            setIconImage(icon);
        } catch (IOException | NullPointerException e) {
            System.out.println("Could not load application icon.");
        }

        mainPanel = new MainPanel();
        add(mainPanel);

        // Hace que el frame se adecue al tamaño de su contenido
        pack();

        // Centra el frame en el centro de la pantalla
        setLocationRelativeTo(null);

        keyInputHandler = new KeyInputHandler(mainPanel);

        loadBindingsFromFile();

        mainPanel.setKeyInputHandler(keyInputHandler);
        setFocusable(true);
        addKeyListener(keyInputHandler);
    }

    private void loadBindingsFromFile() {
        // Try to load from XML
        boolean loaded = KeyBindingsManager.loadBindings(keyInputHandler);

        // If file didn't exist or failed to load, set defaults and create the file
        if (!loaded) {
            setDefaultBindings();
            KeyBindingsManager.saveBindings(keyInputHandler);
        }
    }

    private void setDefaultBindings() {
        keyInputHandler.bindGoBackToMenuKey(KeyEvent.VK_ENTER);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_A, InputAction.MOVE_LEFT, true);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_D, InputAction.MOVE_RIGHT, true);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_S, InputAction.SOFT_DROP, true);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_W, InputAction.HARD_DROP, true);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_J, InputAction.ROTATE_LEFT, true);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_K, InputAction.ROTATE_RIGHT, true);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_L, InputAction.FLIP, true);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_SHIFT, InputAction.HOLD, true);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_LEFT, InputAction.MOVE_LEFT, false);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_RIGHT, InputAction.MOVE_RIGHT, false);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_DOWN, InputAction.SOFT_DROP, false);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_UP, InputAction.HARD_DROP, false);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_NUMPAD1, InputAction.ROTATE_LEFT, false);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_NUMPAD2, InputAction.ROTATE_RIGHT, false);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_NUMPAD3, InputAction.FLIP, false);
        keyInputHandler.bindKeyToPlayerAction(KeyEvent.VK_CONTROL, InputAction.HOLD, false);

        // TODO: save key bindings
    }
}
