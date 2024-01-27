package kallas.zubrzycki;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SQLLiteJDBCTest {
    // TODO: Add tests

    @Test
    public void shouldGetPreviousMoveReturnEmptyStringForEmptyFile() {
        /*
        GameHistory gameHistory = GameHistory.getInstance();

        assertEquals("", gameHistory.getPreviousMove(0));
         */
    }

    @Test
    public void shouldAddAndReadLinesToHistoryFile() {
        /*
        GameHistory gameHistory = GameHistory.getInstance();

        gameHistory.addToDatabase("go 1 1");
        gameHistory.addToDatabase("pass");
        gameHistory.addToDatabase("go 2 2");
        gameHistory.addToDatabase("go 3 3");
        gameHistory.addToDatabase("go 4 4");
        gameHistory.addToDatabase("pass");
        gameHistory.addToDatabase("pass");

        // TODO: Zastanowić się czy to ma na pewno tak działać
        assertEquals("go 4 4", gameHistory.getPreviousMove(0));
        assertEquals("go 3 3", gameHistory.getPreviousMove(1));
        assertEquals("go 4 4", gameHistory.getPreviousMove(2));
        assertEquals("go 3 3", gameHistory.getPreviousMove(3));
        assertEquals("go 2 2", gameHistory.getPreviousMove(4));
        assertEquals("", gameHistory.getPreviousMove(5));
        assertEquals("go 1 1", gameHistory.getPreviousMove(6));
        assertEquals("", gameHistory.getPreviousMove(7));
        */
    }
}