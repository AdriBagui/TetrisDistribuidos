package src;

import javax.swing.*;
import java.awt.*;

// El MainFrame es el frame (la ventana) de la aplicación
public class MainFrame extends JFrame {
    private MainPanel mainPanel;

    public MainFrame() {
        // Cambia el título que se ve en la barra de arriba de la ventana
        setTitle("Tetris Distribuidos");
        // Cambia el comportamiento de presionar la x de la ventana para que se cierre la aplicación
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Impide que se pueda modificar el tamaño de la ventana
        setResizable(false);

        // Este layout será el que use el MainPanel. Es como una baraja de paneles (en vez de de cartas) y puedes
        // cambiar de uno a otro fácilmente
        CardLayout cardLayout = new CardLayout();

        mainPanel = new MainPanel(cardLayout);
        add(mainPanel);

        // Hace que el frame se adecue al tamaño de su contenido
        pack();

        // Centra el frame en el centro de la pantalla
        setLocationRelativeTo(null);
    }
}
