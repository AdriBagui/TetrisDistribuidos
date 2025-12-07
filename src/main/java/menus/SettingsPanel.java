package menus;

import main.MainPanel;
import tetris.keyMaps.InputAction;
import tetris.keyMaps.KeyInputHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class SettingsPanel extends JPanel {
    private MainPanel mainPanel;
    private KeyInputHandler keyInputHandler;
    private JPanel contentPanel;

    // Temporary storage for changes before saving
    private Map<InputAction, Integer> p1PendingChanges = new HashMap<>();
    private Map<InputAction, Integer> p2PendingChanges = new HashMap<>();
    private int pendingGoBackKey = -1;

    private boolean isListeningForKey = false;

    // UI Constants
    private static final Color BACKGROUND_COLOR = new Color(22, 22, 22);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public SettingsPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;

        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        // --- CENTER: SCROLLABLE CONTENT ---
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null); // Remove white border
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        // SPEED UP SCROLLING
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // APPLY CUSTOM MODERN SCROLLBAR UI
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        // Make the scrollbar purely an overlay or thinner if desired
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));

        add(scrollPane, BorderLayout.CENTER);

        // --- BOTTOM: FOOTER (SAVE/CANCEL) ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        ModernButton saveBtn = new ModernButton("SAVE & EXIT");
        saveBtn.addActionListener(e -> saveAndExit());

        ModernButton cancelBtn = new ModernButton("CANCEL");
        cancelBtn.addActionListener(e -> {
            resetPendingChanges();
            mainPanel.backToStartMenu();
        });

        footerPanel.add(cancelBtn);
        footerPanel.add(saveBtn);
        add(footerPanel, BorderLayout.SOUTH);
    }

    public void setKeyInputHandler(KeyInputHandler keyInputHandler) {
        this.keyInputHandler = keyInputHandler;
    }

    /**
     * Rebuilds the UI based on current bindings.
     */
    public void refresh() {
        if (keyInputHandler == null) return;

        isListeningForKey = false;
        contentPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        addSectionHeader("General", gbc);
        addKeyBindingRow("Back to Menu", null, false, true, gbc);
        addSeparator(gbc);

        addSectionHeader("Player 1 Controls", gbc);
        addControlsSection(true, gbc);
        addSeparator(gbc);

        addSectionHeader("Player 2 Controls", gbc);
        addControlsSection(false, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        contentPanel.add(Box.createGlue(), gbc);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void addControlsSection(boolean isPlayer1, GridBagConstraints gbc) {
        InputAction[] actions = {
                InputAction.MOVE_LEFT, InputAction.MOVE_RIGHT,
                InputAction.SOFT_DROP, InputAction.HARD_DROP,
                InputAction.ROTATE_LEFT, InputAction.ROTATE_RIGHT,
                InputAction.FLIP, InputAction.HOLD
        };

        for (InputAction action : actions) {
            addKeyBindingRow(formatActionName(action), action, isPlayer1, false, gbc);
        }
    }

    private void addKeyBindingRow(String labelText, InputAction action, boolean isPlayer1, boolean isGlobal, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 30, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(LABEL_FONT);
        lbl.setForeground(TEXT_COLOR);
        contentPanel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(5, 10, 5, 30);
        gbc.anchor = GridBagConstraints.EAST;

        JButton bindButton = createBindButton(action, isPlayer1, isGlobal);
        contentPanel.add(bindButton, gbc);

        gbc.gridy++;
    }

    private JButton createBindButton(InputAction action, boolean isPlayer1, boolean isGlobal) {
        int displayKey = getCurrentKey(action, isPlayer1, isGlobal);
        String keyName = (displayKey == -1) ? "..." : KeyEvent.getKeyText(displayKey);

        JButton button = new ModernButton(keyName);
        button.setPreferredSize(new Dimension(140, 30));

        button.addActionListener(e -> {
            if (isListeningForKey) return;

            isListeningForKey = true;
            String originalText = button.getText();
            button.setText("Press Key...");

            KeyAdapter keyAdapter = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent evt) {
                    int newKeyCode = evt.getKeyCode();

                    if (isGlobal) {
                        pendingGoBackKey = newKeyCode;
                    } else {
                        if (isPlayer1) p1PendingChanges.put(action, newKeyCode);
                        else p2PendingChanges.put(action, newKeyCode);
                    }

                    button.setText(KeyEvent.getKeyText(newKeyCode));
                    finishListening(button, this);
                }
            };

            FocusAdapter focusAdapter = new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    button.setText(originalText);
                    finishListening(button, keyAdapter);
                    button.removeFocusListener(this);
                }
            };

            button.addKeyListener(keyAdapter);
            button.addFocusListener(focusAdapter);
            button.requestFocusInWindow();
        });

        return button;
    }

    private void finishListening(JButton button, KeyAdapter keyAdapter) {
        isListeningForKey = false;
        button.removeKeyListener(keyAdapter);
        for(var fl : button.getFocusListeners()) {
            if(fl instanceof FocusAdapter) button.removeFocusListener(fl);
        }
    }

    private int getCurrentKey(InputAction action, boolean isPlayer1, boolean isGlobal) {
        if (isGlobal) {
            return (pendingGoBackKey != -1) ? pendingGoBackKey : keyInputHandler.getGoBackToMenuKey();
        }
        Map<InputAction, Integer> pendingMap = isPlayer1 ? p1PendingChanges : p2PendingChanges;
        if (pendingMap.containsKey(action)) {
            return pendingMap.get(action);
        }
        return keyInputHandler.getKeyForAction(action, isPlayer1);
    }

    private void addSectionHeader(String text, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(20, 20, 10, 20);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lbl = new JLabel(text);
        lbl.setFont(HEADER_FONT);
        lbl.setForeground(new Color(60, 130, 255));
        contentPanel.add(lbl, gbc);

        gbc.gridy++;
    }

    private void addSeparator(GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 40, 10, 40);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(60, 60, 60));
        sep.setBackground(BACKGROUND_COLOR);
        contentPanel.add(sep, gbc);

        gbc.gridy++;
    }

    private void saveAndExit() {
        p1PendingChanges.forEach((action, key) -> keyInputHandler.changeKeyBinding(action, key, true));
        p2PendingChanges.forEach((action, key) -> keyInputHandler.changeKeyBinding(action, key, false));

        if (pendingGoBackKey != -1) {
            keyInputHandler.bindGoBackToMenuKey(pendingGoBackKey);
        }
        resetPendingChanges();
        mainPanel.backToStartMenu();
    }

    private void resetPendingChanges() {
        p1PendingChanges.clear();
        p2PendingChanges.clear();
        pendingGoBackKey = -1;
    }

    private String formatActionName(InputAction action) {
        String name = action.name().replace("_", " ").toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    // ==========================================================
    // CUSTOM SCROLLBAR UI
    // ==========================================================
    private static class ModernScrollBarUI extends BasicScrollBarUI {
        private final Dimension d = new Dimension();

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return new JButton() {
                @Override
                public Dimension getPreferredSize() {
                    return d; // Zero size (removes the arrow buttons)
                }
            };
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return new JButton() {
                @Override
                public Dimension getPreferredSize() {
                    return d; // Zero size (removes the arrow buttons)
                }
            };
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            // Paint the track same color as background so it looks transparent
            g.setColor(BACKGROUND_COLOR);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Color of the scroll handle
            g2.setColor(new Color(80, 80, 80));

            // Draw with rounded corners
            // We shrink it slightly (x+2, width-4) to give it some padding on the sides
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2,
                    thumbBounds.width - 4, thumbBounds.height - 4,
                    10, 10);
            g2.dispose();
        }

        @Override
        protected void setThumbBounds(int x, int y, int width, int height) {
            super.setThumbBounds(x, y, width, height);
            scrollbar.repaint();
        }
    }
}