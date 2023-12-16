package kallas.zubrzycki;

import java.util.Scanner;

public class GameManager implements IGameManager {
    private Board board;
    private Player player1;
    private Player player2;
    private static int BOARD_SIZE = 19;

    private boolean isPassedPlayer1 = false;
    private boolean isPassedPlayer2 = false;

    private Player currentPlayer = player1;

    @Override
    public void initializeGame() {
        player1 = new Player(EPointColor.BLACK);
        player2 = new Player(EPointColor.WHITE);
        board = new Board(BOARD_SIZE);
    }

    @Override
    public void beginGame() {
        while (!(isPassedPlayer1 && isPassedPlayer2)) { 
            boolean isInputCorrect = false;
            do {
                final String input = currentPlayer.readInput();
                isInputCorrect = parseInput(input);
            } while (!isInputCorrect);

        }
    }

    private boolean parseInput(String input) {
        if (input.equals("pass")) {
            if (currentPlayer == player1) {
                isPassedPlayer1 = true;
            }
            else {
                isPassedPlayer2 = true;
            }
            return true;
        }
        else {
            final String[] words = input.split(" ");

            if (words[0].equals("go")) {
                try {
                    final int x = Integer.parseInt(words[1]);
                    final int y = Integer.parseInt(words[2]);

                    if (x < 0 || x > BOARD_SIZE || y < 0 || y > BOARD_SIZE) {
                        System.out.println("x and y should be in <1, " + BOARD_SIZE + ">");
                        return false;
                    }
                    else {
                        return true;
                    }
                }
                catch (Exception ex) {
                    System.out.println("Correct format: go <x> <y>");
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public void countScore() {

    }
}
