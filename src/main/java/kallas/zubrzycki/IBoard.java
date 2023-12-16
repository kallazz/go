package kallas.zubrzycki;

public interface IBoard {
    public void printBoard();
    public void updateBoard(int x, int y, EPointColor playerColor);
    public boolean checkMove(int x, int y, EPointColor playerColor);
    public EPointColor[][] getBoardPoints();
}
