package src.tetris.boards;

import src.tetris.physics.BoardPhysics;
import src.tetris.physics.OnlineBoardPhysics;

import java.io.InputStream;

public class OnlineBoard extends Board {
    private InputStream inputStream;

    public OnlineBoard(int x, int y, long seed, InputStream inputStream) {
        super(x, y, seed);
        this.inputStream = inputStream;
    }

    @Override
    protected BoardPhysics initializeBoardPhysics() {
        return new OnlineBoardPhysics(grid, inputStream);
    }

    @Override
    protected void updateGarbage() {
        // TODO: Necesita saber la columna que se va a quedar vacía, probablemente como es online board physics la que lee los inputs
        // TODO: hacer un método get en la online board physics
    }
}
