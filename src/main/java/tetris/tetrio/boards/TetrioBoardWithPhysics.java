package tetris.tetrio.boards;

import tetris.general.boards.BoardGrid;
import tetris.general.boards.BoardWithPhysics;
import tetris.tetrio.boards.physics.TetrioBoardPhysics;

import java.io.*;
import java.util.Random;

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
    // BOARD PHYSICS
    protected TetrioBoardPhysics tetrioBoardPhysics;
    // GARBAGE SYSTEM
    protected Random garbageRandom;
    protected GarbageRecievingSystem garbageRecievingSystem;

    public TetrioBoardWithPhysics(int x, int y, BoardGrid grid, long seed, OutputStream garbageAndUpdatesOutput) {
        super(x, y, grid, seed, new TetrioBoardPhysics(grid));

        this.garbageAndUpdatesOutput = new DataOutputStream(garbageAndUpdatesOutput);

        tetrioBoardPhysics = (TetrioBoardPhysics) boardPhysics;

        garbageRandom = new Random();
        garbageRecievingSystem = new GarbageRecievingSystem();
    }

    public void addGarbage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        garbageRecievingSystem.addGarbage(numberOfGarbageRows, emptyGarbageColumn);

        // Acción 6 (UPDATE_GARBAGE): le van a seguir 2 bytes significando "LÍNEAS COLUMNA_VACÍA"
        sendMessage((byte) 6, new byte[] {
                numberOfGarbageRows,
                emptyGarbageColumn
        });
    }

    @Override
    public void update() {
        super.update();

        if (garbageRecievingSystem.updateGarbage(grid, fallingTetromino, isFallingTetrominoLocked()))
            isAlive = false;

        // Acción 4 (MOVE): le van a seguir 4 bytes significando "X Y ROT LOCK"
        sendMessage((byte) 4, new byte[] {
                (byte) fallingTetromino.getX(),
                (byte) fallingTetromino.getY(),
                (byte) fallingTetromino.getRotationIndex(),
                (byte) (boardPhysics.isLocked() ? 1 : 0)
        });
    }

    @Override
    protected int lockFallingTetrominoAndClearLines() {
        int clearedLines = super.lockFallingTetrominoAndClearLines();

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

        return clearedLines;
    }

    @Override
    public void hold() {
        super.hold();

        // Acción 5 (HOLD): no le sigue nada
        sendMessage((byte) 5, null);
    }

    // Controls
    @Override
    public void moveLeftPressed() { tetrioBoardPhysics.moveLeftPressed(); }
    @Override
    public void moveLeftReleased() { tetrioBoardPhysics.moveLeftReleased(); }
    @Override
    public void moveRightPressed() { tetrioBoardPhysics.moveRightPressed(); }
    @Override
    public void moveRightReleased() { tetrioBoardPhysics.moveRightReleased(); }
    @Override
    public void softDropPressed() { tetrioBoardPhysics.moveDownPressed(); }
    @Override
    public void softDropReleased() { tetrioBoardPhysics.moveDownReleased(); }
    @Override
    public void hardDropPressed() { tetrioBoardPhysics.dropPressed(); }
    @Override
    public void hardDropReleased() { tetrioBoardPhysics.dropReleased(); }
    @Override
    public void rotateRightPressed() { tetrioBoardPhysics.rotateRightPressed(); }
    @Override
    public void rotateRightReleased() { tetrioBoardPhysics.rotateRightReleased(); }
    @Override
    public void rotateLeftPressed() { tetrioBoardPhysics.rotateLeftPressed(); }
    @Override
    public void rotateLeftReleased() { tetrioBoardPhysics.rotateLeftReleased(); }
    @Override
    public void flipPressed() { tetrioBoardPhysics.flipPressed(); }
    @Override
    public void flipReleased() { tetrioBoardPhysics.flipReleased(); }

    // ---------------------------------------------------------------------------------
    // Métodos auxiliares
    private synchronized void sendMessage(byte action, byte[] content) {
        try {
            garbageAndUpdatesOutput.writeByte(action);

            if (content != null) {
                for (byte b : content) {
                    garbageAndUpdatesOutput.writeByte(b);
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