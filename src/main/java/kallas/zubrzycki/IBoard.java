package kallas.zubrzycki;

public interface IBoard {
    public void addErrorMessage(String errorMessage, int playerId);
    public boolean checkMove(int x, int y, EPointColor playerColor, int playerId);
    public Stone getBoardPoint(int x, int y);
    public Stone[][] getBoardPoints();
    public void initialize(int size, GameManager gameManager);
    public void performMove(int x, int y, Player player);
    public String getBoardView(int playerId, EPointColor playerColor, EPointColor currentPlayerColor);
}
