package kallas.zubrzycki;

import java.util.ArrayList;

public class ChainOfStones implements IChainOfStones {
    ArrayList<Stone> stones;

    @Override
    public boolean willBeCaptured(int x, int y) {
        for(Stone stone : stones) {
            stone.checkLiberties();
        }
    }

}
