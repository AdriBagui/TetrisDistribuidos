package tetris.boards;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BoardOutputHandler {
    /*
     * Vamos a codificar lo que pasamos en bytes para reducir la carga (por ejemplo, un int son 4 bytes, pero nunca
     * necesitamos enviar números tan grandes). Los mensajes estarán codificados de la siguiente manera:
     * Las acciones del 0 al 3 están reservadas para mensajes entre TetrioBoardWithPhysics:
     * - Acción 0 (ADD_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
     * El resto de acciones son para mensajes a sus representaciones:
     * - Acción 4 (MOVE): le van a seguir 4 bytes significando "X Y ROT LOCK"
     * - Acción 5 (HOLD): no le sigue nada
     * - Acción 6 (UPDATE_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
     */
    private static final byte ADD_GARBAGE = 0;
    private static final byte MOVE = 4;
    private static final byte HOLD = 5;
    private static final byte UPDATE_GARBAGE = 6;

    protected DataOutputStream dataOutputStream;

    public BoardOutputHandler(OutputStream outputStream) {
        this.dataOutputStream = new DataOutputStream(outputStream);
    }

    public void sendAddGarbageMessage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        // Acción 0 (ADD_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
        sendMessage(ADD_GARBAGE, new byte[] {
                numberOfGarbageRows,
                emptyGarbageColumn
        });
    }

    public void sendUpdateMessage(byte x, byte y, byte rotationIndex, boolean isFallingTetrominoLocked) {
        // Acción 4 (MOVE): le van a seguir 4 bytes significando "X Y ROT LOCK"
        sendMessage((byte) 4, new byte[] {
                x,
                y,
                rotationIndex,
                (byte) (isFallingTetrominoLocked ? 1 : 0)
        });
    }

    public void sendHoldMessage() { sendMessage((byte) 5, null); }

    public void sendUpdateGarbageMessage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        // Acción 6 (UPDATE_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
        sendMessage((byte) 6, new byte[] {
                numberOfGarbageRows,
                emptyGarbageColumn
        });
    }

    // ---------------------------------------------------------------------------------
    // Métodos auxiliares
    private synchronized void sendMessage(byte action, byte[] content) {
        try {
            dataOutputStream.writeByte(action);

            if (content != null) {
                for (byte b : content) {
                    dataOutputStream.writeByte(b);
                }
            }

            dataOutputStream.flush();
        }
        catch (IOException ioe) {
            // Se entra si se cierra el input socket al otro lado
            // O si de alguna forma se cierra este output socket
            ioe.printStackTrace();
        }
    }
}
