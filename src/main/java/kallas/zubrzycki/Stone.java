package kallas.zubrzycki;

public class Stone implements IStone {
    private EPointColor color;
    private int x;
    private int y;
    private ChainOfStones chain;
    private boolean visited;
    public boolean visitedDuringScoreCount = false;

    public Stone(int x, int y, EPointColor color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public boolean isVisited(){
        return visited;
    }

    public void setVisited(boolean v){
        this.visited = v;
    }

    @Override
    public int countLiberties(Stone[][] boardPoints) {
        int liberties = 0;

        if (boardPoints[x][y+1].getColor() == EPointColor.NONE) {
            liberties++;
        }
        if (boardPoints[x][y-1].getColor() == EPointColor.NONE) {
            liberties++;
        }
        if (boardPoints[x+1][y].getColor() == EPointColor.NONE) {
            liberties++;
        }
        if (boardPoints[x-1][y].getColor() == EPointColor.NONE) {
            liberties++;
        }

        return liberties;
    }

    @Override
    public boolean doesExist() {
        return (color == EPointColor.NONE || color == EPointColor.BORDER) ? false : true;
    }

    @Override
    public ChainOfStones getChain() {
        return chain;
    }

    @Override
    public EPointColor getColor() {
        return color;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setChain(ChainOfStones chain) {
        this.chain = chain;
    }

    public void setColor(EPointColor color) {
        this.color = color;
    }
}
