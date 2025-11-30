package src.tetris.boards;

import src.tetris.physics.LocalBoardPhysics;

import java.io.OutputStream;

public class LocalBoard extends ClassicBoard {
    private OutputStream outputStream;


    public LocalBoard(int x, int y, long seed, OutputStream outputStream) {
        super(x, y, seed);
        this.outputStream = outputStream;
    }

    // TODO: Hacer override del update (o de un método auxiliar si queremos ejecutar código a mitad de update no al final)
    // TODO: para que se envíen los datos necesarios
}