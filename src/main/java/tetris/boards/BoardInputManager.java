package tetris.boards;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BoardInputManager extends Thread {
    private DataInputStream boardsInputReciever;
    private TetrioBoard garbageRecieverBoard;
    private InputBoard updatesRecieverBoard;

    public BoardInputManager(InputStream boardsInputReciever, TetrioBoard garbageRecieverBoard, InputBoard updatesRecieverBoard) {
        this.boardsInputReciever = new DataInputStream(boardsInputReciever);
        this.garbageRecieverBoard = garbageRecieverBoard;
        this.updatesRecieverBoard = updatesRecieverBoard;

        new Thread(this).start();
    }

    @Override
    public void run() {
        byte action, x, y, rotationIndex;
        byte numberOfGarbageRowsToAdd, emptyGarbageColumnToAdd;
        byte numberOfGarbageRowsToUpdate, emptyGarbageColumnToUpdate;
        boolean isLocked;

        // Cuando se interrumpe este hilo, acaba lo que esté haciendo y se muere
        while (!isInterrupted()) {
            /*
             * Vamos a codificar lo que pasamos de la siguiente manera. Enviaremos bytes codificados de la siguiente manera:
             * El primer byte que llegue codifica la acción a realizar.
             * Las acciones del 0 al 3 están reservadas para mensajes a la garbageRecieverBoard:
             * - Acción 0 (ADD_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
             * El resto de acciones son para mensajes a la updatesRecieverBoard:
             * - Acción 4 (MOVE): le van a seguir 4 bytes significando "X Y ROT LOCK"
             * - Acción 5 (HOLD): no le sigue nada
             * - Acción 6 (UPDATE_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
             */
            try {
                action = boardsInputReciever.readByte();
                switch (action) {
                    case 0:
                        // Acción 0 (ADD_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
                        numberOfGarbageRowsToAdd = boardsInputReciever.readByte();
                        emptyGarbageColumnToAdd = boardsInputReciever.readByte();
                        garbageRecieverBoard.addGarbage(numberOfGarbageRowsToAdd, emptyGarbageColumnToAdd);
                        break;
                    case 4:
                        // Acción 4 (MOVE): le van a seguir 4 bytes significando "X Y ROT LOCK"
                        x = boardsInputReciever.readByte();
                        y = boardsInputReciever.readByte();
                        rotationIndex = boardsInputReciever.readByte();
                        isLocked = (boardsInputReciever.readByte() == 1);
                        //fallingTetromino.setXYRotationIndex(x,y,rotationIndex);
                        break;
                    case 5:
                        // Acción 5 (HOLD): no le sigue nada
                        //inputBoard.hold();
                        break;
                    case 6:
                        // Acción 6 (UPDATE_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
                        numberOfGarbageRowsToUpdate = boardsInputReciever.readByte();
                        emptyGarbageColumnToUpdate = boardsInputReciever.readByte();
                        break;
                }
            }
            catch (IOException ioe){
                // Se entra si se cierra el output socket al otro lado
                // O si de alguna forma se cierra este input socket
                ioe.printStackTrace();
            }
        }
    }
}
