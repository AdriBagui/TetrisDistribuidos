package src;

import src.menus.StartMenuPanel;
import src.tetris.panels.OnlineTwoPlayerTetrisPanel;
import src.tetris.panels.LocalTwoPlayerTetrisPanel;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private static final String START_MENU_PANEL = "Start Menu Screen";
    private static final String LOCAL_TETRIS_PANEL = "Local Tetris Screen";
    private static final String ONLINE_TETRIS_PANEL = "Online Tetris Screen";

    private StartMenuPanel startMenuPanel;
    private LocalTwoPlayerTetrisPanel localTwoPlayerTetrisPanel;
    private OnlineTwoPlayerTetrisPanel onlineTwoPlayerTetrisPanel;
    private CardLayout cardLayout;

    public MainPanel(CardLayout cardLayout) {
        super(cardLayout);

        this.cardLayout = cardLayout;

        startMenuPanel = new StartMenuPanel(this);
        // TODO: Probably have to add more menus to create room and join room
        localTwoPlayerTetrisPanel = new LocalTwoPlayerTetrisPanel(this);
        onlineTwoPlayerTetrisPanel = new OnlineTwoPlayerTetrisPanel(this);

        add(startMenuPanel, START_MENU_PANEL);
        add(localTwoPlayerTetrisPanel, LOCAL_TETRIS_PANEL);
        add(onlineTwoPlayerTetrisPanel, ONLINE_TETRIS_PANEL);

        cardLayout.show(this, START_MENU_PANEL);
    }

    public void startLocalGame() {
        cardLayout.show(this, LOCAL_TETRIS_PANEL);
        localTwoPlayerTetrisPanel.startGame();
    }

    public void startOnlineGame() {
        cardLayout.show(this, ONLINE_TETRIS_PANEL);
        onlineTwoPlayerTetrisPanel.startGame();
    }

    public void endGame() {
        cardLayout.show(this, START_MENU_PANEL);
    }
}
