package kallas.zubrzycki;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BoardTest {
    private static int BOARD_SIZE = 10;

    @Test
    public void shouldArrayGetInitializedCorrectly() {
        final Board board = new Board(BOARD_SIZE);
        final EPointColor[][] boardPoints = board.getBoardPoints();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                assertEquals(EPointColor.NONE, boardPoints[i][j]);
            }
        }
    }
}
