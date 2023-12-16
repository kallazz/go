package kallas.zubrzycki;

public class Stone implements IStone{
    EPointColor color;
    int x;
    int y;
    Board board;


    public boolean checkLiberties(){
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
}
