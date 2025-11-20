package classictetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class TwoPlayerTetris {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

    // ==========================================
    // CLASS: MainFrame
    // Handles the window, game loop timer, and input
    // ==========================================
    static class MainFrame extends JFrame {
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

    // ==========================================
    // CLASS: Canvas
    // Handles drawing both boards and scores
    // ==========================================
    static class Canvas extends JPanel {
        private MainFrame mainFrame;
        private Board p1Board;
        private Board p2Board;
        private boolean gameOver = false;

        public static final int CELL_SIZE = 25;
        private static final int BOARD_WIDTH_PIXELS = 10 * CELL_SIZE;
        private static final int BOARD_HEIGHT_PIXELS = 20 * CELL_SIZE;

        public Canvas(MainFrame frame) {
            this.mainFrame = frame;
            setPreferredSize(new Dimension(800, 600));
            setBackground(Color.DARK_GRAY);

            // Initialize Boards
            p1Board = new Board(1, frame);
            p2Board = new Board(2, frame);
        }

        public void reset() {
            gameOver = false;
            p1Board = new Board(1, mainFrame);
            p2Board = new Board(2, mainFrame);
            repaint();
        }

        public void updateBoards() {
            if (p1Board.isAlive()) p1Board.update();
            if (p2Board.isAlive()) p2Board.update();

            if (!p1Board.isAlive() && !p2Board.isAlive()) {
                gameOver = true;
            }
        }

        public boolean isGameOver() { return gameOver; }
        public Board getP1Board() { return p1Board; }
        public Board getP2Board() { return p2Board; }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // Draw Player 1 Area
            drawBoard(g2, p1Board, 50, 50);

            // Draw Player 2 Area
            drawBoard(g2, p2Board, 450, 50);

            // Draw Controls/Info
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("Player 1 (WASD)", 50, 40);
            g2.drawString("Player 2 (Arrows)", 450, 40);

            if (gameOver) {
                g2.setColor(Color.RED);
                g2.setFont(new Font("Arial", Font.BOLD, 40));
                String msg = "GAME OVER";
                String winner = "";
                if (p1Board.getScore() > p2Board.getScore()) winner = "Player 1 Wins!";
                else if (p2Board.getScore() > p1Board.getScore()) winner = "Player 2 Wins!";
                else winner = "It's a Tie!";

                g2.drawString(msg, 300, 250);
                g2.drawString(winner, 270, 300);
                g2.setFont(new Font("Arial", Font.PLAIN, 20));
                g2.drawString("Press 'R' to Restart", 310, 350);
            }
        }

        private void drawBoard(Graphics2D g, Board board, int offsetX, int offsetY) {
            // Draw Grid Background
            g.setColor(Color.BLACK);
            g.fillRect(offsetX, offsetY, BOARD_WIDTH_PIXELS, BOARD_HEIGHT_PIXELS);

            // Draw Static Blocks
            int[][] grid = board.getGrid();
            for (int r = 0; r < 20; r++) {
                for (int c = 0; c < 10; c++) {
                    if (grid[r][c] != 0) {
                        drawCell(g, offsetX + c * CELL_SIZE, offsetY + r * CELL_SIZE, grid[r][c]);
                    }
                }
            }

            // Draw Current Falling Block
            Block curr = board.getCurrentBlock();
            if (curr != null) {
                drawBlock(g, curr, offsetX, offsetY);
            }

            // Draw Border
            g.setColor(Color.WHITE);
            g.drawRect(offsetX, offsetY, BOARD_WIDTH_PIXELS, BOARD_HEIGHT_PIXELS);

            // Draw Score
            g.setColor(Color.WHITE);
            g.drawString("Score: " + board.getScore(), offsetX, offsetY + BOARD_HEIGHT_PIXELS + 30);

            // Draw Next Piece Preview
            g.drawString("Next:", offsetX + BOARD_WIDTH_PIXELS + 20, offsetY + 50);
            Block next = board.getNextBlock();
            if (next != null) {
                // Create a dummy version at 0,0 for preview rendering
                int previewX = offsetX + BOARD_WIDTH_PIXELS + 30;
                int previewY = offsetY + 60;
                drawBlockPreview(g, next, previewX, previewY);
            }
        }

        private void drawBlock(Graphics2D g, Block b, int offX, int offY) {
            for (Point p : b.getPoints()) {
                int x = offX + (b.getX() + p.x) * CELL_SIZE;
                int y = offY + (b.getY() + p.y) * CELL_SIZE;
                drawCell(g, x, y, b.getColorIndex());
            }
        }

        private void drawBlockPreview(Graphics2D g, Block b, int x, int y) {
            for (Point p : b.getPoints()) {
                drawCell(g, x + p.x * CELL_SIZE, y + p.y * CELL_SIZE, b.getColorIndex());
            }
        }

        private void drawCell(Graphics2D g, int x, int y, int colorIndex) {
            Color c = getColorForIndex(colorIndex);
            g.setColor(c);
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            g.setColor(c.darker());
            g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
        }

        private Color getColorForIndex(int i) {
            return switch (i) {
                case 1 -> Color.CYAN;   // I
                case 2 -> Color.BLUE;   // J
                case 3 -> Color.ORANGE; // L
                case 4 -> Color.YELLOW; // O
                case 5 -> Color.GREEN;  // S
                case 6 -> Color.MAGENTA;// T
                case 7 -> Color.RED;    // Z
                default -> Color.GRAY;
            };
        }
    }

    // ==========================================
    // CLASS: Board
    // Logic for a single player's 10x20 grid
    // ==========================================
    static class Board {
        private int[][] grid = new int[20][10]; // 20 rows, 10 cols
        private Block currentBlock;
        private Block nextBlock;
        private int score = 0;
        private boolean isAlive = true;
        private int playerID; // 1 or 2
        private MainFrame mainFrame;
        private int blockCounter = 0; // Which block index we are on

        public Board(int playerID, MainFrame frame) {
            this.playerID = playerID;
            this.mainFrame = frame;
            spawnBlock(); // Initialize first block
        }

        private void spawnBlock() {
            if (nextBlock == null) {
                // First spawn
                currentBlock = createBlock(mainFrame.getBlockTypeForIndex(blockCounter++));
                nextBlock = createBlock(mainFrame.getBlockTypeForIndex(blockCounter++));
            } else {
                currentBlock = nextBlock;
                nextBlock = createBlock(mainFrame.getBlockTypeForIndex(blockCounter++));
            }

            // Set Start Position
            currentBlock.setX(3);
            currentBlock.setY(0);

            // Game Over check immediately on spawn
            if (checkCollision(currentBlock.getX(), currentBlock.getY(), currentBlock.getShape())) {
                isAlive = false;
            }
        }

        public void update() {
            if (!isAlive) return;

            if (!checkCollision(currentBlock.getX(), currentBlock.getY() + 1, currentBlock.getShape())) {
                currentBlock.moveDown();
            } else {
                lockBlock();
                clearLines();
                spawnBlock();
            }
        }

        // Controls
        public void moveLeft() {
            if (isAlive && !checkCollision(currentBlock.getX() - 1, currentBlock.getY(), currentBlock.getShape())) {
                currentBlock.moveLeft();
            }
        }

        public void moveRight() {
            if (isAlive && !checkCollision(currentBlock.getX() + 1, currentBlock.getY(), currentBlock.getShape())) {
                currentBlock.moveRight();
            }
        }

        public void drop() {
            if (!isAlive) return;
            while(!checkCollision(currentBlock.getX(), currentBlock.getY() + 1, currentBlock.getShape())) {
                currentBlock.moveDown();
            }
            // Force update next tick will lock it
        }

        public void rotate() {
            if (!isAlive) return;
            int[][] rotated = currentBlock.getNextRotation();
            // Basic Wall kick logic (if rotation hits wall, try moving left/right)
            if (!checkCollision(currentBlock.getX(), currentBlock.getY(), rotated)) {
                currentBlock.rotate();
            } else if (!checkCollision(currentBlock.getX() - 1, currentBlock.getY(), rotated)) {
                currentBlock.moveLeft();
                currentBlock.rotate();
            } else if (!checkCollision(currentBlock.getX() + 1, currentBlock.getY(), rotated)) {
                currentBlock.moveRight();
                currentBlock.rotate();
            }
        }

        private void lockBlock() {
            for (Point p : currentBlock.getPoints()) {
                int x = currentBlock.getX() + p.x;
                int y = currentBlock.getY() + p.y;
                if (y >= 0 && y < 20 && x >= 0 && x < 10) {
                    grid[y][x] = currentBlock.getColorIndex();
                }
            }
        }

        private void clearLines() {
            int linesCleared = 0;
            for (int r = 0; r < 20; r++) {
                boolean full = true;
                for (int c = 0; c < 10; c++) {
                    if (grid[r][c] == 0) {
                        full = false;
                        break;
                    }
                }
                if (full) {
                    linesCleared++;
                    // Shift down
                    for (int y = r; y > 0; y--) {
                        System.arraycopy(grid[y - 1], 0, grid[y], 0, 10);
                    }
                    // Clear top
                    for (int c = 0; c < 10; c++) grid[0][c] = 0;
                }
            }
            // NES Scoring: 40, 100, 300, 1200
            switch (linesCleared) {
                case 1 -> score += 40;
                case 2 -> score += 100;
                case 3 -> score += 300;
                case 4 -> score += 1200;
            }
        }

        private boolean checkCollision(int x, int y, int[][] shape) {
            for (int r = 0; r < shape.length; r++) {
                for (int c = 0; c < shape[r].length; c++) {
                    if (shape[r][c] != 0) {
                        int boardX = x + c;
                        int boardY = y + r;

                        // Check boundaries
                        if (boardX < 0 || boardX >= 10 || boardY >= 20) return true;

                        // Check grid occupied (ignore above board)
                        if (boardY >= 0 && grid[boardY][boardX] != 0) return true;
                    }
                }
            }
            return false;
        }

        public int[][] getGrid() { return grid; }
        public int getScore() { return score; }
        public Block getCurrentBlock() { return currentBlock; }
        public Block getNextBlock() { return nextBlock; }
        public boolean isAlive() { return isAlive; }

        // Factory for blocks
        private Block createBlock(int type) {
            return switch (type) {
                case 0 -> new LineBlock();
                case 1 -> new JBlock();
                case 2 -> new LBlock();
                case 3 -> new SquareBlock();
                case 4 -> new SBlock();
                case 5 -> new TBlock();
                case 6 -> new ZBlock();
                default -> new SquareBlock();
            };
        }
    }

    // ==========================================
    // ABSTRACT CLASS: Block
    // Base class for all Tetris shapes
    // ==========================================
    static abstract class Block {
        protected int[][] shape;
        protected int x, y;
        protected int colorIndex;

        public Block() {
            initShape();
        }

        protected abstract void initShape();

        public int[][] getShape() { return shape; }
        public int getX() { return x; }
        public int getY() { return y; }
        public int getColorIndex() { return colorIndex; }

        public void setX(int x) { this.x = x; }
        public void setY(int y) { this.y = y; }

        public void moveDown() { y++; }
        public void moveLeft() { x--; }
        public void moveRight() { x++; }

        public void rotate() {
            shape = getNextRotation();
        }

        // Returns what the shape would look like if rotated
        public int[][] getNextRotation() {
            int rows = shape.length;
            int cols = shape[0].length;
            int[][] rotated = new int[cols][rows];
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    rotated[c][rows - 1 - r] = shape[r][c];
                }
            }
            return rotated;
        }

        // Helper to get actual occupied points
        public ArrayList<Point> getPoints() {
            ArrayList<Point> points = new ArrayList<>();
            for(int r=0; r<shape.length; r++) {
                for(int c=0; c<shape[r].length; c++) {
                    if(shape[r][c] != 0) {
                        points.add(new Point(c, r));
                    }
                }
            }
            return points;
        }
    }

    // ==========================================
    // BLOCK SUBCLASSES
    // ==========================================

    static class LineBlock extends Block { // I Piece
        @Override protected void initShape() {
            shape = new int[][] { {1, 1, 1, 1} };
            colorIndex = 1;
        }
    }

    static class JBlock extends Block { // J Piece
        @Override protected void initShape() {
            shape = new int[][] {
                    {1, 0, 0},
                    {1, 1, 1}
            };
            colorIndex = 2;
        }
    }

    static class LBlock extends Block { // L Piece
        @Override protected void initShape() {
            shape = new int[][] {
                    {0, 0, 1},
                    {1, 1, 1}
            };
            colorIndex = 3;
        }
    }

    static class SquareBlock extends Block { // O Piece
        @Override protected void initShape() {
            shape = new int[][] {
                    {1, 1},
                    {1, 1}
            };
            colorIndex = 4;
        }
    }

    static class SBlock extends Block { // S Piece
        @Override protected void initShape() {
            shape = new int[][] {
                    {0, 1, 1},
                    {1, 1, 0}
            };
            colorIndex = 5;
        }
    }

    static class TBlock extends Block { // T Piece
        @Override protected void initShape() {
            shape = new int[][] {
                    {0, 1, 0},
                    {1, 1, 1}
            };
            colorIndex = 6;
        }
    }

    static class ZBlock extends Block { // Z Piece
        @Override protected void initShape() {
            shape = new int[][] {
                    {1, 1, 0},
                    {0, 1, 1}
            };
            colorIndex = 7;
        }
    }
}
