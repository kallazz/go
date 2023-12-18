package kallas.zubrzycki;

public class Stone implements IStone {
    private EPointColor color;
    private int x;
    private int y;
    private ChainOfStones chain;

    public Stone(int x, int y, EPointColor color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public boolean areLibertiesAvailible() {
        int liberties = 0;
        Board board = Board.getInstance();

        if (board.getBoardPoint(x, y + 1) == EPointColor.NONE || board.getBoardPoint(x, y + 1) == color) {
            liberties++;
        }
        if (board.getBoardPoint(x, y - 1) == EPointColor.NONE || board.getBoardPoint(x, y - 1) == color) {
            liberties++;
        }
        if (board.getBoardPoint(x + 1, y) == EPointColor.NONE || board.getBoardPoint(x + 1, y) == color) {
            liberties++;
        }
        if (board.getBoardPoint(x - 1, y) == EPointColor.NONE || board.getBoardPoint(x - 1, y) == color) {
            liberties++;
        }

        if (liberties > 0) {
            return true;
        } else {
            return false;
        }
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
