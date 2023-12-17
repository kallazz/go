package kallas.zubrzycki;

public interface IBoard {
    public void addErrorMessage(String errorMessage);
    public boolean checkMove(int x, int y, EPointColor playerColor);
    public void printBoard();
    public void updateBoard(int x, int y, EPointColor state);
}
