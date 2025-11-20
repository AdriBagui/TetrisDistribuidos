package classictetris.blocks;

public class LBlock extends Block { // L Piece
    @Override protected void initShape() {
        shape = new int[][] {
                {0, 0, 1},
                {1, 1, 1}
        };
        colorIndex = 3;
    }
}
