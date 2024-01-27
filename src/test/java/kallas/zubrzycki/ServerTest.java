package kallas.zubrzycki;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

// The server needs to be running for these tests!

public class ServerTest {
    private static String IP_ADDRESS = "127.0.0.1";
    private static int PORT = 4444;

    @Test
    @Ignore
    public void shouldNotStartGameForOnePlayer() {
        /*
        Client client = new Client();
        client.startConnection(IP_ADDRESS, PORT);
        String output = client.sendMessage("some message");

        client.stopConnection();

        assertEquals("The game hasn't started yet!", output);
        */
    }

    @Test
    public void shouldStartGameForTwoPlayers() {
        /*
        TODO
        Client client = new Client();
        client.startConnection(IP_ADDRESS, PORT);
        Client client2 = new Client();
        client2.startConnection(IP_ADDRESS, PORT);
        String output = client2.sendMessage("some message");
        System.out.println(output);

        client.stopConnection();
        client2.stopConnection();

        assertNotEquals("The game hasn't started yet!", output);
        */
    }
}
