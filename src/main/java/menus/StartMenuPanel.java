package menus;

import main.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Es el primer panel que se muestra, contiene los botones para jugar a todas las modalidades
public class StartMenuPanel extends JPanel {
    private MainPanel mainPanel;

    /**
     * Creates the basic layout for the start menu panel
     * @param mainPanel {@code MainPanel} to modify
     */
    public StartMenuPanel(MainPanel mainPanel) {
        super();

        this.mainPanel = mainPanel;

        // CONSTRAINTS FOR LAYOUT (Generados por Eclipse WindowsBuilder)
        GridBagLayout gbl_startMenu = new GridBagLayout();
        gbl_startMenu.columnWidths = new int[] {0};
        gbl_startMenu.rowHeights = new int[] {0};
        gbl_startMenu.columnWeights = new double[]{1.0, 1.0};
        gbl_startMenu.rowWeights = new double[]{0.0, 1.0};
        setLayout(gbl_startMenu);

        // Título de la sección izquierda para jugar local
        JLabel lblLocal = new JLabel("LOCAL");
        lblLocal.setFont(new Font("Arial", Font.PLAIN, 30));
        lblLocal.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_lblLocal = new GridBagConstraints();
        gbc_lblLocal.ipady = 50;
        gbc_lblLocal.ipadx = 50;
        gbc_lblLocal.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblLocal.insets = new Insets(25, 0, 5, 5);
        gbc_lblLocal.gridx = 0;
        gbc_lblLocal.gridy = 0;
        add(lblLocal, gbc_lblLocal);

        // Título de la sección derecha para jugar online
        JLabel lblOnline = new JLabel("ONLINE");
        lblOnline.setFont(new Font("Arial", Font.PLAIN, 30));
        lblOnline.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_lblOnline = new GridBagConstraints();
        gbc_lblOnline.ipady = 50;
        gbc_lblOnline.ipadx = 50;
        gbc_lblOnline.fill = GridBagConstraints.BOTH;
        gbc_lblOnline.insets = new Insets(25, 0, 5, 0);
        gbc_lblOnline.gridx = 1;
        gbc_lblOnline.gridy = 0;
        add(lblOnline, gbc_lblOnline);

        // Panel que contiene los botones para jugar local
        StartMenuLocalButtonsPanel localButtons = new StartMenuLocalButtonsPanel(mainPanel, this);

        // Panel que contiene los botones para jugar online
        StartMenuOnlineButtonsPanel onlineButtons = new StartMenuOnlineButtonsPanel(mainPanel, this);
    }
}
