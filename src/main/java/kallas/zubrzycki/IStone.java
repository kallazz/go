package kallas.zubrzycki;

public interface IStone {
    public int countLiberties(Stone[][] boardPoints);
    public boolean doesExist();
    public ChainOfStones getChain();
    public EPointColor getColor();
    public int getX();
    public int getY();
    public void setChain(ChainOfStones chain);
}
