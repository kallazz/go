package kallas.zubrzycki;

public class Board implements IBoard {
    private String[][] boardPoints;

    public Board(int size) {
        boardPoints = new String[size][size];
    }

    @Override
    public void printBoard() {

    }

    @Override
    public boolean updateBoard(int x, int y, EPointColor state) {

    }
}
