package classictetris.blocks;

public class JBlock extends Block { // J Piece
    @Override protected void initShape() {
        shape = new int[][] {
                {1, 0, 0},
                {1, 1, 1}
        };
        colorIndex = 2;
    }
}
