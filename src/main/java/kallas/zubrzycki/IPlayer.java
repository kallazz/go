package kallas.zubrzycki;

public interface IPlayer {
    public void attemptMove(int x, int y);
    public void pass();
    public int getCapturedStones();
}
