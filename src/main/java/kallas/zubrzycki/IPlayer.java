package kallas.zubrzycki;

public interface IPlayer {
    public void addCapturedStones(int newlyCaptured);
    public int getCapturedStones();
    public EPointColor getColor();
    public String readInput();
}
