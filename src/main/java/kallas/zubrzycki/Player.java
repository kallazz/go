package kallas.zubrzycki;

public class Player implements IPlayer {

    private int capturedStones = 0;
    private EPointColor color;

    Player(EPointColor color){
        this.color = color;
    }

    @Override
    public void attemptMove(int x, int y){

    }

    @Override
    public void pass(){

    }
    
    @Override
    public int getCapturedStones(){
        return capturedStones;
    }
}
