package client.userInterface.panels;

import tetris.tetrominoes.Tetromino;
import tetris.tetrominoes.TetrominoCell;
import tetris.tetrominoes.TetrominoFactory;
import tetris.tetrominoes.TetrominoType;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import static client.userInterface.panels.MainPanel.*;

/**
 * A panel displayed while the client is negotiating a connection with the server.
 * <p>
 * This screen shows a rotating random Tetromino and status messages.
 * The Tetromino is selected once per session and rotates around its visual center.
 * </p>
 */
public class WaitingOpponentPanel extends JPanel {
    private final JLabel roomIdLabel;
    private final JLabel waitingLabel;

    // Animation state
    private Timer animationTimer;
    private Tetromino currentTetromino;
    private double currentAngle = 0;
    private final Random random = new Random();

    // Configuration
    private static final int ANIMATION_DELAY_MS = 16; // ~60 FPS
    private static final double ROTATION_SPEED = Math.toRadians(2); // 2 degrees per frame

    public WaitingOpponentPanel() {
        setLayout(new GridBagLayout());
        setBackground(BACKGROUND_COLOR); //

        // --- Labels Setup ---
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Reserve top space for the animation so text doesn't overlap
        gbc.insets = new Insets(150, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Waiting message
        waitingLabel = new JLabel("Connecting to server...", SwingConstants.CENTER);
        waitingLabel.setFont(HEADER_FONT); //
        waitingLabel.setForeground(TEXT_COLOR);

        gbc.gridy = 0;
        add(waitingLabel, gbc);

        // Room ID Label
        roomIdLabel = new JLabel("", SwingConstants.CENTER);
        roomIdLabel.setFont(MEDIUM_MESSAGE_FONT); //
        roomIdLabel.setForeground(TEXT_COLOR);
        roomIdLabel.setVisible(false);

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 10, 0);
        add(roomIdLabel, gbc);

        // Initialize timer but do not start it yet
        animationTimer = new Timer(ANIMATION_DELAY_MS, e -> {
            currentAngle += ROTATION_SPEED;
            // Wrap angle to keep numbers small, though not strictly necessary
            if (currentAngle >= Math.PI * 2) {
                currentAngle -= Math.PI * 2;
            }
            repaint();
        });
    }

    /**
     * Starts the loading animation.
     * Should be called when the panel becomes visible.
     */
    public void startAnimation() {
        if (animationTimer.isRunning()) return;

        // Pick a new random tetromino for this specific waiting session
        pickRandomTetromino();
        currentAngle = 0;
        animationTimer.start();
    }

    /**
     * Stops the loading animation to save resources.
     * Should be called when the panel is hidden (game starts or back to menu).
     */
    public void stopAnimation() {
        if (animationTimer.isRunning()) {
            animationTimer.stop();
        }
    }

    private void pickRandomTetromino() {
        TetrominoType[] types = TetrominoType.values();
        TetrominoType randomType = types[random.nextInt(types.length)];
        // Create dummy tetromino at 0,0 to get shape/color data
        currentTetromino = TetrominoFactory.createTetromino(randomType, 0, 0, 0, 0, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (currentTetromino == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- 1. Calculate the "Visual Center" ---
        // This ensures S, Z, and I rotate around their visible mass, not the grid corner.
        Rectangle2D visibleBounds = getVisibleBounds(currentTetromino);

        double centerX = getWidth() / 2.0;
        double centerY = (getHeight() / 2.0) - 50; // Shift up slightly

        // --- 2. Apply Transformations ---
        AffineTransform oldTransform = g2.getTransform();

        // Move origin to the panel's visual center
        g2.translate(centerX, centerY);

        // Rotate the canvas
        g2.rotate(currentAngle);

        // Offset so the Tetromino's *visual center* aligns with the origin
        // visibleBounds.getX() handles empty columns on the left
        // visibleBounds.getWidth() / 2.0 finds the midpoint of the block mass
        double offsetX = (visibleBounds.getX() + visibleBounds.getWidth() / 2.0);
        double offsetY = (visibleBounds.getY() + visibleBounds.getHeight() / 2.0);

        g2.translate(-offsetX, -offsetY);

        // --- 3. Draw ---
        boolean[][] shape = currentTetromino.getShape();
        Color c = currentTetromino.getColor();

        for (int r = 0; r < shape.length; r++) {
            for (int col = 0; col < shape[r].length; col++) {
                if (shape[r][col]) {
                    TetrominoCell.drawClassicCell(g2, col * CELL_SIZE, r * CELL_SIZE, c); //
                }
            }
        }

        g2.setTransform(oldTransform);
    }

    /**
     * Calculates the bounding box of the actual filled blocks in pixels.
     * Essential for centering S, Z, and I pieces correctly.
     */
    private Rectangle2D getVisibleBounds(Tetromino t) {
        boolean[][] shape = t.getShape();
        int minCol = Integer.MAX_VALUE;
        int maxCol = Integer.MIN_VALUE;
        int minRow = Integer.MAX_VALUE;
        int maxRow = Integer.MIN_VALUE;

        boolean found = false;

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c]) {
                    if (c < minCol) minCol = c;
                    if (c > maxCol) maxCol = c;
                    if (r < minRow) minRow = r;
                    if (r > maxRow) maxRow = r;
                    found = true;
                }
            }
        }

        if (!found) return new Rectangle2D.Double(0, 0, 0, 0);

        // Calculate pixel dimensions relative to the shape's 0,0 origin
        double x = minCol * CELL_SIZE;
        double y = minRow * CELL_SIZE;
        double w = (maxCol - minCol + 1) * CELL_SIZE;
        double h = (maxRow - minRow + 1) * CELL_SIZE;

        return new Rectangle2D.Double(x, y, w, h);
    }

    // --- Message Updates ---

    public void setMessage(String message) {
        waitingLabel.setText(message);
    }

    public void setRoomId(int roomId) {
        roomIdLabel.setText("Room ID: " + roomId);
    }

    public void setRoomIdVisibility(boolean visible) {
        roomIdLabel.setVisible(visible);
    }
}