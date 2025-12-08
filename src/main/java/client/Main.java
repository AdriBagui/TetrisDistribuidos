package client;

import client.userInterface.MainFrame;

import javax.swing.*;

/**
 * The entry point for the Tetris Distribuidos client application.
 */
public class Main {
    /**
     * The main method that launches the GUI.
     *
     * @param args Command line arguments if used the firs arg is the server ip to connect to and the second the server port.
     */
    public static void main(String[] args) {
        // SwingUtilities.invokeLater ensures that the GUI creation code runs on the
        // Event Dispatch Thread (EDT). This is crucial for Swing thread safety,
        // preventing concurrency issues between the main thread and the AWT event handler.
        SwingUtilities.invokeLater(() -> {
            MainFrame frame;

            if (args != null && args.length > 1) frame = new MainFrame(args[0], Integer.parseInt(args[1]));
            else frame = new MainFrame();

            frame.setVisible(true);
        });
    }
}