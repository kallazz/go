package kallas.zubrzycki;

public class Board implements IBoard {
    private EPointColor[][] boardPoints;
    private int size;

    public Board(int size) {
        boardPoints = new EPointColor[size + 2][size + 2];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                boardPoints[i][j] = EPointColor.NONE;
            }
        }
        for(int i = 0; i < size; i ++){
            boardPoints[i][0] = EPointColor.BORDER;
            boardPoints[i][size - 1] = EPointColor.BORDER;
            boardPoints[0][i] = EPointColor.BORDER;
            boardPoints[size - 1][i] = EPointColor.BORDER;
        }
        this.size = size;
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
<<<<<<< HEAD

    @Override
    public boolean checkMove(int x, int y, EPointColor playerColor) {
        if(!(boardPoints[x][y] == EPointColor.NONE)){ // Check if the spot is empty
            return false;
        }
        //Check if there are availible liberties
        checkLiberties(x, y, playerColor);

        return true;
    }

    private boolean checkLiberties(int x, int y, EPointColor playerColor) {

    }
=======
>>>>>>> 7fb6f55 (Added Board tests)
}
