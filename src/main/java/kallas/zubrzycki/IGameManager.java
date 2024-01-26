package kallas.zubrzycki;

public interface IGameManager {
    public void makeMove(String input, int playerId);
    public void countScore();
    public void initializeGame(int player1Id, int player2Id);
}
