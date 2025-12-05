package tetris.boards;

import tetris.tetrominoes.Tetromino;

import java.io.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import static tetris.Config.BOARD_COLUMNS;

public class TetrioBoard extends PhysicsCalculatingBoard {
    protected DataOutputStream garbageOutput;
    protected Queue<Byte> garbageRowsToAdd;
    protected Queue<Byte> garbageEmptyColumnsToAdd;

    public TetrioBoard(int x, int y, long seed, OutputStream garbageAndUpdateOutput) {
        super(x, y, seed);

        this.garbageOutput = new DataOutputStream(garbageAndUpdateOutput);
        garbageRowsToAdd = new ConcurrentLinkedDeque<>();
        garbageEmptyColumnsToAdd = new ConcurrentLinkedDeque<>();
    }

    public void addGarbage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        garbageRowsToAdd.add(numberOfGarbageRows);
        garbageEmptyColumnsToAdd.add(emptyGarbageColumn);
    }

    @Override
    public void update() {
        super.update();

        if (!garbageRowsToAdd.isEmpty()) { updateGarbage(); }
    }
    protected void updateGarbage() {
        boolean overflow = false;
        Tetromino aux;
        int distanceToFloor;
        byte garbageRows;

        while (!garbageRowsToAdd.isEmpty()) {
            garbageRows = garbageRowsToAdd.remove();

            if (fallingTetromino != null) {
                aux = fallingTetromino.createCopy();

                distanceToFloor = 0;
                do {
                    aux.moveDown();
                    distanceToFloor++;
                } while (distanceToFloor < garbageRows && !grid.hasCollision(aux));
                distanceToFloor -= 1;

                if (distanceToFloor < garbageRows) {
                    if (fallingTetromino.getY() - garbageRows + distanceToFloor >= 0) {
                        fallingTetromino.setY(fallingTetromino.getY() - garbageRows + distanceToFloor);
                    } else {
                        fallingTetromino.setY(0);
                    }
                }
            }

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

        try {
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
        catch (IOException ioe) {
            // Se entra si se cierra el input socket al otro lado
            // O si de alguna forma se cierra este output socket
            ioe.printStackTrace();
        }
    }

    private void sendGarbageMessage(byte numberOfGarbageRows, byte emptyGarbageColumn) throws IOException {
        garbageOutput.writeByte(0);
        garbageOutput.writeByte(numberOfGarbageRows);
        garbageOutput.writeByte(emptyGarbageColumn);
    }
}
