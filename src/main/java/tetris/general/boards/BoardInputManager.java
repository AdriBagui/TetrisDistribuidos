package tetris.general.boards;

import tetris.tetrio.boards.TetrioBoardRepresentation;
import tetris.tetrio.boards.TetrioBoardWithPhysics;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BoardInputManager extends Thread {
    /*
     * Vamos a codificar lo que pasamos en bytes para reducir la carga (por ejemplo, un int son 4 bytes, pero nunca
     * necesitamos enviar números tan grandes). Los mensajes estarán codificados de la siguiente manera:
     * El primer byte que llegue codifica la acción a realizar.
     * Las acciones del 0 al 3 están reservadas para mensajes a la garbageRecieverBoard:
     * - Acción 0 (ADD_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
     * El resto de acciones son para mensajes a la updatesRecieverBoard:
     * - Acción 4 (MOVE): le van a seguir 4 bytes significando "X Y ROT LOCK"
     * - Acción 5 (HOLD): no le sigue nada
     * - Acción 6 (UPDATE_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
     */
    private DataInputStream boardsInputReciever;
    private TetrioBoardWithPhysics garbageRecieverBoard;
    private TetrioBoardRepresentation updatesRecieverBoard;

    public BoardInputManager(InputStream boardsInputReciever, TetrioBoardWithPhysics garbageRecieverBoard, TetrioBoardRepresentation updatesRecieverBoard) {
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
                        if (updatesRecieverBoard != null) updatesRecieverBoard.setFallingTetrominoXYRotationIndex(x,y,rotationIndex);
                        break;
                    case 5:
                        // Acción 5 (HOLD): no le sigue nada
                        if (updatesRecieverBoard != null) updatesRecieverBoard.hold();
                        break;
                    case 6:
                        // Acción 6 (UPDATE_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
                        numberOfGarbageRowsToUpdate = boardsInputReciever.readByte();
                        emptyGarbageColumnToUpdate = boardsInputReciever.readByte();
                        if (updatesRecieverBoard != null) updatesRecieverBoard.addGarbage(numberOfGarbageRowsToUpdate, emptyGarbageColumnToUpdate);
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
