package kallas.zubrzycki;

import java.util.ArrayList;

public class ChainOfStones implements IChainOfStones {
    ArrayList<Stone> stones = new ArrayList<Stone>();

    public int countLiberties(Stone[][] allStones){
        int liberties = 0;
        for(Stone stone : stones) {
            liberties += stone.countLiberties(allStones);
        }
        return liberties;
    }

    public void becomeCaptured(){
        for (Stone stone : stones){
            stone.setColor(EPointColor.NONE);
        }
    }

    public int size(){
        return stones.size();
    }
    @Override
    public void addStone(Stone stone) {
        stones.add(stone);
    }
}
