package kallas.zubrzycki;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StoneTest {
    @Test
    public void shouldDoesExistReturnFalseForNoneStone() {
        Stone stone = new Stone(1, 1, EPointColor.NONE);
        assertEquals(false, stone.doesExist());
    }

    @Test
    public void shouldDoesExistReturnTrueForPlayersStones() {
        Stone stone1 = new Stone(1, 1, EPointColor.BLACK);
        assertEquals(true, stone1.doesExist());

        Stone stone2 = new Stone(2, 2, EPointColor.WHITE);
        assertEquals(true, stone2.doesExist());
    }
}
