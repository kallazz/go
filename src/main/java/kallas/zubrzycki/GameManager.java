package kallas.zubrzycki;

public class GameManager implements IGameManager {
    private Board board;
    private Player player1;
    private Player player2;
    private static int BOARD_SIZE = 19;

    private boolean isPassedPlayer1 = false;
    private boolean isPassedPlayer2 = false;

    private Player currentPlayer;


    private GameHistory gameHistory = new GameHistory();

    @Override
    public void initializeGame() {
        player1 = new Player(EPointColor.BLACK);
        player2 = new Player(EPointColor.WHITE);
        currentPlayer = player1;
        board = new Board(BOARD_SIZE);
    }

    @Override
    public void beginGame() {
        while (!(isPassedPlayer1 && isPassedPlayer2)) { 
            boolean isInputCorrect = false;
            String input;
            do {
                input = currentPlayer.readInput();
                System.out.println("what is in input???");
                isInputCorrect = parseInput(input);
            } while (!isInputCorrect);
            if (!input.equals("pass")) {
                int x = Integer.parseInt(input.split(" ")[1]);
                int y = Integer.parseInt(input.split(" ")[2]);
                
                if(board.checkMove(x, y, currentPlayer.getColor())){
                    board.updateBoard(x, y, currentPlayer.getColor());
                    gameHistory.addToDatabase(input);
                    
                    
    
                } else {
                    System.out.println("Wrong move");
                }
            }
            currentPlayer = player1 == currentPlayer ? player2 : player1;
            board.printBoard();
        }
    }

    private boolean parseInput(String input) {
        System.out.println("Parsing input: " + input);
        if (input.equals("pass")) {
            System.out.println("we are passing");
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
