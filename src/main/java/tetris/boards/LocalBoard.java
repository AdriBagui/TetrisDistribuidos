package tetris.boards;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static tetris.Config.BOARD_COLUMNS;

public class LocalBoard extends PhysicsCalculatingBoard {
    private DataOutputStream outputStream;


    public LocalBoard(int x, int y, long seed, OutputStream outputStream) {
        super(x, y, seed);
        this.outputStream = new DataOutputStream(outputStream);
    }

    /*
    Vamos a codificar lo que pasamos de la siguiente manera. Enviaremos bytes codificados de la siguiente manera:
        - Si empieza por 1 le van a seguir 4 bytes significando "MOVE X Y ROT LOCK"
        - Si empieza por 2 significará "HOLD"
        - Si empieza por 3 le va a seguir 1 byte significando "RECIEVE_GARBAGE LINEAS COLUMNA_VACÍA" (para recibir la basura enviada por el rival)
        - Si empieza por 4 le va a seguir 2 bytes significando "UPDATE_GARBAGE LINEAS COLUMNA_VACÍA" (para actualizar la OnlineBoard (la que representa la board del rival))
    */

    @Override
    public void update() {
        super.update();
        // Aquí se envía lo relacinado con el *CASO 1*
        synchronized (this) {
            try {
                outputStream.writeShort(1); // Indico el caso
                outputStream.writeInt(fallingTetromino.getX());
                outputStream.writeInt(fallingTetromino.getY());
                outputStream.writeInt(fallingTetromino.getRotationIndex());
                outputStream.writeBoolean(boardPhysics.isLocked());
                outputStream.flush();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    @Override
    public void hold() {
        super.hold();
        // Aquí se envía lo relacinado con el *CASO 2*
        synchronized (this) {
            try {
                outputStream.writeShort(2); // Envío el caso
                outputStream.flush();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    @Override
    protected void clearLines() {
        // Aquí se envía lo relacinado con el *CASO 3*

        int totalClearedLines = this.totalClearedLines;
        super.clearLines();
        int clearedLines = this.totalClearedLines - totalClearedLines;

        switch (clearedLines) {
            case 2:
                sendGarbage(1, garbageRandom.nextInt(BOARD_COLUMNS));
                break;
            case 3:
                sendGarbage(2, garbageRandom.nextInt(BOARD_COLUMNS));
                break;
            case 4:
                sendGarbage(4, garbageRandom.nextInt(BOARD_COLUMNS));
                break;
        }
    }

    @Override
    public void addGarbage(int lines, int emptyGarbageColumn) {
        // Aquí se envía lo relacinado con el *CASO 4*
        super.addGarbage(lines, emptyGarbageColumn);

        synchronized (this) {
            try{
                outputStream.writeShort(4); // Envio el caso
                outputStream.writeInt(garbageLinesToAdd);
                outputStream.writeInt(this.emptyGarbageColumn);
                outputStream.flush();
            } catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
    }

    private synchronized void sendGarbage(int lines, int emptyColumn) {
        // Métod0 auxiliar para lo relacinado con el *CASO 3*
        try{
            outputStream.writeShort(3); // Envio el caso
            outputStream.writeInt(lines);
            outputStream.writeInt(emptyColumn);
            outputStream.flush();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}