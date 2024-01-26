package kallas.zubrzycki;

public interface IBoard {
    public void addErrorMessage(String errorMessage);
    public boolean checkMove(int x, int y, EPointColor playerColor);
    public EPointColor getBoardPoint(int x, int y);
    public EPointColor[][] getBoardPoints();
    public void initialize(int size);
    public void printBoard();
    public void performMove(int x, int y, EPointColor state);
}
