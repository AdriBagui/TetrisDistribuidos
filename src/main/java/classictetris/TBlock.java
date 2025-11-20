package classictetris;

public class TBlock extends Block { // T Piece
    @Override protected void initShape() {
        shape = new int[][] {
                {0, 1, 0},
                {1, 1, 1}
        };
        colorIndex = 6;
    }
}
