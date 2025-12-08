package menus;

import main.MainPanel;
import tetris.keyMaps.InputAction;
import tetris.keyMaps.KeyBindingsManager;
import tetris.keyMaps.KeyInputHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class SettingsPanel extends JPanel {
    private MainPanel mainPanel;
    private KeyInputHandler keyInputHandler;
    private JPanel contentPanel;

    // Registry to track all buttons so we can update them dynamically
    // Maps the JButton to its context (Action, Player, etc.)
    private final Map<JButton, BindingContext> buttonRegistry = new HashMap<>();

    // Temporary storage for changes before saving
    private Map<InputAction, Integer> p1PendingChanges = new HashMap<>();
    private Map<InputAction, Integer> p2PendingChanges = new HashMap<>();
    private int pendingGoBackKey = -1;

    // Track the currently active button (the one waiting for a key)
    private JButton activeBindButton = null;
    private KeyAdapter activeKeyAdapter = null;
    private FocusAdapter activeFocusAdapter = null;

    // UI Constants
    private static final Color BACKGROUND_COLOR = new Color(22, 22, 22);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    // Record to hold context for each button
    private record BindingContext(InputAction action, boolean isPlayer1, boolean isGlobal) {}

    public SettingsPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;

        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        // --- CENTER: SCROLLABLE CONTENT ---
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Add a mouse listener to the panel to grab focus when clicking empty space
        // This ensures the button loses focus (and cancels binding) if you click the background
        contentPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));

        add(scrollPane, BorderLayout.CENTER);

        // --- BOTTOM: FOOTER ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        ModernButton saveBtn = new ModernButton("SAVE & EXIT");
        saveBtn.addActionListener(e -> saveAndExit());

        ModernButton cancelBtn = new ModernButton("CANCEL");
        cancelBtn.addActionListener(e -> {
            cancelActiveBinding(); // Ensure any active listener is stopped
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

    public void refresh() {
        if (keyInputHandler == null) return;

        cancelActiveBinding();
        buttonRegistry.clear();
        contentPanel.removeAll();
        p1PendingChanges.clear();
        p2PendingChanges.clear();
        pendingGoBackKey = -1;

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
        JButton button = new ModernButton("...");
        button.setPreferredSize(new Dimension(140, 30));

        // Register button context
        BindingContext ctx = new BindingContext(action, isPlayer1, isGlobal);
        buttonRegistry.put(button, ctx);

        // Set Initial Text
        updateButtonLabel(button, ctx);

        button.addActionListener(e -> {
            // 1. If another button is already active, cancel it first.
            if (activeBindButton != null && activeBindButton != button) {
                cancelActiveBinding();
            }

            // 2. If this button was already active (rare double click), ignore
            if (activeBindButton == button) return;

            // 3. Start Listening
            startListening(button, ctx);
        });

        return button;
    }

    private void startListening(JButton button, BindingContext ctx) {
        activeBindButton = button;
        button.setText("Press Key...");

        // Key Listener: Captures the new key
        activeKeyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                int newKeyCode = evt.getKeyCode();

                // 1. Resolve conflicts (Steal key from other actions)
                handleKeyConflict(newKeyCode, ctx);

                // 2. Set the new binding in pending storage
                setPendingKey(ctx, newKeyCode);

                // 3. Stop listening
                stopListening(button);

                // 4. Update ALL buttons (to show if a key was stolen from another button)
                updateAllButtonLabels();
            }
        };

        // Focus Listener: Cancels if user clicks away
        activeFocusAdapter = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                // Determine if we should really cancel.
                // Sometimes internal focus changes happen, but usually focusLost means
                // the user clicked another component or the background.
                cancelActiveBinding();
            }
        };

        button.addKeyListener(activeKeyAdapter);
        button.addFocusListener(activeFocusAdapter);

        // Important: Button must stay enabled to receive KeyEvents!
        button.requestFocusInWindow();
    }

    private void stopListening(JButton button) {
        if (button == null) return;

        if (activeKeyAdapter != null) button.removeKeyListener(activeKeyAdapter);
        if (activeFocusAdapter != null) button.removeFocusListener(activeFocusAdapter);

        activeBindButton = null;
        activeKeyAdapter = null;
        activeFocusAdapter = null;
    }

    /**
     * Called when clicking another button or clicking background.
     * Reverts the active button to its stored state.
     */
    private void cancelActiveBinding() {
        if (activeBindButton != null) {
            JButton btn = activeBindButton;
            stopListening(btn);
            // Revert label to what it was before "Press Key..."
            BindingContext ctx = buttonRegistry.get(btn);
            if (ctx != null) updateButtonLabel(btn, ctx);
        }
    }

    private void handleKeyConflict(int keyCode, BindingContext currentCtx) {
        // Iterate over all buttons to see if this key is used elsewhere
        // If strict uniqueness is required globally (keyboard can only do one thing):
        for (BindingContext otherCtx : buttonRegistry.values()) {

            // Skip the action we are currently modifying
            if (otherCtx == currentCtx) continue;

            // Get the key currently assigned to this 'other' action
            int otherKey = getPendingOrActualKey(otherCtx);

            // If it matches the new key, unbind the 'other' action
            if (otherKey == keyCode) {
                setPendingKey(otherCtx, -1); // -1 means Unbound
            }
        }
    }

    private void setPendingKey(BindingContext ctx, int keyCode) {
        if (ctx.isGlobal()) {
            pendingGoBackKey = keyCode;
        } else {
            if (ctx.isPlayer1()) p1PendingChanges.put(ctx.action(), keyCode);
            else p2PendingChanges.put(ctx.action(), keyCode);
        }
    }

    private int getPendingOrActualKey(BindingContext ctx) {
        if (ctx.isGlobal()) {
            if (pendingGoBackKey != -1) return pendingGoBackKey;
            return keyInputHandler.getGoBackToMenuKey();
        }

        Map<InputAction, Integer> pendingMap = ctx.isPlayer1() ? p1PendingChanges : p2PendingChanges;

        // If we have a pending change (even if it is -1/unbound), return it
        if (pendingMap.containsKey(ctx.action())) {
            return pendingMap.get(ctx.action());
        }

        // Otherwise return stored config
        return keyInputHandler.getKeyForAction(ctx.action(), ctx.isPlayer1());
    }

    private void updateAllButtonLabels() {
        buttonRegistry.forEach(this::updateButtonLabel);
    }

    private void updateButtonLabel(JButton button, BindingContext ctx) {
        int key = getPendingOrActualKey(ctx);
        String text = (key == -1) ? "NONE" : KeyEvent.getKeyText(key);
        button.setText(text);
    }

    // --- GRID BAG HELPERS ---

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
        cancelActiveBinding(); // Ensure we aren't in listening mode

        p1PendingChanges.forEach((action, key) -> keyInputHandler.changeKeyBinding(action, key, true));
        p2PendingChanges.forEach((action, key) -> keyInputHandler.changeKeyBinding(action, key, false));

        if (pendingGoBackKey != -1) {
            keyInputHandler.bindGoBackToMenuKey(pendingGoBackKey);
        }

        // Save to XML
        KeyBindingsManager.saveBindings(keyInputHandler);

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
                public Dimension getPreferredSize() { return d; }
            };
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return new JButton() {
                @Override
                public Dimension getPreferredSize() { return d; }
            };
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(BACKGROUND_COLOR);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(80, 80, 80));
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 10, 10);
            g2.dispose();
        }
    }
}