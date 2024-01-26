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
    public void initializeGame(int player1Id, int player2Id) {
        board = Board.getInstance();
        board.initialize(BOARD_SIZE);
        gameHistory = GameHistory.getInstance();
        player1 = new Player(EPointColor.BLACK, player1Id);
        player2 = new Player(EPointColor.WHITE, player2Id);
        currentPlayer = player1;
    }
    @Override
    public synchronized String makeMove(String input, int playerId) {
        // Quit if it's not this player's turn
        if (playerId != currentPlayer.getId()) {
            board.addErrorMessage("This is not your turn!");
            return board.getBoardView();
        }

        // Quit if input is incorrect
        if (!parseInput(input)) {
            return board.getBoardView();
        }

        // Handle correct input
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

        if (isPassedPlayer1 && isPassedPlayer2) {
            return "THE GAME IS FINISHED";
        }

        currentPlayer = (player1 == currentPlayer) ? player2 : player1;

        return board.getBoardView();
    }

    @Override
    public void startGameLoop() {
        while (!(isPassedPlayer1 && isPassedPlayer2)) {


            board.printBoard();
            processInput();

            currentPlayer = (player1 == currentPlayer) ? player2 : player1;
        }
    }

    private void processInput(){
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
                    board.performMove(x, y, currentPlayer.getColor());
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
            } else { // Here is what happens when a player types "pass"
                gameHistory.addToDatabase(input);

                if (currentPlayer == player1) {
                    isPassedPlayer1 = true;
                } else {
                    isPassedPlayer2 = true;
                }
            }
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

}
