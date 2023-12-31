package kallas.zubrzycki;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BoardTest {
    private static int BOARD_SIZE = 10;

    @Test
    public void shouldArrayGetInitializedCorrectly() {
        final Board board = Board.getInstance();
        board.initialize(BOARD_SIZE);
        final EPointColor[][] boardPoints = board.getBoardPoints();

        for (int i = 1; i <= BOARD_SIZE; i++) {
            for (int j = 1; j <= BOARD_SIZE; j++) {
                assertEquals(EPointColor.NONE, boardPoints[i][j]);
            }
        }

        for (int i = 0; i <= BOARD_SIZE + 1; i++) {
            assertEquals(EPointColor.BORDER, boardPoints[i][0]);
            assertEquals(EPointColor.BORDER, boardPoints[i][BOARD_SIZE + 1]);
            assertEquals(EPointColor.BORDER, boardPoints[0][i]);
            assertEquals(EPointColor.BORDER, boardPoints[BOARD_SIZE + 1][i]);
        } 
    }

    @Test
    public void shouldCheckWrongMove() {
        final Board board = Board.getInstance();
        board.initialize(BOARD_SIZE);
        assertEquals(false, board.checkMove(0, BOARD_SIZE + 1, EPointColor.BLACK));
    }

    @Test
    public void shouldCheckCorrectMove() {
        final Board board = Board.getInstance();
        board.initialize(BOARD_SIZE);
        assertEquals(true, board.checkMove(1, BOARD_SIZE, EPointColor.BLACK));
    }

    @Test
    public void shouldUpdateBoard() {
        final Board board = Board.getInstance();
        board.initialize(BOARD_SIZE);
        EPointColor[][] boardPoints;

        board.updateBoard(2, 3, EPointColor.BLACK);
        boardPoints = board.getBoardPoints();
        assertEquals(EPointColor.BLACK, boardPoints[2][3]);
        board.updateBoard(2, 3, EPointColor.WHITE);
        boardPoints = board.getBoardPoints();
        assertEquals(EPointColor.WHITE, boardPoints[2][3]);
    }
}
