package tetris.general.boards.physics;

import tetris.general.boards.BoardGrid;
import tetris.tetrio.boards.InputBoard;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class OnlineBoardPhysics extends BoardPhysics {
    private DataInputStream inputStream;
    private InputBoard inputBoard;
    private boolean isLocked;
    private int garbageLinesRecieved;
    private int emptyGarbageColumnRecieved;
    private int garbageLinesToUpdate;
    private int emptyGarbageColumnToUpdate;

    public OnlineBoardPhysics(BoardGrid grid, InputBoard inputBoard, InputStream inputStream) {
        super(grid);
        this.inputStream = new DataInputStream(inputStream);
        this.inputBoard = inputBoard;
        isLocked = false;
        garbageLinesRecieved = 0;
        emptyGarbageColumnRecieved = 0;
        garbageLinesToUpdate = 0;
        emptyGarbageColumnToUpdate = 0;
    }

    // Este metodo se encarga de actualizar el estado del tetromino activo constantemente
    @Override
    public void update() {
        /*
        Vamos a codificar lo que pasamos de la siguiente manera. Enviaremos bytes codificados de la siguiente manera:
            - Si empieza por 1 le van a seguir 4 bytes significando "MOVE X Y ROT LOCK"
            - Si empieza por 2 significará "HOLD"
            - Si empieza por 3 le va a seguir 1 byte significando "RECIEVE_GARBAGE LINEAS COLUMNA_VACÍA" (para recibir la basura enviada por el rival)
            - Si empieza por 4 le va a seguir 2 bytes significando "UPDATE_GARBAGE LINEAS COLUMNA_VACÍA" (para actualizar la OnlineBoard (la que representa la board del rival))
        */
        try{
            short action = inputStream.readShort();
            int x, y, rotationIndex, lock;
            switch (action) {
                case 1:
                    x = inputStream.readInt();
                    y = inputStream.readInt();
                    rotationIndex = inputStream.readInt();
                    isLocked = inputStream.readBoolean();
                    fallingTetromino.setXYRotationIndex(x,y,rotationIndex);
                    break;
                case 2:
                    inputBoard.hold();
                    break;
                case 3:
                    garbageLinesRecieved = inputStream.readInt();
                    emptyGarbageColumnRecieved = inputStream.readInt();
                    break;
                case 4:
                    garbageLinesToUpdate = inputStream.readInt();
                    emptyGarbageColumnToUpdate = inputStream.readInt();
                    break;
            }
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    // Sirve para ver si una pieza ha sido fijada
    @Override
    public boolean isLocked() { return isLocked; }
    public int getGarbageLinesRecieved(){
        int aux = garbageLinesRecieved;
        garbageLinesRecieved = 0;
        return aux;
    }
    public int getEmptyGarbageColumnRecieved() {
        int aux = emptyGarbageColumnRecieved;
        emptyGarbageColumnRecieved = 0;
        return aux;
    }
    public int getGarbageLinesToUpdate(){
        int aux = garbageLinesToUpdate;
        garbageLinesToUpdate = 0;
        return aux;
    }
    public int getEmptyGarbageColumnToUpdate() {
        int aux = emptyGarbageColumnToUpdate;
        emptyGarbageColumnToUpdate = 0;
        return aux;
    }
}
