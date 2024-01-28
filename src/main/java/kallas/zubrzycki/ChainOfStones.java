package kallas.zubrzycki;

import java.util.ArrayList;

public class ChainOfStones implements IChainOfStones {
    private ArrayList<Stone> stones = new ArrayList<Stone>();

    public int countLiberties(final Stone[][] allStones) {
        int liberties = 0;
        for (Stone stone : stones) {
            liberties += stone.countLiberties(allStones);
        }
        return liberties;
    }

    public void becomeCaptured() {
        for (Stone stone : stones) {
            stone.setColor(EPointColor.NONE);
            stone.setChain(null);
        }
    }

    public ArrayList<Stone> getStones() {
        return stones;
    }

    public int getSize() {
        return stones.size();
    }

    @Override
    public void addStone(final Stone stone) {
        stones.add(stone);
    }
}
