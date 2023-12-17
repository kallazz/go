package kallas.zubrzycki;

public class GameManager implements IGameManager {
    private static int BOARD_SIZE = 19;

    private Board board;
    private GameHistory gameHistory;
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    private boolean isPassedPlayer1 = false;
    private boolean isPassedPlayer2 = false;

    @Override
    public void initializeGame() {
        board = Board.getInstance();
        board.initialize(BOARD_SIZE);
        gameHistory = GameHistory.getInstance();
        player1 = new Player(EPointColor.BLACK);
        player2 = new Player(EPointColor.WHITE);
        currentPlayer = player1;
    }

    @Override
    public void startGameLoop() {
        while (!(isPassedPlayer1 && isPassedPlayer2)) {
            board.printBoard();

            boolean isInputCorrect = false;
            String input;
            do {
                input = currentPlayer.readInput();
                isInputCorrect = parseInput(input);

                board.printBoard();
            } while (!isInputCorrect);

            if (!input.equals("pass")) {
                final int x = Integer.parseInt(input.split(" ")[1]);
                final int y = Integer.parseInt(input.split(" ")[2]);

                if (board.checkMove(x, y, currentPlayer.getColor())) {
                    board.updateBoard(x, y, currentPlayer.getColor());
                    gameHistory.addToDatabase(input);

                    // Reset this player's pass status
                    if (currentPlayer == player1) {
                        isPassedPlayer1 = false;
                    } else {
                        isPassedPlayer2 = false;
                    }
                } else {
                    board.addErrorMessage("Wrong move - you lose your turn");
                }
            } else {
                gameHistory.addToDatabase(input);

                if (currentPlayer == player1) {
                    isPassedPlayer1 = true;
                } else {
                    isPassedPlayer2 = true;
                }
            }

            currentPlayer = (player1 == currentPlayer) ? player2 : player1;
        }
    }

    private boolean parseInput(String input) {
        if (input.equals("pass")) {
            return true;
        } else {
            final String[] words = input.split(" ");

            if (words[0].equals("go")) {
                try {
                    final int x = Integer.parseInt(words[1]);
                    final int y = Integer.parseInt(words[2]);

                    if (x < 0 || x > BOARD_SIZE || y < 0 || y > BOARD_SIZE) {
                        board.addErrorMessage("x and y should be in <1, " + BOARD_SIZE + ">");
                        return false;
                    } else {
                        return true;
                    }
                } catch (Exception ex) {
                    board.addErrorMessage("Correct format: go <x> <y>");
                    return false;
                }
            }
        }
        board.addErrorMessage("Allowed actions are: 'go <x> <y>' or 'pass'");
        return false;
    }

    @Override
    public void countScore() {

    }
}
