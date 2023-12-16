package kallas.zubrzycki;

import java.util.Scanner;

public class GameManager implements IGameManager {
    

    private Board board;
    private Player player1;
    private Player player2;
    private boolean isDoublePassed = false;
    private boolean isPassed = false;
    private Player currentPlayer = player1;

    @Override
    public void initializeGame() {
        player1 = new Player(EPointColor.BLACK);
        player2 = new Player(EPointColor.WHITE);
        board = new Board(19);
    }

    @Override
    public void beginGame() {
        while (!isDoublePassed) { 
            getInput();

        }
    }

    @Override
    public void getInput() {
    }

    @Override
    public void countScore() {

    }
}
