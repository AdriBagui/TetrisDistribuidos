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
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // SwingUtilities.invokeLater ensures that the GUI creation code runs on the
        // Event Dispatch Thread (EDT). This is crucial for Swing thread safety,
        // preventing concurrency issues between the main thread and the AWT event handler.
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}