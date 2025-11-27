package tetris;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import static tetris.Config.FPS;
import static tetris.Config.MILIS_PER_FRAME;

// ==========================================
// CLASS: MainFrame
// Handles the window, game loop timer, and input
// ==========================================
public class MainFrame extends JFrame {
    private GamePanel gamePanel;

    public MainFrame() {
        setTitle("2-Player Java Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Create the view
        gamePanel = new GamePanel(this);
        add(gamePanel);
        pack();
        setLocationRelativeTo(null);

        // Input Handling
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gamePanel.isGameOver()) {
                    if (e.getKeyCode() == KeyEvent.VK_R) {
                        gamePanel.restartGame();
                    }
                    return;
                }

                // Player 1 Controls (WASD)
                Board b1 = gamePanel.getP1Board();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A -> b1.moveLeftPressed();
                    case KeyEvent.VK_D -> b1.moveRightPressed();
                    case KeyEvent.VK_W -> b1.dropPressed();
                    case KeyEvent.VK_S -> b1.moveDownPressed();
                    case KeyEvent.VK_J -> b1.rotateLeftPressed();
                    case KeyEvent.VK_K -> b1.rotateRightPressed();
                    case KeyEvent.VK_L -> b1.flipPressed();
                    case KeyEvent.VK_SHIFT ->  b1.hold();
                }

                // Player 2 Controls (Arrows)
                Board b2 = gamePanel.getP2Board();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> b2.moveLeftPressed();
                    case KeyEvent.VK_RIGHT -> b2.moveRightPressed();
                    case KeyEvent.VK_UP -> b2.rotateRightPressed();
                    case KeyEvent.VK_DOWN -> b2.moveDownPressed();
                    case KeyEvent.VK_PERIOD ->  b2.hold();
                }
                gamePanel.repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Player 1 Controls (WASD)
                Board b1 = gamePanel.getP1Board();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A -> b1.moveLeftReleased();
                    case KeyEvent.VK_D -> b1.moveRightReleased();
                    case KeyEvent.VK_W -> b1.dropReleased();
                    case KeyEvent.VK_S -> b1.moveDownReleased();
                    case KeyEvent.VK_J -> b1.rotateLeftReleased();
                    case KeyEvent.VK_K -> b1.rotateRightReleased();
                    case KeyEvent.VK_L -> b1.flipReleased();
                }

                // Player 2 Controls (Arrows)
                Board b2 = gamePanel.getP2Board();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> b2.moveLeftReleased();
                    case KeyEvent.VK_RIGHT -> b2.moveRightReleased();
                    case KeyEvent.VK_UP -> b2.rotateRightReleased();
                    case KeyEvent.VK_DOWN -> b2.moveDownReleased();
                }
                gamePanel.repaint();
            }
        });
    }
}
