package client.userInterface.panels;

import client.userInterface.components.ModernButton;
import client.userInterface.components.ModernScrollBar;
import client.keyMaps.InputAction;
import client.keyMaps.KeyBindingsManager;
import client.keyMaps.KeyInputHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import static client.userInterface.panels.MainPanel.*;

/**
 * The settings screen that allows users to configure key bindings for Player 1 and Player 2.
 * <p>
 * This panel dynamically generates a list of buttons for every action defined in {@link InputAction}.
 * It handles the logic for detecting key presses, resolving conflicts, and saving changes to disk via
 * {@link KeyBindingsManager}.
 * </p>
 */
public class SettingsPanel extends JPanel {
    private final MainPanel mainPanel;
    private KeyInputHandler keyInputHandler;
    private final JPanel contentPanel;

    // Registry to track all buttons so we can update them dynamically
    // Maps the JButton to its context (Action, Player, etc.)
    private final Map<JButton, BindingContext> buttonRegistry = new HashMap<>();

    // Temporary storage for changes before saving
    private final Map<InputAction, Integer> p1PendingChanges = new HashMap<>();
    private final Map<InputAction, Integer> p2PendingChanges = new HashMap<>();
    private int pendingGoBackKey = -1;

    // Track the currently active button (the one waiting for a key)
    private JButton activeBindButton = null;
    private KeyAdapter activeKeyAdapter = null;
    private FocusAdapter activeFocusAdapter = null;

    /**
     * Java 8 Compatibility: Replaced 'record' with a static inner class.
     * Represents the context associated with a specific binding button.
     */
    private static class BindingContext {
        private final InputAction action;
        private final boolean isPlayer1;
        private final boolean isGlobal;

        public BindingContext(InputAction action, boolean isPlayer1, boolean isGlobal) {
            this.action = action;
            this.isPlayer1 = isPlayer1;
            this.isGlobal = isGlobal;
        }

        public InputAction getAction() {
            return action;
        }

        public boolean isPlayer1() {
            return isPlayer1;
        }

        public boolean isGlobal() {
            return isGlobal;
        }
    }

    /**
     * Constructs the SettingsPanel layout.
     *
     * @param mainPanel The main application panel (used for navigation).
     */
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
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBar());
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

    /**
     * Sets the input handler used to query current bindings and apply new ones.
     *
     * @param keyInputHandler The application's input handler.
     */
    public void setKeyInputHandler(KeyInputHandler keyInputHandler) {
        this.keyInputHandler = keyInputHandler;
    }

    /**
     * Rebuilds the UI components to reflect the current state of bindings.
     * Should be called every time the settings panel is opened.
     */
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

    /**
     * Adds the list of control rows for a specific player.
     */
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

    /**
     * Adds a single row consisting of a label and a bind button.
     */
    private void addKeyBindingRow(String labelText, InputAction action, boolean isPlayer1, boolean isGlobal, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 30, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(DEFAULT_FONT);
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

    /**
     * Creates a button that, when clicked, listens for the next key press.
     */
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

    /**
     * Enters "listening mode" for a button, capturing the next key press.
     */
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

    /**
     * Stops listening for key events on the specified button.
     */
    private void stopListening(JButton button) {
        if (button == null) return;

        if (activeKeyAdapter != null) button.removeKeyListener(activeKeyAdapter);
        if (activeFocusAdapter != null) button.removeFocusListener(activeFocusAdapter);

        activeBindButton = null;
        activeKeyAdapter = null;
        activeFocusAdapter = null;
    }

    /**
     * Cancels the currently active binding operation (e.g., if the user clicks away).
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

    /**
     * Checks if the new key is already used by another action and unbinds it if necessary.
     */
    private void handleKeyConflict(int keyCode, BindingContext currentCtx) {
        // Iterate over all buttons to see if this key is used elsewhere
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

    /**
     * Stores a proposed key change in the pending maps.
     */
    private void setPendingKey(BindingContext ctx, int keyCode) {
        if (ctx.isGlobal()) {
            pendingGoBackKey = keyCode;
        } else {
            // CHANGED: .action() -> .getAction()
            if (ctx.isPlayer1()) p1PendingChanges.put(ctx.getAction(), keyCode);
            else p2PendingChanges.put(ctx.getAction(), keyCode);
        }
    }

    /**
     * Helper to get the key code for a context, checking pending changes first.
     */
    private int getPendingOrActualKey(BindingContext ctx) {
        if (ctx.isGlobal()) {
            if (pendingGoBackKey != -1) return pendingGoBackKey;
            return keyInputHandler.getGoBackToMenuKey();
        }

        Map<InputAction, Integer> pendingMap = ctx.isPlayer1() ? p1PendingChanges : p2PendingChanges;

        // If we have a pending change (even if it is -1/unbound), return it
        // CHANGED: .action() -> .getAction()
        if (pendingMap.containsKey(ctx.getAction())) {
            return pendingMap.get(ctx.getAction());
        }

        // Otherwise return stored config
        // CHANGED: .action() -> .getAction()
        return keyInputHandler.getKeyForAction(ctx.getAction(), ctx.isPlayer1());
    }

    /**
     * Refreshes the text of all bind buttons.
     */
    private void updateAllButtonLabels() {
        buttonRegistry.forEach(this::updateButtonLabel);
    }

    /**
     * Updates the text of a single button based on its current (pending or actual) key.
     */
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

    /**
     * Commits all pending changes to the KeyInputHandler and saves them to file.
     */
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
}