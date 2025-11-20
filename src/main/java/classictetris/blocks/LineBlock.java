package classictetris.blocks;

public class LineBlock extends Block { // I Piece
    @Override protected void initShape() {
        shape = new int[][] { {1, 1, 1, 1} };
        colorIndex = 1;
    }
}
