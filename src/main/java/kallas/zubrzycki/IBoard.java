package kallas.zubrzycki;

public interface IBoard {
    public boolean checkMove(int x, int y, EPointColor playerColor);
    public void printBoard();
    public void updateBoard(int x, int y, EPointColor state);
}
