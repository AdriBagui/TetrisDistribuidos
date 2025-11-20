package classictetris;

public class ZBlock extends Block { // Z Piece
    @Override protected void initShape() {
        shape = new int[][] {
                {1, 1, 0},
                {0, 1, 1}
        };
        colorIndex = 7;
    }
}
