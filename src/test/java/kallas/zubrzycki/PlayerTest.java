package kallas.zubrzycki;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PlayerTest {
    @Test
    public void shouldCapturedStonesBeZeroForNewPlayer() {
        Player player = new Player(EPointColor.BLACK);
        assertEquals(0, player.getCapturedStones());
    }

    @Test
    public void shouldAddCapturedStones() {
        Player player = new Player(EPointColor.BLACK);
        player.addCapturedStones(10);
        assertEquals(10, player.getCapturedStones());
    }
}
