package src;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private MainPanel mainPanel;

    public MainFrame() {
        setTitle("2-Player Java Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        CardLayout cardLayout = new CardLayout();

        mainPanel = new MainPanel(cardLayout);
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }
}
