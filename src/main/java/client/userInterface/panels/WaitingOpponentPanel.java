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
 * This screen shows a rotating random Tetromino.
 * J and L pieces rotate around their long bar; others rotate around their visual center.
 * </p>
 */
public class WaitingOpponentPanel extends JPanel {
    private final JLabel roomIdLabel;
    private final JLabel waitingLabel;

    // Animation state
    private final Timer animationTimer;
    private Tetromino currentTetromino;
    private double currentAngle = 0;
    private final Random random = new Random();

    // Configuration
    private static final int ANIMATION_DELAY_MS = 16; // ~60 FPS
    private static final double ROTATION_SPEED = Math.toRadians(2); // 2 degrees per frame

    public WaitingOpponentPanel() {
        setLayout(new GridBagLayout());
        setBackground(BACKGROUND_COLOR);

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
        waitingLabel.setFont(HEADER_FONT);
        waitingLabel.setForeground(TEXT_COLOR);

        gbc.gridy = 0;
        add(waitingLabel, gbc);

        // Room ID Label
        roomIdLabel = new JLabel("", SwingConstants.CENTER);
        roomIdLabel.setFont(MEDIUM_MESSAGE_FONT);
        roomIdLabel.setForeground(TEXT_COLOR);
        roomIdLabel.setVisible(false);

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 10, 0);
        add(roomIdLabel, gbc);

        // Initialize timer (starts only when panel is shown via mainPanel)
        animationTimer = new Timer(ANIMATION_DELAY_MS, e -> {
            currentAngle += ROTATION_SPEED;
            if (currentAngle >= Math.PI * 2) {
                currentAngle -= Math.PI * 2;
            }
            repaint();
        });
    }

    /**
     * Starts the loading animation.
     * Called by MainPanel when switching to this view.
     */
    public void startAnimation() {
        if (animationTimer.isRunning()) return;

        pickRandomTetromino();
        currentAngle = 0;
        animationTimer.start();
    }

    /**
     * Stops the loading animation.
     * Called by MainPanel when leaving this view.
     */
    public void stopAnimation() {
        if (animationTimer.isRunning()) {
            animationTimer.stop();
        }
    }

    private void pickRandomTetromino() {
        TetrominoType[] types = TetrominoType.values();
        TetrominoType randomType = types[random.nextInt(types.length)];
        // Create dummy tetromino at 0,0 (Rotation 0)
        currentTetromino = TetrominoFactory.createTetromino(randomType, 0, 0, 0, 0, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (currentTetromino == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- Calculate offsets ---
        double centerX = getWidth() / 2.0;
        double centerY = (getHeight() / 2.0) - 50;
        double pivotOffsetX, pivotOffsetY;

        pivotOffsetY = switch (currentTetromino.getType()) {
            case J, L, T -> {
                pivotOffsetX = 1.5 * CELL_SIZE;
                yield 1.5 * CELL_SIZE;
            }
            case I -> {
                pivotOffsetX = 2 * CELL_SIZE;
                yield 1.5 * CELL_SIZE;
            }
            case Z, S -> {
                pivotOffsetX = 1.5 * CELL_SIZE;
                yield CELL_SIZE;
            }
            case O -> {
                pivotOffsetX = CELL_SIZE;
                yield CELL_SIZE;
            }
        };

        // --- Transformation Stack ---
        AffineTransform oldTransform = g2.getTransform();

        // 1. Move origin to panel center
        g2.translate(centerX, centerY);

        // 2. Rotate
        g2.rotate(currentAngle);

        // 3. Shift back so the pivot point aligns with the origin
        g2.translate(-pivotOffsetX, -pivotOffsetY);

        // --- Draw ---
        boolean[][] shape = currentTetromino.getShape();
        Color c = currentTetromino.getColor();

        for (int r = 0; r < shape.length; r++) {
            for (int col = 0; col < shape[r].length; col++) {
                if (shape[r][col]) {
                    TetrominoCell.drawClassicCell(g2, col * CELL_SIZE, r * CELL_SIZE, c);
                }
            }
        }

        g2.setTransform(oldTransform);
    }

    /**
     * Calculates the bounding box of the actual filled blocks.
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

        return new Rectangle2D.Double(
                minCol * CELL_SIZE,
                minRow * CELL_SIZE,
                (maxCol - minCol + 1) * CELL_SIZE,
                (maxRow - minRow + 1) * CELL_SIZE
        );
    }

    // --- Message Updates ---
    public void setMessage(String message) { waitingLabel.setText(message); }
    public void setRoomId(int roomId) { roomIdLabel.setText("Room ID: " + roomId); }
    public void setRoomIdVisibility(boolean visible) { roomIdLabel.setVisible(visible); }
}