package tetris.general.boards;

import tetris.tetrio.boards.TetrioBoardRepresentation;
import tetris.tetrio.boards.TetrioBoardWithPhysics;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BoardsInputManager extends Thread {
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
    private final DataInputStream boardsInputReceiver;
    private final TetrioBoardWithPhysics garbageReceiverBoard;
    private final TetrioBoardRepresentation updatesReceiverBoard;

    public BoardsInputManager(InputStream boardsInputReceiver, TetrioBoardWithPhysics garbageReceiverBoard, TetrioBoardRepresentation updatesReceiverBoard) {
        this.boardsInputReceiver = new DataInputStream(boardsInputReceiver);
        this.garbageReceiverBoard = garbageReceiverBoard;
        this.updatesReceiverBoard = updatesReceiverBoard;
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
                action = boardsInputReceiver.readByte();
                switch (action) {
                    case 0:
                        // Acción 0 (ADD_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
                        numberOfGarbageRowsToAdd = boardsInputReceiver.readByte();
                        emptyGarbageColumnToAdd = boardsInputReceiver.readByte();
                        garbageReceiverBoard.addGarbage(numberOfGarbageRowsToAdd, emptyGarbageColumnToAdd);
                        break;
                    case 4:
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

                        break;
                    case 5:
                        // Acción 5 (HOLD): no le sigue nada
                        if (updatesReceiverBoard != null) updatesReceiverBoard.hold();
                        break;
                    case 6:
                        // Acción 6 (UPDATE_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
                        numberOfGarbageRowsToUpdate = boardsInputReceiver.readByte();
                        emptyGarbageColumnToUpdate = boardsInputReceiver.readByte();
                        if (updatesReceiverBoard != null) updatesReceiverBoard.addGarbage(numberOfGarbageRowsToUpdate, emptyGarbageColumnToUpdate);
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
