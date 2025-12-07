package tetris.boards;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReceiverBoardInputHandler extends Thread {
    /*
     * Vamos a codificar lo que pasamos en bytes para reducir la carga (por ejemplo, un int son 4 bytes, pero nunca
     * necesitamos enviar números tan grandes). Los mensajes estarán codificados de la siguiente manera:
     * El primer byte que llegue codifica la acción a realizar.
     * Las acciones del 0 al 3 están reservadas para mensajes a la garbageReceiverBoard:
     * - Acción 0 (ADD_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
     * El resto de acciones son para mensajes a la updatesReceiverBoard:
     * - Acción 4 (MOVE): le van a seguir 4 bytes significando "X Y ROT LOCK"
     * - Acción 5 (HOLD): no le sigue nada
     * - Acción 6 (UPDATE_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
     */
    protected final DataInputStream boardsInputReceiver;
    protected final ReceiverBoard updatesReceiverBoard;

    public ReceiverBoardInputHandler(InputStream boardsInputReceiver, ReceiverBoard updatesReceiverBoard) {
        this.boardsInputReceiver = new DataInputStream(boardsInputReceiver);
        this.updatesReceiverBoard = updatesReceiverBoard;
    }

    @Override
    public void run() {
        byte action;

        // Cuando se interrumpe este hilo, acaba lo que esté haciendo y se muere
        while (!isInterrupted()) {
            try {
                action = readAction();
                executeAction(action);
            }
            catch (IOException ioe){
                // Se entra si se cierra el output socket al otro lado
                // O si de alguna forma se cierra este input socket
                ioe.printStackTrace();
            }
        }
    }

    protected byte readAction() throws IOException {
        return boardsInputReceiver.readByte();
    }

    protected void executeAction(byte action) throws IOException {
        byte x, y, rotationIndex;
        boolean isLocked;

        if (action == 4) {
            // Acción 4 (MOVE): le van a seguir 4 bytes significando "X Y ROT LOCK"
            x = boardsInputReceiver.readByte();
            y = boardsInputReceiver.readByte();
            rotationIndex = boardsInputReceiver.readByte();
            isLocked = (boardsInputReceiver.readByte() == 1);

            if (updatesReceiverBoard != null)  {
                updatesReceiverBoard.setFallingTetrominoXYRotationIndex(x,y,rotationIndex);
                if (isLocked) updatesReceiverBoard.lockFallingTetromino();
                updatesReceiverBoard.update();
            }
        }
    }
}
