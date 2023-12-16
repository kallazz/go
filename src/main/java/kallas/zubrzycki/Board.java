package kallas.zubrzycki;

public class Board implements IBoard {
    private EPointColor[][] boardPoints;

    public Board(int size) {
        boardPoints = new EPointColor[size][size];
    }

    @Override
    public void printBoard() {

    }

    @Override
    public void updateBoard(int x, int y, EPointColor state) {

    }

    @Override
    public EPointColor[][] getBoardPoints() {
        return this.boardPoints;
}
