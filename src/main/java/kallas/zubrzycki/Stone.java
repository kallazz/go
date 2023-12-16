package kallas.zubrzycki;

public class Stone implements IStone{
    EPointColor color;
    int x;
    int y;
    ChainOfStones chain;

    public Stone(int x, int y, EPointColor color){
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public boolean areLibertiesAvailible(){
        int liberties = 0;
        if(Board.getBoardPoint(x, y + 1) == EPointColor.NONE || Board.getBoardPoint(x, y + 1) == color) {
            liberties++;
        }
        if(Board.getBoardPoint(x, y - 1) == EPointColor.NONE || Board.getBoardPoint(x, y - 1) == color) {
            liberties++;
        }
        if(Board.getBoardPoint(x + 1, y) == EPointColor.NONE || Board.getBoardPoint(x + 1, y) == color) {
            liberties++;
        }
        if(Board.getBoardPoint(x - 1, y) == EPointColor.NONE || Board.getBoardPoint(x - 1, y) == color) {
            liberties++;
        }

        if(liberties > 0){
            return true;
        } else {
            return false;
        }
    }

    public ChainOfStones getChain(){
        return chain;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
