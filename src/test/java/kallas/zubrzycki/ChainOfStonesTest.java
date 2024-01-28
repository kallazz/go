package kallas.zubrzycki;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

public class ChainOfStonesTest {
    @Test
    public void shouldNewChainBeEmpty() {
        ChainOfStones chainOfStones = new ChainOfStones();

        assertEquals(0, chainOfStones.getSize()); 
    }

    @Test
    public void shouldAddStone() {
        ChainOfStones chainOfStones = new ChainOfStones();
        chainOfStones.addStone(new Stone(1, 1, EPointColor.BLACK));

        assertEquals(1, chainOfStones.getSize()); 
    }

    @Test
    public void shouldGetStones() {
        ChainOfStones chainOfStones = new ChainOfStones();
        Stone firstStone = new Stone(1, 1, EPointColor.BLACK);
        Stone secondStone = new Stone(2, 2, EPointColor.WHITE);
        chainOfStones.addStone(firstStone);
        chainOfStones.addStone(secondStone);
        ArrayList<Stone> stones = chainOfStones.getStones();

        assertEquals(firstStone, stones.get(0)); 
        assertEquals(secondStone, stones.get(1)); 
    }

    @Test
    public void shouldCaptureStones() {
        ChainOfStones chainOfStones = new ChainOfStones();
        Stone firstStone = new Stone(1, 1, EPointColor.BLACK);
        Stone secondStone = new Stone(2, 2, EPointColor.WHITE);
        chainOfStones.addStone(firstStone);
        chainOfStones.addStone(secondStone);
        chainOfStones.becomeCaptured();
        ArrayList<Stone> stones = chainOfStones.getStones();

        assertEquals(EPointColor.NONE, stones.get(0).getColor()); 
        assertEquals(EPointColor.NONE, stones.get(1).getColor());
    }
}
