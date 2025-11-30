package src.tetris.boards;

import src.tetris.physics.BoardPhysics;
import src.tetris.physics.LocalBoardPhysics;

import java.io.IOException;
import java.io.OutputStream;

public class LocalBoard extends ClassicBoard {
    private OutputStream outputStream;


    public LocalBoard(int x, int y, long seed, OutputStream outputStream) {
        super(x, y, seed);
        this.outputStream = outputStream;
    }

    /*
        Vamos a codificar lo que pasamos de la siguiente manera. Enviaremos bytes codificados de la siguiente manera:
            - Si empieza por 1 le van a seguir 4 bytes significando "MOVE X Y ROT LOCK"
            - Si empieza por 2 significará "HOLD"
            - Si empieza por 3 le va a seguir 1 byte significando "GARBAGE LINEAS"
    */

    @Override
    public void update() {
        super.update();
        // Aquí se envía lo relacinado con el *CASO 1*
        try {
            outputStream.write(1); // Indico el caso
            outputStream.write(fallingTetromino.getX());
            outputStream.write(fallingTetromino.getY());
            outputStream.write(fallingTetromino.getRotationIndex());
            outputStream.write(boardPhysics.isLocked()? 1 : 0);
            outputStream.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void hold() {
        super.hold();
        // Aquí se envía lo relacinado con el *CASO 2*
        try {
            outputStream.write(2); // Envío el caso
            outputStream.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void addGarbage(int lines) {
        super.addGarbage(lines);
        try{
            outputStream.write(3); // Envio el caso
            outputStream.write(lines);
            outputStream.flush();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}