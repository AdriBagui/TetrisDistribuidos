package src.tetris.boards;

import src.tetris.physics.BoardPhysics;
import src.tetris.physics.OnlineBoardPhysics;

public class OnlineBoard extends Board {
    public OnlineBoard(int x, int y, long seed) {
        super(x, y, seed);
        // TODO: Insertar InputStream (y no sé si algo más)
    }

    @Override
    protected BoardPhysics initializeBoardPhysics() {
        // TODO: La OnlineBoardPhysics necesitará un Input Stream (y no sé si algo más)
        return new OnlineBoardPhysics(grid);
    }

    @Override
    protected void updateGarbage() {
        // TODO: Necesita saber la columna que se va a quedar vacía, probablemente como es online board physics la que lee los inputs
        // TODO: hacer un método get en la online board physics
    }
}
