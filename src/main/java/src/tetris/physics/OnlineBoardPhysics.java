package src.tetris.physics;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class OnlineBoardPhysics extends BoardPhysics {
    private InputStream inputStream;
    private boolean lock;
    private int garbageLines;

    public OnlineBoardPhysics(boolean[][] grid, InputStream inputStream) {
        super(grid);
        this.inputStream = inputStream;
        this.lock = false;
        this.garbageLines = 0;
    }

    // Este método se encarga de actualizar el estado del tetromino activo en todo momento
    @Override
    public void update() {
        /*
        Vamos a codificar lo que pasamos de la siguiente manera. Enviaremos bytes codificados de la siguiente manera:
            - Si empieza por 1 le van a seguir 4 bytes significando "MOVE X Y ROT LOCK"
            - Si empieza por 2 significará "HOLD"
            - Si empieza por 3 le va a seguir 1 byte significando "GARBAGE LINEAS"
        */
        try{
            int action = inputStream.read();
            int x, y, rotationIndex, lock;
            switch (action){
                case 1:
                    x = inputStream.read();
                    y = inputStream.read();
                    rotationIndex = inputStream.read();
                    lock = inputStream.read();
                    fallingTetromino.setXYRotationIndex(x,y,rotationIndex);
                    this.lock = (lock==1);
                    break;
                case 3:
                    this.garbageLines = inputStream.read();
                    break;
            }
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    // Sirve para ver si una pieza ha sido fijada
    @Override
    public boolean isLocked() {
        return this.lock;
    }

    public int getGarbageLines(){
        return this.garbageLines;
    }
}
