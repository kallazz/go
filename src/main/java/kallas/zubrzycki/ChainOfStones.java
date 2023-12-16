package kallas.zubrzycki;

import java.util.ArrayList;

public class ChainOfStones implements IChainOfStones {
    ArrayList<Stone> stones;

    @Override
    public boolean willBeCaptured() {
        for(Stone stone : stones) {
            stone.areLibertiesAvailible();
        }
    }

    public void addStone(Stone stone) {
        stones.add(stone);
    }
}
