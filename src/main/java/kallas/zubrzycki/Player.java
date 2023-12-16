package kallas.zubrzycki;

public class Player implements IPlayer {

    private int capturedStones = 0;
    private EPointColor color;
    private Board board;

    Player(EPointColor color, Board board){
        this.color = color;
        this.board = board;
        this.board.addObserverPlayer(color);
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
