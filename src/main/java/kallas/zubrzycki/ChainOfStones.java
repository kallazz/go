package kallas.zubrzycki;

import java.util.ArrayList;

public class ChainOfStones implements IChainOfStones {
    ArrayList<Stone> stones = new ArrayList<Stone>();

    @Override
    public boolean willBeCaptured() {
        for (Stone stone: stones) {
            if (stone.areLibertiesAvailible()) {
                return false;
            }
        }
        return true;
    }

    public void addStone(Stone stone) {
        stones.add(stone);
    }
}
