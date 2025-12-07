package main;

import javax.swing.*;

public class Main {
    // La aplicación empieza aquí
    public static void main(String[] args) {
        // SwingUtilities.invokeLater es para que el código del run se ejecute en el hilo del AWT (el hilo que gestiona
        // los eventos de la interfaz gráfica como el cambio de tamaño del frame, teclas presionadas, ...). Si no se hiciera
        // así podría haber problemas de concurrencia de hilos entre el hilo que ejecutará la aplicación y el hilo de AWT.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}
