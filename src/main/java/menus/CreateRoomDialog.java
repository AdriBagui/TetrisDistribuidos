package menus;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class CreateRoomDialog extends JDialog {

    private JTextField roomNumberField;
    private int enteredRoomNumber = -1;
    private CardLayout cardLayout = new CardLayout();
    private JPanel contentPanel;
    private static final String INPUT_CARD = "INPUT_VIEW";
    private static final String WAITING_CARD = "WAITING_VIEW";
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 220);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font WAITING_FONT = new Font("Segoe UI", Font.BOLD, 18);

    /**
     * Creates a RoomDialog to create a room
     * @param owner {@code JFrame} owner of the dialog
     */
    public CreateRoomDialog(JFrame owner) {
        // We lock the main thread until this dialog closes.
        super(owner, "Host Game - Enter Room ID", true);

        // --- 1. Window config ---
        setLayout(new BorderLayout()); // Usamos BorderLayout para el JDialog
        setSize(450, 250);
        setLocationRelativeTo(owner);
        setResizable(false);

        // --- 2. Main panel ---
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND_COLOR);

        // --- 3. Creation of both views ---
        JPanel inputView = createInputView();
        JPanel waitingView = createWaitingView();

        contentPanel.add(inputView, INPUT_CARD);
        contentPanel.add(waitingView, WAITING_CARD);

        add(contentPanel, BorderLayout.CENTER);

        // Inicialmente mostramos la vista de entrada
        cardLayout.show(contentPanel, INPUT_CARD);
    }

    /**
     * Creates the view to enter the room code
     * @return {@code JPanel} where you enter the room code
     */
    private JPanel createInputView() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Entry panel
        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel instructionLabel = new JLabel("Enter the desired Room Number (ID):");
        instructionLabel.setForeground(TEXT_COLOR);
        instructionLabel.setFont(LABEL_FONT);

        // Serialized room number field
        roomNumberField = new JTextField(15);
        roomNumberField.setFont(LABEL_FONT);
        roomNumberField.setBackground(new Color(35, 35, 35));
        roomNumberField.setForeground(TEXT_COLOR);
        roomNumberField.setCaretColor(TEXT_COLOR);
        roomNumberField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));

        inputPanel.add(instructionLabel);
        inputPanel.add(roomNumberField);
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));

        JButton createButton = new ModernButton("CREATE ROOM");
        JButton cancelButton = new ModernButton("CANCEL");

        // Listeners
        createButton.addActionListener(e -> attemptRoomCreation());
        cancelButton.addActionListener(e -> {
            enteredRoomNumber = -1;
            dispose();
        });

        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel);

        getRootPane().setDefaultButton(createButton);

        return mainPanel;
    }

    /**
     * Creates the waiting screen for someone to connect
     * @return {@code JPanel} with waiting screen
     */
    private JPanel createWaitingView() {
        JPanel waitingPanel = new JPanel(new GridBagLayout());
        waitingPanel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // We use a spinner to give some sense of time passing
        JProgressBar spinner = new JProgressBar();
        spinner.setIndeterminate(true);
        spinner.setPreferredSize(new Dimension(80, 20));

        // Changes the spinner color
        spinner.setForeground(new Color(60, 130, 255));
        spinner.setBackground(new Color(35, 35, 35));

        gbc.gridy = 0;
        waitingPanel.add(spinner, gbc);

        // 2. Waiting message
        JLabel waitingLabel = new JLabel("Waiting for an opponent...");
        waitingLabel.setFont(WAITING_FONT);
        waitingLabel.setForeground(TEXT_COLOR);

        gbc.gridy = 1;
        waitingPanel.add(waitingLabel, gbc);

        return waitingPanel;
    }

    /**
     * Attempts the creation of a room
     */
    private void attemptRoomCreation() {
        String text = roomNumberField.getText().trim();

        try {
            int number = Integer.parseInt(text);

            if (number <= 0) {
                CustomMessageDialog.showMessage(this,
                        "ERROR: Numbers must be positive.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                roomNumberField.requestFocusInWindow();
                return;
            }

            // 1. Saves room number
            enteredRoomNumber = number;

            // 2. Change view to waiting
            cardLayout.show(contentPanel, WAITING_CARD);

            // 3. TODO: wait for a connection

        } catch (NumberFormatException ex) {
            CustomMessageDialog.showMessage(this,
                    "ERROR: Please enter only valid numbers.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            roomNumberField.requestFocusInWindow();
        }
    }

    public int getRoomNumber() {
        return enteredRoomNumber;
    }
}