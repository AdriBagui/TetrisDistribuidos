package classictetris;

public class SquareBlock extends Block { // O Piece
    @Override protected void initShape() {
        shape = new int[][] {
                {1, 1},
                {1, 1}
        };
        colorIndex = 4;
    }
}
