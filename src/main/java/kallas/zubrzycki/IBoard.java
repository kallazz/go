package kallas.zubrzycki;

public interface IBoard {
    public void printBoard();
    public void updateBoard(int x, int y, EPointColor state);
    public void addObserverPlayer(EPointColor playerColor);
    public void notifyPlayer(EPointColor playerColor);
}
