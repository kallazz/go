package kallas.zubrzycki;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

public class GameManagerTest {
    @Test
    public void shouldMakeMove() {
        GameManager gameManager = new GameManager();
        gameManager.initializeGame();
        gameManager.beginGame();

        // InputStream sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream("go 5 5".getBytes());
        System.setIn(in);

        in = new ByteArrayInputStream("go 5 5".getBytes());
        System.setIn(in);
    }
}
