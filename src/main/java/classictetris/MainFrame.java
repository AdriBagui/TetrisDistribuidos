package classictetris;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

// ==========================================
// CLASS: MainFrame
// Handles the window, game loop timer, and input
// ==========================================
public class MainFrame extends JFrame {
    private Canvas gameCanvas;
    private Timer gameTimer;

    // Shared sequence of blocks to ensure fairness
    private ArrayList<Integer> sharedBlockQueue;
    private Random random;

    public MainFrame() {
        setTitle("2-Player Java Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Initialize shared block logic
        sharedBlockQueue = new ArrayList<>();
        random = new Random();

        // Create the view
        gameCanvas = new Canvas(this);
        add(gameCanvas);
        pack();
        setLocationRelativeTo(null);

        // Game Loop (approx 60fps for smoothness, logic updates slower)
        gameTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameCanvas.isGameOver()) {
                    gameCanvas.updateBoards();
                    gameCanvas.repaint();
                }
            }
        });
        gameTimer.start();

        // Input Handling
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameCanvas.isGameOver()) {
                    if (e.getKeyCode() == KeyEvent.VK_R) {
                        restartGame();
                    }
                    return;
                }

                // Player 1 Controls (WASD)
                Board b1 = gameCanvas.getP1Board();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A -> b1.moveLeft();
                    case KeyEvent.VK_D -> b1.moveRight();
                    case KeyEvent.VK_W -> b1.rotate();
                    case KeyEvent.VK_S -> b1.drop();
                }

                // Player 2 Controls (Arrows)
                Board b2 = gameCanvas.getP2Board();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> b2.moveLeft();
                    case KeyEvent.VK_RIGHT -> b2.moveRight();
                    case KeyEvent.VK_UP -> b2.rotate();
                    case KeyEvent.VK_DOWN -> b2.drop();
                }
                gameCanvas.repaint();
            }
        });
    }

    public void restartGame() {
        sharedBlockQueue.clear();
        gameCanvas.reset();
        gameTimer.start();
    }

    // Ensures both players get the exact same sequence of blocks
    public int getBlockTypeForIndex(int index) {
        while (sharedBlockQueue.size() <= index) {
            sharedBlockQueue.add(random.nextInt(7)); // 0-6 for 7 block types
        }
        return sharedBlockQueue.get(index);
    }
}
