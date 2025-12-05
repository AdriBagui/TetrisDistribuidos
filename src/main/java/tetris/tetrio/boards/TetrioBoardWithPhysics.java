package tetris.tetrio.boards;

import tetris.general.boards.BoardWithPhysics;
import tetris.general.tetrominoes.Tetromino;

import java.io.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import static tetris.Config.BOARD_COLUMNS;

public class TetrioBoardWithPhysics extends BoardWithPhysics {
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
    protected DataOutputStream garbageAndUpdatesOutput;
    protected Queue<Byte> garbageRowsToAdd;
    protected Queue<Byte> garbageEmptyColumnsToAdd;

    public TetrioBoardWithPhysics(int x, int y, long seed, OutputStream garbageAndUpdatesOutput) {
        super(x, y, seed);

        this.garbageAndUpdatesOutput = new DataOutputStream(garbageAndUpdatesOutput);
        garbageRowsToAdd = new ConcurrentLinkedDeque<>();
        garbageEmptyColumnsToAdd = new ConcurrentLinkedDeque<>();
    }

    public void addGarbage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        garbageRowsToAdd.add(numberOfGarbageRows);
        garbageEmptyColumnsToAdd.add(emptyGarbageColumn);

        // Acción 6 (UPDATE_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
        sendMessage((byte) 6, new byte[] {
                numberOfGarbageRows,
                emptyGarbageColumn
        });
    }

    @Override
    public void update() {
        super.update();

        if (!garbageRowsToAdd.isEmpty()) { updateGarbage(); }

        // Acción 4 (MOVE): le van a seguir 4 bytes significando "X Y ROT LOCK"
        sendMessage((byte) 4, new byte[] {
                (byte) fallingTetromino.getX(),
                (byte) fallingTetromino.getY(),
                (byte) fallingTetromino.getRotationIndex(),
                (byte) (boardPhysics.isLocked() ? 1 : 0)
        });
    }
    protected void updateGarbage() {
        boolean overflow = false;
        int distanceToFloor;
        byte garbageRows;

        while (!garbageRowsToAdd.isEmpty()) {
            garbageRows = garbageRowsToAdd.remove();

            // Si no ha sido ya fijado al tablero
            if (fallingTetromino != null) { // Es null si acaba de ser fijado en el mismo frame
                distanceToFloor = grid.distanceToFloor(fallingTetromino);

                // Si las filas de basura llegan a la altura del tetromino
                if (distanceToFloor < garbageRows) {
                    fallingTetromino.setY(Math.max(fallingTetromino.getY() - garbageRows + distanceToFloor, 0));
                }
            }

            // Si ha habido overflow al añadir basura se marca
            if (grid.addGarbage(garbageRows, garbageEmptyColumnsToAdd.remove()))
                overflow = true;
        }

        if (overflow) isAlive = false;
    }

    @Override
    protected void lockTetromino() {
        int totalClearedLines = this.totalClearedLines;
        super.lockTetromino();
        int clearedLines = this.totalClearedLines - totalClearedLines;

        // Acción 0 (ADD_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
        switch (clearedLines) {
            case 2:
                sendGarbageMessage((byte) 1, (byte) garbageRandom.nextInt(BOARD_COLUMNS));
                break;
            case 3:
                sendGarbageMessage((byte) 2, (byte) garbageRandom.nextInt(BOARD_COLUMNS));
                break;
            case 4:
                sendGarbageMessage((byte) 4, (byte) garbageRandom.nextInt(BOARD_COLUMNS));
                break;
        }
    }

    @Override
    public void hold() {
        super.hold();

        // Acción 5 (HOLD): no le sigue nada
        sendMessage((byte) 5, null);
    }

    // ---------------------------------------------------------------------------------
    // Métodos auxiliares
    private synchronized void sendMessage(byte action, byte[] content) {
        try {
            garbageAndUpdatesOutput.writeByte(action);

            if (content != null) {
                for (int i = 0; i < content.length; i++) {
                    garbageAndUpdatesOutput.writeByte(content[i]);
                }
            }

            garbageAndUpdatesOutput.flush();
        }
        catch (IOException ioe) {
            // Se entra si se cierra el input socket al otro lado
            // O si de alguna forma se cierra este output socket
            ioe.printStackTrace();
        }
    }

    private void sendGarbageMessage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        // Acción 0 (ADD_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
        sendMessage((byte) 0, new byte[] {
                numberOfGarbageRows,
                emptyGarbageColumn
        });
    }
}
