package kallas.zubrzycki;

public interface IGameHistory {
    public void addToDatabase(String move);
    public String getPreviousMove(int index);
}
