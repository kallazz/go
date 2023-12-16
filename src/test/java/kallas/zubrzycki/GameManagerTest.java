package kallas.zubrzycki;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

public class GameManagerTest {
    @Test
    public void shouldParseWrongInput() {
        final GameManager gameManager = new GameManager();

        InputStream sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream("pass".getBytes());
        System.setIn(in);
    }
}
