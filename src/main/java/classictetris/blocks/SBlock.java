package classictetris.blocks;

public class SBlock extends Block { // S Piece
    @Override protected void initShape() {
        shape = new int[][] {
                {0, 1, 1},
                {1, 1, 0}
        };
        colorIndex = 5;
    }
}
